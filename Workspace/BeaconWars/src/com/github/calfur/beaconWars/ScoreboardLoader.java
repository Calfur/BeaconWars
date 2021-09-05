package com.github.calfur.beaconWars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.github.calfur.beaconWars.beaconFight.BeaconFight;
import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.database.KillDbConnection;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.helperClasses.ScoreboardHelper;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;


public class ScoreboardLoader {
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();

	private Scoreboard defaultScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	private TopKiller topKiller;
	private ArrayList<Attack> attacks = new ArrayList<Attack>();
	private BossBarManager bossBarManager = new BossBarManager();
	private static final boolean displayTeamPoints = true;
	
	public void setTopKiller(TopKiller topKiller) {
		this.topKiller = topKiller;
		reloadScoreboardForAllOnlinePlayers();
	}

	public void addAttack(Attack attack) {
		attacks.add(attack);		
		reloadScoreboardForAllOnlinePlayers();
	}
	
	public void removeAttack(Attack attack) {
		attacks.remove(attack);		
		reloadScoreboardForAllOnlinePlayers();
	}
	
	public ScoreboardLoader() {
		setTopKiller(TopKiller.getCurrentTopKiller());
		reloadScoreboardForAllOnlinePlayers();
	}
	
	public void reloadScoreboardForAllOnlinePlayers() {
		reloadSideBarScoreboard();
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for (Player player : onlinePlayers) {
			reloadScoreboardFor(player);
		}
	}
	
	public void reloadScoreboardFor(Player player) {
		playerModeManager.updateHighlight(player, topKiller);
		reloadTabList(player);
		reloadPlayerChatName(player);
		reloadNameAbovePlayer(player);
		bossBarManager.displayAllBossBarsTo(player);
	}

	public void addBossBar(String name, ChatColor chatColor, LocalDateTime countdownEnd) {
		CustomBossBar bossBar = bossBarManager.addBossBar(name, chatColor, countdownEnd);
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for (Player player : onlinePlayers) {
			bossBar.addPlayer(player);
		}
	}
	
	public void removeBossBar(String name) {
		bossBarManager.removeBossBar(name);
	}

	private void reloadSideBarScoreboard() { // same for all Players
		Objective deletableObjective = defaultScoreboard.getObjective(DisplaySlot.SIDEBAR);
		if(deletableObjective != null) {			
			deletableObjective.unregister();
		}
		
		Objective objective = defaultScoreboard.registerNewObjective("Sidebar", "dummy", ChatColor.BOLD + "Beacon wars");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		ArrayList<String> scoreTexts = new ArrayList<String>();
		
		// Beacon fight if existing
		BeaconFight ongoingBeaconFight = beaconFightManager.getOngoingBeaconFight();
		if(ongoingBeaconFight != null) {
			scoreTexts.add("Beaconevent");
			scoreTexts.add("Ende" + ChatColor.GRAY + ": " + ChatColor.RESET + ChatColor.BOLD + ongoingBeaconFight.getEndTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")));
			scoreTexts.add("");
		}else {
			BeaconFight nextWaitingBeaconFight = beaconFightManager.getNextWaitingBeaconFight();
			if(nextWaitingBeaconFight != null) {				
				scoreTexts.add("Beaconevent");
				scoreTexts.add("Start" + ChatColor.GRAY + ": " + ChatColor.RESET + ChatColor.BOLD + nextWaitingBeaconFight.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")));
				scoreTexts.add("");
			}
		}

		// Team points
		if(displayTeamPoints) {			
			scoreTexts.add("Punktestand");
			scoreTexts.add(getTeamPointsText(0));
			scoreTexts.add(getTeamPointsText(1));
			scoreTexts.add(getTeamPointsText(2));
			scoreTexts.add("");
		}else {
			scoreTexts.add("");
			scoreTexts.add("");
			scoreTexts.add("");
			scoreTexts.add("");
			scoreTexts.add("");
		}
		
		// TopKiller
		scoreTexts.add("Höchste K/D");
		scoreTexts.add(topKillerScoreText());
		scoreTexts.add("");
		
		// Current attacks
		scoreTexts.add("Aktuelle Angriffe");
		scoreTexts.add(attackScoreText(0));
		scoreTexts.add(attackScoreText(1));
		scoreTexts.add(attackScoreText(2));
		
		ScoreboardHelper.addScoreRows(objective, scoreTexts);
	}

	
	private void reloadTabList(Player player) {
		String name = player.getName();
		String listName;
		if(playerDbConnection.existsPlayer(name)) {			
			int teamId = playerDbConnection.getPlayer(name).getTeamId();
			ChatColor teamColor = teamDbConnection.getTeam(teamId).getColor();
			int bounty = killDbConnection.getBounty(name);		
			
			if(teamId != TeamDbConnection.spectatorTeamNumber) {				
				listName = teamColor + name + getActiveModes(player) + " " + ChatColor.AQUA + bounty + " Dias";
				player.setPlayerListFooter(ChatColor.RESET + "Dein Guthaben: " + ChatColor.AQUA + "" + playerDbConnection.getPlayer(player.getName()).getCollectableDiamonds() + " Dias");
			}else {
				listName = teamColor + name + " [Spec]";
				player.setPlayerListFooter(ChatColor.RESET + "Du bist im Spectator Team und spielst nicht mit");
			}
		}else {
			listName = StringFormatter.error(name + " UNREGISTRIERT");
		}
		player.setPlayerListName(listName);
	}
	
	private void reloadPlayerChatName(Player player) {
		String name = player.getName();
		String listName;
		if(playerDbConnection.existsPlayer(name)) {			
			int teamId = playerDbConnection.getPlayer(name).getTeamId();
			ChatColor teamColor = teamDbConnection.getTeam(teamId).getColor();			
			listName = teamColor + name + getActiveModes(player) + ChatColor.RESET;
		}else {
			listName = StringFormatter.error("UNREGISTRIERT " + name);
		}
		player.setDisplayName(listName);		
	}

	private void reloadNameAbovePlayer(Player player) {
		String name = player.getName();
		if(playerDbConnection.existsPlayer(name)) {				
			int amountOfLetters = name.length();
			if(amountOfLetters > 12) {
				amountOfLetters = 12;
			}
			String scoreboardTeamName = name.substring(0, amountOfLetters) + "TM";
			org.bukkit.scoreboard.Team team = defaultScoreboard.getTeam(scoreboardTeamName);
			if(team == null) {			
				team = defaultScoreboard.registerNewTeam(scoreboardTeamName);
			}
			int teamId = playerDbConnection.getPlayer(name).getTeamId();
			ChatColor teamColor = teamDbConnection.getTeam(teamId).getColor();
			team.setColor(teamColor);
			team.setSuffix(getActiveModes(player));
			
			team.addEntry(player.getName());
		}
	}
	
	private String getActiveModes(Player player) {
		if(playerModeManager.isPlayerInBuildMode(player)) {				
			return ChatColor.GOLD + " [Baut]";
		}else {
			return "";
		}
	}

	private String topKillerScoreText() {
		if(!topKiller.areMultipleTopKiller()) {
			PlayerJson playerJson = playerDbConnection.getPlayer(topKiller.getName());
			ChatColor teamColor = ChatColor.RESET;
			if(playerJson != null) {				
				teamColor = teamDbConnection.getTeam(playerJson.getTeamId()).getColor();
			}
			return teamColor + StringFormatter.firstLetterToUpper(topKiller.getName()) + ChatColor.GRAY + ": " + StringFormatter.diaWord(topKiller.getDiamondValue());
		}else {
			return "-";
		}
	}
	
	private String attackScoreText(int position) {
		if(attacks.size() > position) {
			Attack attack = attacks.get(position);
			Team attacker = attack.getAttacker();
			Team defender = attack.getDefender();
			return (attacker.getColor() + "Team " + attacker.getId()) + (ChatColor.RESET + " -> ") + (defender.getColor() + "Team " + defender.getId());
		}
		return "";
	}
	
	private String getTeamPointsText(int position) {
		HashMap<String, TeamJson> teams = teamDbConnection.getTeams();
		if(teams.size() > position) {
			ArrayList<String> teamIds = new ArrayList<>();
			teamIds.addAll(teams.keySet());
			teamIds.sort(new Comparator<String>() {

				@Override
				public int compare(String firstTeamId, String secondTeamId) {
					TeamJson firstTeamJson = teams.get(firstTeamId);
					TeamJson secondTeamJson = teams.get(secondTeamId);
					if(firstTeamJson.getPoints() < secondTeamJson.getPoints()) {
						return 1;
					}else if(firstTeamJson.getPoints() == secondTeamJson.getPoints()) {						
						return 0;
					}
					return -1;
				}
				
			});
			
			String teamId = teamIds.get(position);
			TeamJson teamJson = teams.get(teamId);
			return teamJson.getColor() + "Team " + teamId + ChatColor.GRAY + ": " + ChatColor.RESET + teamJson.getPoints();
		}
		return "";
	}
}
