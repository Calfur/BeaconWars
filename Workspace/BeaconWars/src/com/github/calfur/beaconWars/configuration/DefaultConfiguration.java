package com.github.calfur.beaconWars.configuration;

public class DefaultConfiguration implements IConfiguration{
	private final String scoreboardTitle = "Beacon Wars";
	private final int rewardBeaconDefenseTotalDiamonds = 0;
	private final int rewardBeaconDefenseTotalPoints = 0;
	private final int rewardBeaconRaidSuccessDiamonds = 5;
	private final int rewardBeaconRaidSuccessPoints = 0;

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
	
}
