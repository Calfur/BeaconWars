package com.github.calfur.beaconWars.beaconFight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.PlayerModeManager;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.customTasks.TaskScheduler;
import com.github.calfur.beaconWars.database.BeaconFightJson;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;
import com.github.calfur.beaconWars.pvp.DeathBanPluginInteraction;
import com.github.calfur.beaconWars.pvp.Team;

public class BeaconFight {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	private BeaconFightManager manager;
	private List<BeaconRaid> beaconRaids = new ArrayList<BeaconRaid>();
	private List<UUID> teleportedPlayers = new ArrayList<UUID>();
	private int eventStartTaskId;
	private int naturallyEventEndTaskId;

	private BeaconFightJson beaconFightJson;

	public LocalDateTime getStartTime() {
		return beaconFightJson.getStartTime();
	}
	
	public LocalDateTime getEndTime() {
		return getStartTime().plusMinutes(getEventDurationInMinutes());
	}
	
	private int getEventDurationInMinutes() {
		return beaconFightJson.getEventDurationInMinutes();
	}
	
	public BeaconFight(BeaconFightManager manager, BeaconFightJson beaconFightJson) {
		this.manager = manager;
		this.beaconFightJson = beaconFightJson;
		
		long secondsTillStart = ChronoUnit.SECONDS.between(LocalDateTime.now(), getStartTime());
		if(secondsTillStart > 0) {
			eventStartTaskId = TaskScheduler.getInstance().scheduleDelayedTask(Main.getInstance(),
					new Runnable() {
				
						@Override
						public void run() {
							startBeaconFightEvent();
						}
				
					}, 
					getStartTime());
		}else {
			startBeaconFightEvent();
		}
	}
	
	public void cancelBeaconFightBeforeStarted() {
		TaskScheduler.getInstance().cancelTask(eventStartTaskId);
		end();
	}
	
	public void cancelOngoingBeaconFight() {
		sendEventCancelMessage();
		TaskScheduler.getInstance().cancelTask(naturallyEventEndTaskId);
		end();
	}

	public void addBeaconDestruction(Player player, Location beaconLocation) {
		PlayerJson attacker = playerDbConnection.getPlayer(player.getName());
		int attackerTeamId = attacker.getTeamId();
		
		Team attackerTeam = new Team(attackerTeamId, teamDbConnection.getTeam(attackerTeamId).getColor());
		Team defenderTeam = BeaconManager.getTeamByBeaconLocation(beaconLocation);
		
		beaconRaids.add(new BeaconRaid(attackerTeam, defenderTeam, player, beaconLocation, beaconFightJson.getRaidDurationInMinutes(), this));
	}
	
	@Nullable
	public Player getAttackerOfBeaconRaidByDefenderTeam(int defenderTeamId) {
		for (BeaconRaid beaconRaid : beaconRaids) {
			if(beaconRaid.getDefenderTeam().getId() == defenderTeamId) {
				return Bukkit.getPlayer(beaconRaid.getDestructorName());
			}
		}
		return null;
	}

	public void tryAddBeaconPlacement(Player placer, Location placedAgainst) {
		Team teamWhereBeaconWasPlaced = BeaconManager.getTeamByBeaconLocation(placedAgainst);
		PlayerJson attacker = playerDbConnection.getPlayer(placer.getName());
		
		if(teamWhereBeaconWasPlaced == null) {
			placer.sendMessage(StringFormatter.error("Du musst den Beacon an den Beacon von deinem Team plazieren"));
			return;
		}
		if(teamWhereBeaconWasPlaced.getId() != attacker.getTeamId()) {
			placer.sendMessage(StringFormatter.error("Du musst den Beacon an den Beacon von deinem Team plazieren, nicht an den Beacon von " + teamWhereBeaconWasPlaced.getColor() + "Team " + teamWhereBeaconWasPlaced.getId()));
			return;
		}		
		BeaconRaid beaconRaid = getBeaconRaid(placer.getName(), teamWhereBeaconWasPlaced);
		if(beaconRaid == null) {			
			placer.sendMessage(StringFormatter.error("Dein Team hat keinen laufenden Beaconraubzug, du solltest keinen Beacon haben"));
			BeaconManager.removeOneBeaconFromInventory(placer);
			return;
		}
		beaconRaid.addBeaconPlacement(placer);
	}

	public void removeBeaconRaid(BeaconRaid beaconRaid) {
		beaconRaids.remove(beaconRaid);
	}

	public void removeBeaconRaidsByDestructor(Player leaver) {
		BeaconRaid[] localBeaconRaids = beaconRaids.toArray(new BeaconRaid[beaconRaids.size()]);
		for (BeaconRaid beaconRaid : localBeaconRaids) {
			if(beaconRaid.getDestructorName().equalsIgnoreCase(leaver.getName())) {
				beaconRaid.doAttackPreventedActions();
			}
		}
	}

	public void teleportPlayerIfHeWasNot(Player player) {
		UUID uuid = player.getUniqueId();
		if(!teleportedPlayers.contains(uuid)) {
			BeaconManager.teleportPlayerToBeacon(player);
			teleportedPlayers.add(uuid);
		}
	}

	private void startBeaconFightEvent() {
		manager.activateBeaconFightEvent();
		sendEventStartMessage();
		deactivateBuildMode();
		DeathBanPluginInteraction.tryChangeBanDuration(configuration.getDeathBanDurationDuringBeaconFightInMinutes());
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
		
		BeaconManager.teleportAllOnlinePlayersToBeacon();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			teleportedPlayers.add(player.getUniqueId());
		}

		naturallyEventEndTaskId = TaskScheduler.getInstance().scheduleDelayedTask(Main.getInstance(), 
				new Runnable() {
			
					@Override
					public void run() {
						stopBeaconFightNaturally();
					}
					
				}, 
				getStartTime().plusMinutes(getEventDurationInMinutes()));
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
			if(beaconRaid.getAttackerTeam().getId() == attackerTeam.getId()) {
				beaconRaidsFromTeam.add(beaconRaid);
			}
		}
		return beaconRaidsFromTeam;
	}
	
	private void stopBeaconFightNaturally() {
		sendEventDeactivatedMessage();
		end();
	}

	private void end() {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle true");
		DeathBanPluginInteraction.tryChangeBanDuration(configuration.getDeathBanDurationInMinutes());
		BeaconRaid[] localBeaconRaids = beaconRaids.toArray(new BeaconRaid[beaconRaids.size()]);
		for (BeaconRaid beaconRaid : localBeaconRaids) {
			beaconRaid.doTimeOverActions();
		}
		manager.removeOngoingBeaconFight(this);
	}

	private void sendEventStartMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent startet jetzt!");
		Bukkit.broadcastMessage("In den nächsten " + getEventDurationInMinutes() + "min können eure Beacons geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");		
		Bukkit.broadcastMessage(" ");
	}
	
	private void sendEventCancelMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent wurde " + ChatColor.DARK_RED + "abgebrochen");
		Bukkit.broadcastMessage("Ab sofort können keine Beacons mehr geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");	
		Bukkit.broadcastMessage(" ");	
	}

	private void sendEventDeactivatedMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent wurde beendet");
		Bukkit.broadcastMessage("Ab sofort können keine Beacons mehr geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");	
		Bukkit.broadcastMessage(" ");
	}
	
	private void deactivateBuildMode() {
		PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
		playerModeManager.reloadPlayerModeForAllOnlinePlayers();
	}
}
