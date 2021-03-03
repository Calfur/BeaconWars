package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;

public class PlayerModeManager {
	private HashMap<String, PlayerMode> playerModes = new HashMap<String, PlayerMode>();
	private static final int buildModeCooldownInMinutes = 30;
	private static final int baseRange = 100;
	
	public boolean toggleBuildMode(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode.isBuildModeActive()) {
			playerMode.deactivateBuildMode();
			player.sendMessage(ChatColor.GREEN + "Baumodus deaktiviert");
			return true;
		}else {
			return activateBuildModeIfAllowed(playerMode, player);
		}
	}

	private PlayerMode getPlayerMode(String playerName) {
		return playerModes.get(playerName.toLowerCase());
	}
	
	private PlayerMode addPlayerMode(Player player) {
		PlayerMode playerMode = new PlayerMode(player);
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
		}
		return false;
	}
	
	private static boolean isPlayerWithinRangeOfHisBase(Player player) {
		Location playerLocation = player.getLocation();
		Location baseLocation = Main.getInstance().getTeamDbConnection().getTeam((Main.getInstance().getPlayerDbConnection().getPlayer(player.getName()).getTeamId())).getBeaconPosition();
		int distanceX = Math.abs(playerLocation.getBlockX() - baseLocation.getBlockX());
		int distanceY = Math.abs(playerLocation.getBlockY() - baseLocation.getBlockY());
		double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
		return (distance < baseRange) && (playerLocation.getWorld() == baseLocation.getWorld());
	}
}
