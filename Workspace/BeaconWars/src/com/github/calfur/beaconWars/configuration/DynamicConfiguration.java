package com.github.calfur.beaconWars.configuration;

public class DynamicConfiguration implements IConfiguration {
	private String scoreboardTitle;
	private Integer rewardBeaconDefenseTotalDiamonds;
	private Integer rewardBeaconDefenseTotalPoints;
	private Integer rewardBeaconRaidSuccessDiamonds;
	private Integer rewardBeaconRaidSuccessPoints;
	
	public DynamicConfiguration(
			String scoreboardTitle,
			Integer rewardBestBeaconDefenseDiamonds,
			Integer rewardBestBeaconDefensePoints,
			Integer rewardBeaconRaidSuccessDiamonds,
			Integer rewardBeaconRaidSuccessPoints
	){
		this.scoreboardTitle = scoreboardTitle;
		this.rewardBeaconDefenseTotalDiamonds = rewardBestBeaconDefenseDiamonds;
		this.rewardBeaconDefenseTotalPoints = rewardBestBeaconDefensePoints;
		this.rewardBeaconRaidSuccessDiamonds = rewardBeaconRaidSuccessDiamonds;
		this.rewardBeaconRaidSuccessPoints = rewardBeaconRaidSuccessPoints;
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

	@Override
	public Integer getRewardBeaconRaidSuccessDiamonds() {
		return rewardBeaconRaidSuccessDiamonds;
	}

	@Override
	public Integer getRewardBeaconRaidSuccessPoints() {
		return rewardBeaconRaidSuccessPoints;
	}
	
}
