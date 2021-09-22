package com.github.calfur.beaconWars.configuration;

public class DynamicConfiguration implements IConfiguration {
	private String scoreboardTitle;
	private Integer rewardBeaconDefenseTotalDiamonds;
	private Integer rewardBeaconDefenseTotalPoints;
	
	public DynamicConfiguration(
			String scoreboardTitle,
			Integer rewardBestBeaconDefenseDiamonds,
			Integer rewardBestBeaconDefensePoints
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
	public Integer getRewardBeaconDefenseTotalDiamonds() {
		return rewardBeaconDefenseTotalDiamonds;
	}

	@Override
	public Integer getRewardBeaconDefenseTotalPoints() {
		return rewardBeaconDefenseTotalPoints;
	}
	
}
