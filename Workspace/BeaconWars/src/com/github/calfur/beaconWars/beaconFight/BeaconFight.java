package com.github.calfur.beaconWars.beaconFight;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.PlayerModeManager;
import com.github.calfur.beaconWars.Reward;
import com.github.calfur.beaconWars.RewardManager;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.customTasks.TaskScheduler;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;
import com.github.calfur.beaconWars.pvp.DeathBanPluginInteraction;
import com.github.calfur.beaconWars.pvp.Team;

public class BeaconFight {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private RewardManager rewardManager = Main.getInstance().getRewardManager();
	
	private LocalDateTime startTime;
	private BeaconFightManager manager;
	private long eventDurationInMinutes;
	private int attackDurationInMinutes;
	private List<BeaconRaid> beaconRaids = new ArrayList<BeaconRaid>();
	private HashMap<Integer, Integer> amountOfLostDefensesPerTeams = new HashMap<Integer, Integer>();
	private int naturallyEventEndTaskId;
	private int eventStartTaskId;
	private List<UUID> teleportedPlayers = new ArrayList<UUID>();

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
		
		long secondsTillStart = ChronoUnit.SECONDS.between(LocalDateTime.now(), startTime);
		if(secondsTillStart > 0) {
			eventStartTaskId = TaskScheduler.getInstance().scheduleDelayedTask(Main.getInstance(),
					new Runnable() {
				
						@Override
						public void run() {
							startBeaconFightEvent();
						}
				
					}, 
					startTime);
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
		
		beaconRaids.add(new BeaconRaid(attackerTeam, defenderTeam, player, beaconLocation, attackDurationInMinutes, this));
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
		addLostDefense(beaconRaid.getDefenderTeam().getId());
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
		DeathBanPluginInteraction.tryChangeBanDuration(2);
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
				startTime.plusMinutes(eventDurationInMinutes));
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
	
	private void stopBeaconFightNaturally() {
		HashMap<Integer, Reward> defenseRewardPerTeams = calculateDefenseRewardPerTeams();
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
	
	private void payDefenderBounty(HashMap<Integer, Reward> defenseRewardPerTeams) {
		for (Entry<Integer, Reward> defenseReward : defenseRewardPerTeams.entrySet()) {
			int teamId = defenseReward.getKey();
			Reward reward = defenseReward.getValue();
			TeamJson teamJson = teamDbConnection.getTeam(teamId);
			String teamLeaderName = teamJson.getTeamLeader();
			if(playerDbConnection.existsPlayer(teamLeaderName)) {						
				rewardManager.addReward(
					teamLeaderName, 
					reward,
					"Verteidigerbonus nach dem Beaconevent"
				);
			}else {
				Bukkit.broadcastMessage(StringFormatter.error("Der Teamleader " + teamLeaderName + " von ") + teamJson.getColor() + "Team " + teamId + StringFormatter.error(" wurde noch nicht registriert"));
			}
		}
	}

	private void sendEventStartMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent startet jetzt!");
		Bukkit.broadcastMessage("In den n�chsten " + eventDurationInMinutes + "min k�nnen eure Beacons geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");		
		Bukkit.broadcastMessage(" ");
	}
	
	private void sendEventCancelMessage() {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent wurde " + ChatColor.DARK_RED + "abgebrochen");
		Bukkit.broadcastMessage("Ab sofort k�nnen keine Beacons mehr geklaut werden");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");	
		Bukkit.broadcastMessage(" ");	
	}

	private void sendEventDeactivatedMessage(HashMap<Integer, Reward> rewardsPerTeam) {
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.BOLD + "Der Beaconevent wurde beendet");
		Bukkit.broadcastMessage("Ab sofort k�nnen keine Beacons mehr geklaut werden");
		Bukkit.broadcastMessage("Folgende Belohnungen werden an die Teamleader ausgezahlt:");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage("Team | Verlorene Verteidigungen | Belohnung");
		for (Entry<String, TeamJson> team : teamDbConnection.getTeams().entrySet()) {
			Integer teamId = Integer.parseInt(team.getKey());
			Integer amountOfLostDefenses = getAmountOfLostDefenses(teamId);
			Reward defenseReward = rewardsPerTeam.get(teamId);
			Bukkit.broadcastMessage(team.getValue().getColor() + "" + teamId + ChatColor.RESET + "      | " + amountOfLostDefenses + "                               | " + StringFormatter.diaWord(defenseReward.getDiamonds()));
		}
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");	
		Bukkit.broadcastMessage(" ");
	}
	
	private HashMap<Integer, Reward> calculateDefenseRewardPerTeams() {
		HashMap<Integer, Reward> rewardsPerTeams = getZeroRewardsPerTeams();
		
		HashMap<Integer, Integer> teamsWithLowestAmountOfLostDefenses = getTeamsWithLowestAmountOfLostDefenses(rewardsPerTeams.keySet());
		
		int diamondReward = Math.round(configuration.getRewardBeaconDefenseTotalDiamonds() / teamsWithLowestAmountOfLostDefenses.size());
		int pointReward = Math.round(configuration.getRewardBeaconDefenseTotalPoints() / teamsWithLowestAmountOfLostDefenses.size());
		
		for (Entry<Integer, Integer> teamWithLowestAmountOfLostDefenses : teamsWithLowestAmountOfLostDefenses.entrySet()) {
			rewardsPerTeams.put(teamWithLowestAmountOfLostDefenses.getKey(), new Reward(diamondReward, pointReward));
		}
		
		return rewardsPerTeams;
	}

	private HashMap<Integer, Reward> getZeroRewardsPerTeams() {
		HashMap<Integer, Reward> result = new HashMap<Integer, Reward>();
		for (Entry<String, TeamJson> team : teamDbConnection.getTeams().entrySet()) {
			result.put(Integer.parseInt(team.getKey()), new Reward(0, 0));
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
}