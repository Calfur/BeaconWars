package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.BeaconManager;
import com.github.calfur.minecraftserverplugins.diamondkill.DeathBanPluginInteraction;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.PlayerModeManager;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;

import net.md_5.bungee.api.ChatColor;

public class BeaconFight {
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
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
		DeathBanPluginInteraction.tryChangeBanDuration(2);
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
		DeathBanPluginInteraction.tryChangeBanDuration(10);
	}
	
	private void stopBeaconFight() {
		manager.deactivateBeaconFightEvent(this);
		sendEventDeactivatedMessage();
		DeathBanPluginInteraction.tryChangeBanDuration(10);
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

	public void addBeaconBreak(Player player, Location beaconLocation) {
		PlayerJson attacker = playerDbConnection.getPlayer(player.getName());
		int attackerTeamId = attacker.getTeamId();
		TeamJson attackerTeamJson = teamDbConnection.getTeam(attackerTeamId);
		
		Entry<String, TeamJson> defenderTeam = BeaconManager.getTeamByBeaconLocation(beaconLocation);
		String defenderTeamId = defenderTeam.getKey();
		
		TeamJson defenderTeamJson = defenderTeam.getValue();
		Bukkit.broadcastMessage(player.getName() + " von " + attackerTeamJson.getColor() + "Team " + attackerTeamId + ChatColor.RESET + " hat den Beacon von " + defenderTeamJson.getColor() + "Team " + defenderTeamId + ChatColor.RESET + " abgebaut");
		Bukkit.broadcastMessage("Der Beacon muss innerhalb von 15min");
		Bukkit.broadcastMessage("zurück zur Basis von " + attackerTeamJson.getColor() + "Team " + attackerTeamId + ChatColor.RESET + " gebracht werden");
	
		String bossBarName = attackerTeamJson.getColor() + "Team " + attackerTeamId + ChatColor.RESET + " klaut den Beacon von " + defenderTeamJson.getColor() + "Team " + defenderTeamId;
		LocalDateTime deadline = LocalDateTime.now().plusMinutes(15);
		Main.getInstance().getScoreboardLoader().addBossBar(bossBarName, attackerTeamJson.getColor(), deadline);
	}
}
