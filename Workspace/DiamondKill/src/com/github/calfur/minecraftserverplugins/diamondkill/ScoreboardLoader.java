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

import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringEditor;


public class ScoreboardLoader {
	private Scoreboard defaultScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	private TopKiller topKiller = new TopKiller("###", 1);
	private ArrayList<Attack> attacks = new ArrayList<Attack>();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	
	public void setTopKiller(TopKiller topKiller) {
		this.topKiller = topKiller;
		ReloadScoreboardForAllOnlinePlayers();
	}
	
	public ScoreboardLoader() {
		ReloadScoreboardForAllOnlinePlayers();
	}
	
	public void ReloadScoreboardForAllOnlinePlayers() {
		Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for (Player player : onlinePlayers) {
			LoadSideBarScoreboard(player);
		}		
	}
	
	public void ReloadScoreboardFor(Player player) {
		LoadSideBarScoreboard(player);
	}
	
	private void LoadSideBarScoreboard(Player player) {
		/*Objective deletableObjective = defaultScoreboard.getObjective(DisplaySlot.SIDEBAR);
		if(deletableObjective != null) {			
			deletableObjective.unregister();
		}*/
		
		Scoreboard playerScoreboard = player.getScoreboard();
		if(playerScoreboard == defaultScoreboard) {
			playerScoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
			player.setScoreboard(playerScoreboard);
		}
		Objective deletableObjective = playerScoreboard.getObjective(DisplaySlot.SIDEBAR);
		if(deletableObjective != null) {			
			deletableObjective.unregister();
		}
		
		Objective objective = playerScoreboard.registerNewObjective("Title", "dummy", ChatColor.BOLD + "Beacon wars");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if(playerDbConnection.existsPlayer(player.getName())){
			Score score11 = objective.getScore("Diamantguthaben" + ChatColor.GRAY + ":");
			score11.setScore(11);
			Score score10 = objective.getScore(ChatColor.AQUA + "" + playerDbConnection.getPlayer(player.getName()).getCollectableDiamonds() + " Dias");
			score10.setScore(10);
			Score score9 = objective.getScore("         ");
			score9.setScore(9);
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
		}else {
			Score score11 = objective.getScore("           ");
			score11.setScore(11);
			Score score10 = objective.getScore("          ");
			score10.setScore(10);
			Score score9 = objective.getScore(ChatColor.RED + "Du bist keinem");
			score9.setScore(9);
			Score score8 = objective.getScore(ChatColor.RED + "Team zugewiesen.");
			score8.setScore(8);
			Score score7 = objective.getScore(ChatColor.RED + "Bitte melde dich");
			score7.setScore(7);
			Score score6 = objective.getScore(ChatColor.RED + "bei einem");
			score6.setScore(6);
			Score score5 = objective.getScore(ChatColor.RED + "Administrator.");
			score5.setScore(5);
			Score score4 = objective.getScore("    ");
			score4.setScore(4);
			Score score3 = objective.getScore("   ");
			score3.setScore(3);
			Score score2 = objective.getScore("  ");
			score2.setScore(2);
			Score score1 = objective.getScore(" ");
			score1.setScore(1);
			Score score0 = objective.getScore("");
			score0.setScore(0);	
		}
				
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
			return attacker.getColor() + "Team " + attacker.getNumber() + defender.getColor() + " -> " + defender.getColor() + "Team 2";
		}
		return StringEditor.RepeatString(" ", scoreNumber);
	}
}
