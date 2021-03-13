package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.BeaconManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.PlayerModeManager;

import net.md_5.bungee.api.ChatColor;

public class BeaconFight {
	private LocalDateTime startTime;
	private BeaconFightManager manager;
	private long durationInMinutes = 1;

	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public LocalDateTime getEndTime() {
		return startTime.plusMinutes(durationInMinutes);
	}
	
	public BeaconFight(LocalDateTime startTime, long durationInMinutes, BeaconFightManager manager) {
		this.startTime = startTime;
		this.manager = manager;
		this.durationInMinutes = durationInMinutes;
		
		long ticksTillStart = ChronoUnit.SECONDS.between(LocalDateTime.now(), startTime)*20;
		if(ticksTillStart > 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {					
				@Override
				public void run() {
					startBeaconFightEvent();
				}					
			}, ticksTillStart);
		}else {
			startBeaconFightEvent();
		}
	}
	
	private void startBeaconFightEvent() {
		teleportAllOnlinePlayersIntoOverworld();
		manager.activateBeaconFightEvent();
		sendEventStartMessage();
		deactivateBuildMode();
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {					
			@Override
			public void run() {
				stopBeaconFight();
			}
		}, durationInMinutes*60*20);
	}

	public void cancelBeaconFightEvent() {
		manager.deactivateBeaconFightEvent(this);
		sendEventCancelMessage();
	}
	
	private void stopBeaconFight() {
		manager.deactivateBeaconFightEvent(this);
		sendEventDeactivatedMessage();
	}

	private void sendEventStartMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent startet jetzt!");
		Bukkit.broadcastMessage("In den nächsten " + durationInMinutes + "min können eure Beacons geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");		
	}
	
	private void sendEventCancelMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent wurde " + ChatColor.DARK_RED + "abgebrochen");
		Bukkit.broadcastMessage("Ab sofort können keine Beacons mehr geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");		
	}

	private void sendEventDeactivatedMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent wurde beendet");
		Bukkit.broadcastMessage("Ab sofort können keine Beacons mehr geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");		
	}
	
	private void teleportAllOnlinePlayersIntoOverworld() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			if(player.getWorld().getEnvironment() != World.Environment.NORMAL) {
				Location bedSpawnLocation = player.getBedSpawnLocation();
				if(bedSpawnLocation != null) {
					player.teleport(bedSpawnLocation);
				}else {
					Location beaconLocation = BeaconManager.getBeaconLocationByPlayer(player);
					beaconLocation.setY(beaconLocation.getBlockY() + 1);
					player.teleport(beaconLocation);
				}
			}
		}
	}
	
	private void deactivateBuildMode() {
		PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			playerModeManager.reloadPlayerMode(player);
		}
	}

}
