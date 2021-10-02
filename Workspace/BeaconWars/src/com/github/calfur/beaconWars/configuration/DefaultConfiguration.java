package com.github.calfur.beaconWars.configuration;

public class DefaultConfiguration implements IConfiguration{
	private final String scoreboardTitle = "Beacon Wars";
	private final int rewardBeaconDefenseTotalDiamonds = 0;
	private final int rewardBeaconDefenseTotalPoints = 1000;
	private final int rewardBeaconRaidSuccessDiamonds = 7;
	private final int rewardBeaconRaidSuccessPoints = 1000;
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
