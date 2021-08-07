package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.BeaconFight;
import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.BeaconFightManager;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;


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

		BeaconFight ongoingBeaconFight = beaconFightManager.getOngoingBeaconFight();
		
		if(ongoingBeaconFight != null) {
			Score score11 = objective.getScore("Beaconevent" + ChatColor.GRAY + ":");
			score11.setScore(11);
			Score score10 = objective.getScore("Ende" + ChatColor.GRAY + ": " + ChatColor.RESET + ChatColor.BOLD + ongoingBeaconFight.getEndTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")));
			score10.setScore(10);
			Score score9 = objective.getScore("         ");
			score9.setScore(9);
		}else {
			BeaconFight nextWaitingBeaconFight = beaconFightManager.getNextWaitingBeaconFight();
			if(nextWaitingBeaconFight != null) {				
				Score score11 = objective.getScore("Beaconevent" + ChatColor.GRAY + ":");
				score11.setScore(11);
				Score score10 = objective.getScore("Start" + ChatColor.GRAY + ": " + ChatColor.RESET + ChatColor.BOLD + nextWaitingBeaconFight.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM HH:mm")));
				score10.setScore(10);
				Score score9 = objective.getScore("         ");
				score9.setScore(9);
			}
		}
		
		Score score8 = objective.getScore("Höchste K/D" + ChatColor.GRAY + ":");
		score8.setScore(8);
		Score score7 = objective.getScore(topKillerScoreText());
		score7.setScore(7);
		Score score6 = objective.getScore("      ");
		score6.setScore(6);
		Score score5 = objective.getScore("Aktuelle Angriffe" + ChatColor.GRAY + ":");
		score5.setScore(5);
		Score score4 = objective.getScore(attackScoreText(0, 4));
		score4.setScore(4);
		Score score3 = objective.getScore(attackScoreText(1, 3));
		score3.setScore(3);
		Score score2 = objective.getScore(attackScoreText(2, 2));
		score2.setScore(2);
		Score score1 = objective.getScore(attackScoreText(3, 1));
		score1.setScore(1);
		Score score0 = objective.getScore(attackScoreText(4, 0));
		score0.setScore(0);			
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
			listName = StringFormatter.Error(name + " UNREGISTRIERT");
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
			listName = StringFormatter.Error("UNREGISTRIERT " + name);
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
			return StringFormatter.FirstLetterToUpper(topKiller.getName()) + " " + ChatColor.AQUA + topKiller.getDiamondValue() + " Dias";
		}else {
			return "-";
		}
	}
	
	private String attackScoreText(int position, int scoreNumber) {
		if(attacks.size() > position) {
			Attack attack = attacks.get(position);
			Team attacker = attack.getAttacker();
			Team defender = attack.getDefender();
			return (attacker.getColor() + "Team " + attacker.getId()) + (ChatColor.RESET + " -> ") + (defender.getColor() + "Team " + defender.getId());
		}
		return StringFormatter.RepeatString(" ", scoreNumber);
	}
}
