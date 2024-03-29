package com.github.calfur.beaconWars;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;
import com.github.calfur.beaconWars.pvp.TeamAttackManager;
import com.github.calfur.beaconWars.pvp.TopKiller;

public class PlayerModeManager {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamAttackManager teamAttackManager = Main.getInstance().getTeamAttackManager();
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	
	private HashMap<String, PlayerMode> playerModes = new HashMap<String, PlayerMode>();
	private final int buildModeRangeCheckDelayInSeconds = 1;
		
	public PlayerModeManager() {
		HashMap<String, PlayerJson> players = playerDbConnection.getPlayers();
		for (Entry<String, PlayerJson> player : players.entrySet()) {
			addPlayerMode(player.getKey());
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for (PlayerMode playerMode : playerModes.values()) {					
					if(playerMode.isBuildModeActive()) {
						Player player = playerMode.getPlayer();
						if(player != null) {							
							if(!isPlayerWithinRangeOfHisBase(player)) {
								int secondsUntilBuildModeGetsDeactivated = playerMode.getSecondsUntilBuildModeGetsDeactivated();
								if(secondsUntilBuildModeGetsDeactivated <= 0) {
									deactivateBuildMode(playerMode);	
								}else {
									playerMode.reduceSecondsLeftUntilBuildModeGetsDeactivated(buildModeRangeCheckDelayInSeconds);
									player.sendMessage(StringFormatter.error("Nicht mehr im Base Bereich! Baumodus wird in " + secondsUntilBuildModeGetsDeactivated + "s automatisch deaktiviert!"));
								}
							}else {
								playerMode.resetSecondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange();
							}					
						}						
					}
				}				
			}
		}, buildModeRangeCheckDelayInSeconds*20, buildModeRangeCheckDelayInSeconds*20); //repeating all 1s
	}
	
	public boolean toggleBuildMode(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode != null) {			
			if(playerMode.isBuildModeActive()) {
				deactivateBuildMode(playerMode);
				return true;
			}
		}
		return activateBuildModeIfAllowed(playerMode, player);
	}

	public boolean isPlayerAllowedToFight(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode == null) {
			return true;
		}else {
			if(playerMode.isBuildModeActive()) {
				return false;
			}
			return true;
		}
	}
	
	public void reloadPlayerMode(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode == null) {
			PlayerMode.removeModeEffects(player);
		}else {
			if(Main.getInstance().getBeaconFightManager().isBeaconEventActive()) {
				deactivateBuildMode(playerMode);
			}
			playerMode.reloadEffects();
		}
	}
	
	public void reloadPlayerModeForAllOnlinePlayers() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			reloadPlayerMode(player);
		}
	}
	
	public boolean isPlayerInBuildMode(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode == null) {
			return false;
		}else {
			if(playerMode.isBuildModeActive()) {
				return true;
			}
			return false;
		}
	}

	public void updateHighlight(Player player, TopKiller topKiller) {
		if(player.getName().equalsIgnoreCase(topKiller.getName()) 
				&& topKiller.getDiamondValue() >= 5
				&& !beaconFightManager.isBeaconEventActive()) {
			activatePlayerHighlight(player);
		}else {
			deactivatePlayerHighlight(player);
		}
	}
	
	private void activatePlayerHighlight(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode == null) {
			playerMode = addPlayerMode(player.getName());		
		}
		playerMode.activateHighlighted();
	}
	
	private void deactivatePlayerHighlight(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode != null) {
			playerMode.deactivateHighlighted();
		}
	}
	
	private PlayerMode getPlayerMode(String playerName) {
		return playerModes.get(playerName.toLowerCase());
	}
	
	private PlayerMode addPlayerMode(String playerName) {
		PlayerMode playerMode = new PlayerMode(playerName);
		playerModes.put(playerName.toLowerCase(), playerMode);
		return playerMode;
	}
	
	private boolean isPlayerWithinRangeOfHisBase(Player player) {
		int teamId = playerDbConnection.getPlayer(player.getName()).getTeamId();
		Location baseLocation = teamDbConnection.getTeam(teamId).getBeaconLocation();
		Location playerLocation = player.getLocation();
		int distanceX = Math.abs(playerLocation.getBlockX() - baseLocation.getBlockX());
		int distanceZ = Math.abs(playerLocation.getBlockZ() - baseLocation.getBlockZ());
		double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceZ, 2));
		return (distance < configuration.getBuildModeBaseRangeRadiusInBlocks()) && (playerLocation.getWorld() == baseLocation.getWorld());
	}
	
	private void deactivateBuildMode(PlayerMode playerMode) {
		if(playerMode.isBuildModeActive()) {			
			playerMode.deactivateBuildMode();
			playerMode.getPlayer().sendMessage(ChatColor.GREEN + "Baumodus deaktiviert");
			Main.getInstance().getScoreboardLoader().reloadScoreboardFor(playerMode.getPlayer());
		}
	}
	
	private void activateBuildMode(PlayerMode playerMode) {
		playerMode.activateBuildMode();
		playerMode.getPlayer().sendMessage(ChatColor.GREEN + "Baumodus aktiviert." + ChatColor.RESET + " Deaktivieren mit /buildmode");
		Main.getInstance().getScoreboardLoader().reloadScoreboardFor(playerMode.getPlayer());		
	}
	
	private boolean activateBuildModeIfAllowed(PlayerMode playerMode, Player player) {
		
		if(Main.getInstance().getBeaconFightManager().isBeaconEventActive()) {
			player.sendMessage(StringFormatter.error("W�hrend einem Beacon Event kann der Baumodus nicht aktiviert werden"));
			return false;
		}
		
		if(!playerDbConnection.existsPlayer(player.getName())){
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(player));	
			return false;
		}
		
		int teamId = playerDbConnection.getPlayer(player.getName()).getTeamId();
		if(teamAttackManager.isTeamFighting(teamId)) {	
			player.sendMessage(StringFormatter.error("Dein Team befindet sich momentan noch in einem Kampf. Der Baumodus kann erst aktiviert werden wenn der Kampf nicht mehr auf dem Scoreboard angezeigt wird."));
			return false;
		}
		
		if(!isPlayerWithinRangeOfHisBase(player)) {	
			player.sendMessage(StringFormatter.error("Du befindest dich mehr als " + configuration.getBuildModeBaseRangeRadiusInBlocks() + " Bl�cke von deinem Beacon entfernt. Der Baumodus kann hier nicht aktiviert werden."));
			return false;
		}
		
		if(playerMode == null) {
			playerMode = addPlayerMode(player.getName());
			activateBuildMode(playerMode);
			return true;
		}
		
		if(playerMode.getBuildModeCooldownEnd().isAfter(LocalDateTime.now())) {
			long remainingCooldownInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), playerMode.getBuildModeCooldownEnd());
			player.sendMessage(StringFormatter.error("Der Baumodus kann erst in " + StringFormatter.displaySecondsAsTime(remainingCooldownInSeconds, StringFormatter.errorColor) + " erneut aktiviert werden"));
			return false;
		}
		
		activateBuildMode(playerMode);
		return true;
	}
}
