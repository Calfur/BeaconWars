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

import net.md_5.bungee.api.ChatColor;

public class BeaconFight {
	private LocalDateTime startTime;
	private BeaconFightManager manager;
	private static final long durationInMinutes = 90;

	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public LocalDateTime getEndTime() {
		return startTime.plusMinutes(durationInMinutes);
	}
	
	public BeaconFight(LocalDateTime startTime, BeaconFightManager manager) {
		this.startTime = startTime;
		this.manager = manager;
		
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
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
	}
	
	public void cancelBeaconFightEvent() {
		manager.deactivateBeaconFightEvent();
		sendEventCancelMessage();
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

}
