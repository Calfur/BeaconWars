package com.github.calfur.beaconWars.pvp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.customTasks.TaskScheduler;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class TeamAttackManager {
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	private static final int attackDurationSeconds = 60*3;
	
	private HashMap<Integer, Attack> activeAttacks = new HashMap<Integer, Attack>();
	
	public void removeActiveAttack(Attack attack) {
		Entry<Integer, Attack> activeAttack = getActiveAttackIfExists(attack.getAttacker().getId(), attack.getDefender().getId());
		if(activeAttack != null) {
			activeAttacks.remove(activeAttack.getKey());
		}else {
			Bukkit.broadcastMessage(StringFormatter.error("ERROR: removeActiveAttack() -> Attack could not be founded to remove"));
		}
	}
	
	public void registrateHit(int attacker, int defender) {	
		Entry<Integer, Attack> activeAttack = getActiveAttackIfExists(attacker, defender);
		Attack attack;
		if(activeAttack != null) {	// attack started already, don't update the scoreboard
			attack = activeAttack.getValue();
			int taskId = activeAttack.getKey();
			activeAttacks.remove(taskId);
			TaskScheduler.getInstance().cancelTask(taskId);
		}else {	// attack is new, update the scoreboard
			ChatColor attackerColor = teamDbConnection.getTeam(attacker).getColor();
			ChatColor defenderColor = teamDbConnection.getTeam(defender).getColor();
			attack = new Attack(new Team(attacker, attackerColor), new Team(defender, defenderColor));
			Main.getInstance().getScoreboardLoader().addAttack(attack);
		}		
		int taskId = TaskScheduler.getInstance().scheduleDelayedTask(Main.getInstance(), 
				new TeamAttackRemover(attack, this), 
				LocalDateTime.now().plusSeconds(attackDurationSeconds));
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
	
	public boolean isTeamFighting(int teamId) {
		for (Entry<Integer, Attack> activeAttack : activeAttacks.entrySet()) {
			int attackerId = activeAttack.getValue().getAttacker().getId();
			int defenderId =  activeAttack.getValue().getDefender().getId();
			if(attackerId == teamId || defenderId == teamId) {
				return true;
			}
		}
		return false;
	}
}
