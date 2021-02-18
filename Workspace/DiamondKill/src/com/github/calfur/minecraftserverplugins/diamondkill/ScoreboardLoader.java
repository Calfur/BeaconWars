package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;


public class ScoreboardLoader {
	private Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	private TopKiller topKiller = new TopKiller("###", 1);
	private ArrayList<Attack> attacks = new ArrayList<Attack>();
	
	public void setTopKiller(TopKiller topKiller) {
		this.topKiller = topKiller;
		LoadSideBarScoreboard();
	}
	
	public ScoreboardLoader() {
		LoadSideBarScoreboard();
	}
	
	private void LoadSideBarScoreboard() {
		Objective deletableObjective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
		if(deletableObjective != null) {			
			deletableObjective.unregister();
		}
		
		Objective objective = scoreboard.registerNewObjective("Title", "dummy", ChatColor.BOLD + "Beacon wars");	
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score score9 = objective.getScore("Höchste K/D" + ChatColor.GRAY + ":");
		score9.setScore(9);
		Score score8 = objective.getScore(topKillerScoreText());
		score8.setScore(8);
		Score score7 = objective.getScore("       ");
		score7.setScore(7);
		Score score6 = objective.getScore("Aktuelle Angriffe" + ChatColor.GRAY + ":");
		score6.setScore(6);
		Score score5 = objective.getScore(attackScoreText(0));
		score5.setScore(5);
		Score score4 = objective.getScore(attackScoreText(1));
		score4.setScore(4);
		Score score3 = objective.getScore(attackScoreText(2));
		score3.setScore(3);
		Score score2 = objective.getScore("  ");
		score2.setScore(2);
		Score score1 = objective.getScore(" ");
		score1.setScore(1);
		Score score0 = objective.getScore("");
		score0.setScore(0);		
	}

	private String topKillerScoreText() {
		return topKiller.getName() + " " + ChatColor.AQUA + topKiller.getDiamondValue() + " Dias";
	}
	
	private String attackScoreText(int position) {
		if(attacks.size() > position) {
			Attack attack = attacks.get(position);
			Team attacker = attack.getAttacker();
			Team defender = attack.getDefender();
			return attacker.getColor() + "Team " + attacker.getNumber() + defender.getColor() + " -> " + defender.getColor() + "Team 2";
		}
		String result = "      ";
		result = result.substring(0, 5 - position);
		return result;
	}
}
