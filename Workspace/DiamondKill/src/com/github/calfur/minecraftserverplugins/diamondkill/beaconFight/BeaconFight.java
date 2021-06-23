package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.calfur.minecraftserverplugins.diamondkill.BeaconManager;
import com.github.calfur.minecraftserverplugins.diamondkill.DeathBanPluginInteraction;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.PlayerModeManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Team;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class BeaconFight {
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	private LocalDateTime startTime;
	private BeaconFightManager manager;
	private long eventDurationInMinutes;
	private int attackDurationInMinutes;
	private List<BeaconRaid> beaconRaids = new ArrayList<BeaconRaid>();
	private HashMap<Integer, Integer> amountOfLostDefensesPerTeams = new HashMap<Integer, Integer>();
	int totalDefenceReward = 3;
	private BukkitTask naturallyEventEndTask;
	private BukkitTask eventStartTask;

	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public LocalDateTime getEndTime() {
		return startTime.plusMinutes(eventDurationInMinutes);
	}
	
	public BeaconFight(LocalDateTime startTime, long eventDurationInMinutes, int attackDurationInMinutes, BeaconFightManager manager) {
		this.startTime = startTime;
		this.manager = manager;
		this.eventDurationInMinutes = eventDurationInMinutes;
		this.attackDurationInMinutes = attackDurationInMinutes;
		
		long ticksTillStart = ChronoUnit.SECONDS.between(LocalDateTime.now(), startTime)*20;
		if(ticksTillStart > 0) {
			eventStartTask = new BukkitRunnable() {					
				@Override
				public void run() {
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							startBeaconFightEvent();
						}
						
					});
				}					
			}.runTaskLaterAsynchronously(Main.getInstance(), ticksTillStart);
		}else {
			startBeaconFightEvent();
		}
	}
	
	private void startBeaconFightEvent() {
		BeaconManager.teleportAllOnlinePlayersToBeacon();
		manager.activateBeaconFightEvent();
		sendEventStartMessage();
		deactivateBuildMode();
		DeathBanPluginInteraction.tryChangeBanDuration(2);
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
		
		naturallyEventEndTask = new BukkitRunnable() {					
			@Override
			public void run() {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						stopBeaconFightNaturally();
					}
				});
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), eventDurationInMinutes*60*20);
	}

	public void cancelBeaconFightBeforeStarted() {
		eventStartTask.cancel();
		end();
	}
	
	public void cancelOngoingBeaconFight() {
		sendEventCancelMessage();
		naturallyEventEndTask.cancel();
		end();
	}
	
	private void stopBeaconFightNaturally() {
		HashMap<Integer, Integer> defenseRewardPerTeams = calculateDefenseRewardPerTeams();
		sendEventDeactivatedMessage(defenseRewardPerTeams);
		payDefenderBounty(defenseRewardPerTeams);
		end();
	}

	private void end() {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle true");
		DeathBanPluginInteraction.tryChangeBanDuration(10);
		BeaconRaid[] localBeaconRaids = beaconRaids.toArray(new BeaconRaid[beaconRaids.size()]);
		for (BeaconRaid beaconRaid : localBeaconRaids) {
			beaconRaid.doTimeOverActions();
		}
		manager.removeOngoingBeaconFight(this);
	}
	
	private void payDefenderBounty(HashMap<Integer, Integer> defenseRewardPerTeams) {
		for (Entry<Integer, Integer> defenseReward : defenseRewardPerTeams.entrySet()) {
			int teamId = defenseReward.getKey();
			int reward = defenseReward.getValue();
			TeamJson teamJson = teamDbConnection.getTeam(teamId);
			String teamLeaderName = teamJson.getTeamLeader();
			PlayerJson playerJson = playerDbConnection.getPlayer(teamLeaderName);
			if(playerJson != null) {				
				playerJson.addCollectableDiamonds(reward);
				playerDbConnection.addPlayer(teamLeaderName, playerJson);
			}else {
				Bukkit.broadcastMessage(StringFormatter.Error("Der Teamleader " + teamLeaderName + " von") + teamJson.getColor() + "Team " + teamId + StringFormatter.Error(" wurde noch nicht registriert"));
			}
		}
	}

	private void sendEventStartMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent startet jetzt!");
		Bukkit.broadcastMessage("In den nächsten " + eventDurationInMinutes + "min können eure Beacons geklaut werden");
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

	private void sendEventDeactivatedMessage(HashMap<Integer, Integer> rewardsPerTeam) {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent wurde beendet");
		Bukkit.broadcastMessage("Ab sofort können keine Beacons mehr geklaut werden");
		Bukkit.broadcastMessage("Folgende Belohnungen werden an die Teamleader ausgezahlt:");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage("Team | Verlorene Verteidigungen | Belohnung");
		for (Entry<String, TeamJson> team : teamDbConnection.getTeams().entrySet()) {
			Integer teamId = Integer.parseInt(team.getKey());
			Integer amountOfLostDefenses = getAmountOfLostDefenses(teamId);
			int defenseReward = rewardsPerTeam.get(teamId);
			Bukkit.broadcastMessage(team.getValue().getColor() + "" + teamId + ChatColor.RESET + "      | " + amountOfLostDefenses + "                               | " + ChatColor.AQUA + defenseReward + " Dias");
		}
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");	
		Bukkit.broadcastMessage(" ");
	}
	
	private HashMap<Integer, Integer> calculateDefenseRewardPerTeams() {
		HashMap<Integer, Integer> rewardsPerTeams = getZeroRewardsPerTeams();
		
		HashMap<Integer, Integer> teamsWithLowestAmountOfLostDefenses = getTeamsWithLowestAmountOfLostDefenses(rewardsPerTeams.keySet());
		
		int reward = Math.round(totalDefenceReward / teamsWithLowestAmountOfLostDefenses.size());
		
		for (Entry<Integer, Integer> teamWithLowestAmountOfLostDefenses : teamsWithLowestAmountOfLostDefenses.entrySet()) {
			rewardsPerTeams.put(teamWithLowestAmountOfLostDefenses.getKey(), reward);
		}
		
		return rewardsPerTeams;
	}

	private HashMap<Integer, Integer> getZeroRewardsPerTeams() {
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (Entry<String, TeamJson> team : teamDbConnection.getTeams().entrySet()) {
			result.put(Integer.parseInt(team.getKey()), 0);
		}
		return result;
	}

	private HashMap<Integer, Integer> getTeamsWithLowestAmountOfLostDefenses(Set<Integer> teamIds) {
		HashMap<Integer, Integer> teamsWithLowestAmountOfLostDefenses = new HashMap<>();
		
		for(int teamId : teamIds) {
			int amountOfLostDefenses = getAmountOfLostDefenses(teamId);
			
			if(teamsWithLowestAmountOfLostDefenses.size() == 0) { // first time in the loop			
				teamsWithLowestAmountOfLostDefenses.put(teamId, amountOfLostDefenses);
			}else {
				int amountOfLostDefensesOfFirstEntry = teamsWithLowestAmountOfLostDefenses.values().toArray(new Integer[teamsWithLowestAmountOfLostDefenses.size()])[0];
				if(amountOfLostDefenses < amountOfLostDefensesOfFirstEntry) { // lower amount of defenses
					teamsWithLowestAmountOfLostDefenses = new HashMap<Integer, Integer>();
					teamsWithLowestAmountOfLostDefenses.put(teamId, amountOfLostDefenses);
				}else if(amountOfLostDefensesOfFirstEntry == amountOfLostDefenses) { // same amount of defenses
					teamsWithLowestAmountOfLostDefenses.put(teamId, amountOfLostDefenses);
				}
			}
		}
		return teamsWithLowestAmountOfLostDefenses;
	}
	
	private void deactivateBuildMode() {
		PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
		playerModeManager.reloadPlayerModeForAllOnlinePlayers();
	}

	public void addBeaconDestruction(Player player, Location beaconLocation) {
		PlayerJson attacker = playerDbConnection.getPlayer(player.getName());
		int attackerTeamId = attacker.getTeamId();
		
		Team attackerTeam = new Team(attackerTeamId, teamDbConnection.getTeam(attackerTeamId).getColor());
		Team defenderTeam = BeaconManager.getTeamByBeaconLocation(beaconLocation);
		
		beaconRaids.add(new BeaconRaid(attackerTeam, defenderTeam, player, beaconLocation, attackDurationInMinutes, this));
	}

	public void tryAddBeaconPlacement(Player placer, Location placedAgainst) {
		Team teamWhereBeaconWasPlaced = BeaconManager.getTeamByBeaconLocation(placedAgainst);
		PlayerJson attacker = playerDbConnection.getPlayer(placer.getName());
		
		if(teamWhereBeaconWasPlaced == null) {
			placer.sendMessage(StringFormatter.Error("Du musst den Beacon an den Beacon von deinem Team plazieren"));
			return;
		}
		if(teamWhereBeaconWasPlaced.getId() != attacker.getTeamId()) {
			placer.sendMessage(StringFormatter.Error("Du musst den Beacon an den Beacon von deinem Team plazieren, nicht an den Beacon von " + teamWhereBeaconWasPlaced.getColor() + "Team " + teamWhereBeaconWasPlaced.getId()));
			return;
		}		
		BeaconRaid beaconRaid = getBeaconRaid(placer.getName(), teamWhereBeaconWasPlaced);
		if(beaconRaid == null) {			
			placer.sendMessage(StringFormatter.Error("Dein Team hat keinen laufenden Beaconraubzug, du solltest keinen Beacon haben"));
			BeaconManager.removeOneBeaconFromInventory(placer);
			return;
		}
		addLostDefense(beaconRaid.getDefender().getId());
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
	
	private Integer getAmountOfLostDefenses(Integer teamId) {
		Integer amountOfLostDefenses = amountOfLostDefensesPerTeams.get(teamId);
		if(amountOfLostDefenses == null) {
			amountOfLostDefensesPerTeams.put(teamId, 0);
			return 0;
		}
		return amountOfLostDefenses;
	}
	
	private void addLostDefense(Integer teamId) {
		Integer amountOfLostDefenses = getAmountOfLostDefenses(teamId);
		amountOfLostDefensesPerTeams.put(teamId, amountOfLostDefenses + 1);
	}

	public void removeBeaconRaidsByDestructor(Player leaver) {
		BeaconRaid[] localBeaconRaids = beaconRaids.toArray(new BeaconRaid[beaconRaids.size()]);
		for (BeaconRaid beaconRaid : localBeaconRaids) {
			if(beaconRaid.getDestructorName().equalsIgnoreCase(leaver.getName())) {
				beaconRaid.doAttackPreventedActions();
			}
		}
	}
}
