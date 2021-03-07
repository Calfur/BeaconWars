package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringEditor;


public class ScoreboardLoader {
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();

	private Scoreboard defaultScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	private TopKiller topKiller = new TopKiller("###", 1);
	private ArrayList<Attack> attacks = new ArrayList<Attack>();
	
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
		reloadTabList(player);
		reloadPlayerChatName(player);
	}
	
	private void reloadSideBarScoreboard() { // same for all Players
		Objective deletableObjective = defaultScoreboard.getObjective(DisplaySlot.SIDEBAR);
		if(deletableObjective != null) {			
			deletableObjective.unregister();
		}
		
		Objective objective = defaultScoreboard.registerNewObjective("Sidebar", "dummy", ChatColor.BOLD + "Beacon wars");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

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
			
			listName = teamColor + name + " " + ChatColor.AQUA + bounty + " Dias";
			
			player.setPlayerListFooter(ChatColor.RESET + "Dein Guthaben: " + ChatColor.AQUA + "" + playerDbConnection.getPlayer(player.getName()).getCollectableDiamonds() + " Dias");
		}else {
			listName = ChatColor.DARK_RED + name + " UNREGISTRIERT";
		}
		player.setPlayerListName(listName);
	}
	
	private void reloadPlayerChatName(Player player) {
		String name = player.getName();
		String listName;
		if(playerDbConnection.existsPlayer(name)) {			
			int teamId = playerDbConnection.getPlayer(name).getTeamId();
			ChatColor teamColor = teamDbConnection.getTeam(teamId).getColor();
			listName = teamColor + name + ChatColor.RESET;
		}else {
			listName = ChatColor.DARK_RED + "UNREGISTRIERT " + name + ChatColor.RESET;
		}
		player.setDisplayName(listName);		
	}

	private String topKillerScoreText() {
		if(!topKiller.areMultipleTopKiller()) {			
			return StringEditor.FirstLetterToUpper(topKiller.getName()) + " " + ChatColor.AQUA + topKiller.getDiamondValue() + " Dias";
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
		return StringEditor.RepeatString(" ", scoreNumber);
	}


}
