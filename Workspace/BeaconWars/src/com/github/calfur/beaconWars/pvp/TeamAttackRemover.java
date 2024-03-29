package com.github.calfur.beaconWars.pvp;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.ScoreboardLoader;

public class TeamAttackRemover implements Runnable {
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();

	private Attack attack;
	private TeamAttackManager teamAttackManager;
	
	public TeamAttackRemover(Attack attack, TeamAttackManager teamAttackManager) {
		this.attack = attack;
		this.teamAttackManager = teamAttackManager;
	}
	
	@Override
	public void run() {
		scoreboardLoader.removeAttack(attack);
		teamAttackManager.removeActiveAttack(attack);
	}

}
