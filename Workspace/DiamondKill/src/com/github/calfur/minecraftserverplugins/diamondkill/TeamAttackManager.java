package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;

public class TeamAttackManager {
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	
	private static final int attackDurationSeconds = 60*3;
	
	private HashMap<Integer, Attack> activeAttacks = new HashMap<Integer, Attack>();
	
	public void removeActiveAttack(Attack attack) {
		Entry<Integer, Attack> activeAttack = getActiveAttackIfExists(attack.getAttacker().getId(), attack.getDefender().getId());
		if(activeAttack != null) {
			activeAttacks.remove(activeAttack.getKey());
		}else {
			Bukkit.broadcastMessage(ChatColor.RED + "ERROR: removeActiveAttack() -> Attack could not be founded to remove");
		}
	}
	
	public void registrateHit(int attacker, int defender) {	
		Entry<Integer, Attack> activeAttack = getActiveAttackIfExists(attacker, defender);
		Attack attack;
		if(activeAttack != null) {	// attack started already, don't update the scoreboard
			attack = activeAttack.getValue();
			int taskId = activeAttack.getKey();
			activeAttacks.remove(taskId);
			Bukkit.getScheduler().cancelTask(taskId);
		}else {	// attack is new, update the scoreboard
			ChatColor attackerColor = teamDbConnection.getTeam(attacker).getColor();
			ChatColor defenderColor = teamDbConnection.getTeam(defender).getColor();
			attack = new Attack(new Team(attacker, attackerColor), new Team(defender, defenderColor));
			scoreboardLoader.addAttack(attack);
		}		
		int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new TeamAttackRemover(attack, this), attackDurationSeconds*20);
		activeAttacks.put(taskId, attack);
	}
	/**
	 * 
	 * @param team
	 * @param anotherTeam
	 * @return First attack which includes both teams, not relevant if attacker or defender
	 */
	private Entry<Integer, Attack> getActiveAttackIfExists(int team, int anotherTeam){
		for (Entry<Integer, Attack> activeAttack : activeAttacks.entrySet()) {
			int attackerId = activeAttack.getValue().getAttacker().getId();
			int defenderId =  activeAttack.getValue().getDefender().getId();
			if((attackerId == team && defenderId == anotherTeam) || attackerId == anotherTeam && defenderId == team	) {
				return activeAttack;
			}
		}
		return null;
	}
}
