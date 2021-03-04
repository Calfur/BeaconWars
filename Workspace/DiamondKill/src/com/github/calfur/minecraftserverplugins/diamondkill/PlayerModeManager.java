package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;

public class PlayerModeManager {
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	
	private HashMap<String, PlayerMode> playerModes = new HashMap<String, PlayerMode>();
	private static final int buildModeCooldownInMinutes = 30;
	private static final int baseRange = 100;
	
	public boolean toggleBuildMode(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode != null) {			
			if(playerMode.isBuildModeActive()) {
				playerMode.deactivateBuildMode();
				player.sendMessage(ChatColor.GREEN + "Baumodus deaktiviert");
				return true;
			}
		}
		return activateBuildModeIfAllowed(playerMode, player);
	}

	private PlayerMode getPlayerMode(String playerName) {
		return playerModes.get(playerName.toLowerCase());
	}
	
	private PlayerMode addPlayerMode(Player player) {
		PlayerMode playerMode = new PlayerMode(player.getName());
		playerModes.put(player.getName().toLowerCase(), playerMode);
		return playerMode;
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
	
	private boolean activateBuildModeIfAllowed(PlayerMode playerMode, Player player) {
		if(isPlayerWithinRangeOfHisBase(player)) {			
			if(playerMode == null) {
				playerMode = addPlayerMode(player);
				playerMode.activateBuildMode();
				player.sendMessage(ChatColor.GREEN + "Baumodus aktiviert." + ChatColor.RESET + " Deaktivieren mit /buildmode");
				return true;
			}
			long minutesSinceDeactivated = ChronoUnit.SECONDS.between(playerMode.getDeactivatedAt(), LocalDateTime.now())/60;
			if(minutesSinceDeactivated > buildModeCooldownInMinutes) {
				playerMode.activateBuildMode();
				player.sendMessage(ChatColor.GREEN + "Baumodus aktiviert." + ChatColor.RESET + " Deaktivieren mit /buildmode");
				return true;
			}else {
				player.sendMessage(ChatColor.RED + "Der Baumodus kann erst in " + (buildModeCooldownInMinutes - minutesSinceDeactivated) + " Minuten erneut aktiviert werden");
				return false;
			}
		}else {
			player.sendMessage(ChatColor.RED + "Du befindest dich mehr als " + baseRange + " Blöcke von deinem Beacon entfernt. Der Baumodus kann hier nicht aktiviert werden.");
		}
		return false;
	}
	
	private boolean isPlayerWithinRangeOfHisBase(Player player) {
		int teamId = playerDbConnection.getPlayer(player.getName()).getTeamId();
		Location baseLocation = teamDbConnection.getTeam(teamId).getBeaconPosition();
		Location playerLocation = player.getLocation();
		int distanceX = Math.abs(playerLocation.getBlockX() - baseLocation.getBlockX());
		int distanceZ = Math.abs(playerLocation.getBlockZ() - baseLocation.getBlockZ());
		double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceZ, 2));
		return (distance < baseRange) && (playerLocation.getWorld() == baseLocation.getWorld());
	}
	
	public void reloadPlayerMode(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode == null) {
			PlayerMode.reloadPotionEffects(player);
		}else {
			playerMode.reloadPotionEffects();
		}
	}
}
