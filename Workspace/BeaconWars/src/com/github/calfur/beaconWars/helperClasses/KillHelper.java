package com.github.calfur.beaconWars.helperClasses;

import java.time.LocalDateTime;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.Reward;
import com.github.calfur.beaconWars.RewardManager;
import com.github.calfur.beaconWars.ScoreboardLoader;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.database.KillDbConnection;
import com.github.calfur.beaconWars.database.KillJson;
import com.github.calfur.beaconWars.pvp.TopKiller;

public class KillHelper {

	public static Reward addKill(String killer, String victim, String message, Main main) {
		KillDbConnection killDbConnection = main.getKillDbConnection();
		RewardManager rewardManager = main.getRewardManager();
		IConfiguration configuration = main.getConfiguration();
		ScoreboardLoader scoreboardLoader = main.getScoreboardLoader();
		
		int bounty = killDbConnection.getBounty(victim);
		Reward reward = new Reward(
			bounty * configuration.getRewardKillBountyMultiplicatorDiamonds(), 
			bounty * configuration.getRewardKillBountyMultiplicatorPoints()
		);
				
		rewardManager.addReward(
			killer,
			reward, 
			message
		);
		
		killDbConnection.addKill(new KillJson(killer, victim, LocalDateTime.now()));
		scoreboardLoader.setTopKiller(TopKiller.getCurrentTopKiller());
		
		return reward;
	}
}
