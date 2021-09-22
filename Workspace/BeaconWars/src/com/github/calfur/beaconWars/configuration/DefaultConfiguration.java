package com.github.calfur.beaconWars.configuration;

public class DefaultConfiguration implements IConfiguration{
	private final String scoreboardTitle = "Beacon Wars";
	private final int rewardBeaconDefenseTotalDiamonds = 0;
	private final int rewardBeaconDefenseTotalPoints = 0;

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
	
}
