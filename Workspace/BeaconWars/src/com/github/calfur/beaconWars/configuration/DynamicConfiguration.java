package com.github.calfur.beaconWars.configuration;

public class DynamicConfiguration implements IConfiguration {
	private String scoreboardTitle;
	private int rewardBeaconDefenseTotalDiamonds;
	private int rewardBeaconDefenseTotalPoints;
	
	public DynamicConfiguration(
			String scoreboardTitle,
			int rewardBestBeaconDefenseDiamonds,
			int rewardBestBeaconDefensePoints
	){
		this.scoreboardTitle = scoreboardTitle;
		this.rewardBeaconDefenseTotalDiamonds = rewardBestBeaconDefenseDiamonds;
		this.rewardBeaconDefenseTotalPoints = rewardBestBeaconDefensePoints;
	}

	@Override
	public String getScoreboardTitle() {
		return scoreboardTitle;
	}

	@Override
	public int getRewardBeaconDefenseTotalDiamonds() {
		return rewardBeaconDefenseTotalDiamonds;
	}

	@Override
	public int getRewardBeaconDefenseTotalPoints() {
		return rewardBeaconDefenseTotalPoints;
	}
	
}
