package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerModeManager {
	private HashMap<String, PlayerMode> playerModes = new HashMap<String, PlayerMode>();
	private static final int buildModeCooldownInMinutes = 30;
	
	public boolean toggleBuildMode(Player player) {
		PlayerMode playerMode = getPlayerMode(player.getName());
		if(playerMode == null) {
			playerMode = addPlayerMode(player);
			playerMode.activateBuildMode();
			player.sendMessage(ChatColor.GREEN + "Baumodus aktiviert." + ChatColor.RESET + " Deaktivieren mit /buildmode");
			return true;
		}
		if(playerMode.isBuildModeActive()) {
			playerMode.deactivateBuildMode();
			player.sendMessage(ChatColor.GREEN + "Baumodus deaktiviert");
			return true;
		}else {
			long minutesSinceDeactivated = ChronoUnit.SECONDS.between(playerMode.getDeactivatedAt(), LocalDateTime.now())/60;
			if(minutesSinceDeactivated > buildModeCooldownInMinutes) {
				playerMode.activateBuildMode();
				return false;
			}else {
				player.sendMessage(ChatColor.RED + "Der Baumodus kann erst in " + (buildModeCooldownInMinutes - minutesSinceDeactivated) + " Minuten erneut aktiviert werden");
				return false;
			}
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
}
