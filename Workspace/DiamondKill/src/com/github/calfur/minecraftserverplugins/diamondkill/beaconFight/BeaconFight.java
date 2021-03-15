package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.BeaconManager;
import com.github.calfur.minecraftserverplugins.diamondkill.DeathBanPluginInteraction;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.PlayerModeManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Team;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;

import net.md_5.bungee.api.ChatColor;

public class BeaconFight {
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	private LocalDateTime startTime;
	private BeaconFightManager manager;
	private long durationInMinutes;
	private List<BeaconRaid> beaconRaids = new ArrayList<BeaconRaid>();

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
				teleportPlayerIntoOverworld(player);
			}
		}
	}

	private void teleportPlayerIntoOverworld(Player player) {
		Location bedSpawnLocation = player.getBedSpawnLocation();
		if(bedSpawnLocation != null) {
			player.teleport(bedSpawnLocation);
		}else {
			Location beaconLocation = BeaconManager.getBeaconLocationByPlayer(player);
			beaconLocation.setY(beaconLocation.getBlockY() + 1);
			player.teleport(beaconLocation);
		}
	}
	
	private void deactivateBuildMode() {
		PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			playerModeManager.reloadPlayerMode(player);
		}
	}

	public void addBeaconDestruction(Player player, Location beaconLocation) {
		PlayerJson attacker = playerDbConnection.getPlayer(player.getName());
		int attackerTeamId = attacker.getTeamId();
		
		Team attackerTeam = new Team(attackerTeamId, teamDbConnection.getTeam(attackerTeamId).getColor());
		Team defenderTeam = BeaconManager.getTeamByBeaconLocation(beaconLocation);
		
		beaconRaids.add(new BeaconRaid(attackerTeam, defenderTeam, player, beaconLocation, this));
	}

	public void addBeaconPlacement(Player placer, Location placedAgainst) {
		Team teamWhereBeaconWasPlaced = BeaconManager.getTeamByBeaconLocation(placedAgainst);
		PlayerJson attacker = playerDbConnection.getPlayer(placer.getName());
		
		if(teamWhereBeaconWasPlaced == null) {
			placer.sendMessage("Du musst den Beacon an den Beacon von deinem Team plazieren");
			return;
		}
		if(teamWhereBeaconWasPlaced.getId() != attacker.getTeamId()) {
			placer.sendMessage("Du musst den Beacon an den Beacon von deinem Team plazieren, nicht an den Beacon von " + teamWhereBeaconWasPlaced.getColor() + "Team " + teamWhereBeaconWasPlaced.getId());
			return;
		}		
		BeaconRaid beaconRaid = getBeaconRaid(placer.getName(), teamWhereBeaconWasPlaced);
		if(beaconRaid == null) {			
			placer.sendMessage(ChatColor.RED + "Dein Team hat keinen laufenden Beaconraubzug, du solltest keinen Beacon haben");
			return;
		}
		beaconRaid.addBeaconPlacement(attacker, placer);
	}
	
	/**
	 * 
	 * @param attackerName
	 * @param attackerTeam
	 * @return the first beaconRaid which matches attackerName and attackerTeam, the first which matches the attackerTeam or null
	 */
	private BeaconRaid getBeaconRaid(String attackerName, Team attackerTeam) {
		List<BeaconRaid> beaconRaidsFromTeam = getBeaconRaidsFromTeam(attackerTeam);
		if(beaconRaidsFromTeam.size() == 0) {
			return null;
		}
		for (BeaconRaid beaconRaid : beaconRaidsFromTeam) {
			if(beaconRaid.getDestructorName().equalsIgnoreCase(attackerName)) {
				return beaconRaid;
			}
		}
		return beaconRaidsFromTeam.get(0);
	}

	private List<BeaconRaid> getBeaconRaidsFromTeam(Team attackerTeam) {
		List<BeaconRaid> beaconRaidsFromTeam = new ArrayList<BeaconRaid>();
		for (BeaconRaid beaconRaid : beaconRaids) {
			if(beaconRaid.getAttacker().getId() == attackerTeam.getId()) {
				beaconRaidsFromTeam.add(beaconRaid);
			}
		}
		return beaconRaidsFromTeam;
	}

	public void removeBeaconRaid(BeaconRaid beaconRaid) {
		beaconRaids.remove(beaconRaid);
	}
	

}
