package com.github.calfur.beaconWars.configuration;

public class DefaultConfiguration implements IConfiguration{
	/***
	 * The title of the scoreboard
	 */
	private final String scoreboardTitle = "Beacon Wars";
	/***
	 * The total reward which the teams with the best beacon defense get.
	 * Example 1: During a beaconfight team 1 has stolen the beacon of team 2.
	 * If the total reward is set to 12, team 1 and team 3 will get 6, team 2 will get 0.
	 * 
	 * Example 2: During a beaconfight team 1 has stolen the beacon of team 2 and team 2 has stolen the beacon of team 1.
	 * If the total reward is set to 12, team 3 will get 12, team 1 and team 2 will get 0.
	 */
	private final int rewardBeaconDefenseTotalDiamonds = 0;
	private final int rewardBeaconDefenseTotalPoints = 1000;
	/***
	 * The reward that the player who steals and retrieves a beacon gets
	 */
	private final int rewardBeaconRaidSuccessDiamonds = 7;
	private final int rewardBeaconRaidSuccessPoints = 1000;
	/***
	 * The multiplicator of the bounty reward.
	 * Example 1: When a player has a bounty of 2 and gets killed, the killer gets 2x the set reward.
	 * If the reward is set to 5, the player will get 10
	 * 
	 * Example 2: When a player has a bounty of 3 and gets killed, the killer gets 3x the set reward.
	 * If the reward is set to 0, the player will get 0
	 */
	private final int rewardKillBountyMultiplicatorDiamonds = 1;
	private final int rewardKillBountyMultiplicatorPoints = 50;

	@Override
	public String getScoreboardTitle() {
		return scoreboardTitle;
	}
	
	@Override
	public Integer getRewardBeaconDefenseTotalDiamonds() {
		return rewardBeaconDefenseTotalDiamonds;
	}

	@Override
	public Integer getRewardBeaconDefenseTotalPoints() {
		return rewardBeaconDefenseTotalPoints;
	}

	@Override
	public Integer getRewardBeaconRaidSuccessDiamonds() {
		return rewardBeaconRaidSuccessDiamonds;
	}

	@Override
	public Integer getRewardBeaconRaidSuccessPoints() {
		return rewardBeaconRaidSuccessPoints;
	}

	@Override
	public Integer getRewardKillBountyMultiplicatorDiamonds() {
		return rewardKillBountyMultiplicatorDiamonds;
	}

	@Override
	public Integer getRewardKillBountyMultiplicatorPoints() {
		return rewardKillBountyMultiplicatorPoints;
	}
	
}
