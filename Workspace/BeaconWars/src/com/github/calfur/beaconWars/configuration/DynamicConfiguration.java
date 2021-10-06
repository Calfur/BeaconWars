package com.github.calfur.beaconWars.configuration;

public class DynamicConfiguration implements IConfiguration {
	private String scoreboardTitle;
	private Integer rewardBeaconDefenseTotalDiamonds;
	private Integer rewardBeaconDefenseTotalPoints;
	private Integer rewardBeaconRaidSuccessDiamonds;
	private Integer rewardBeaconRaidSuccessPoints;
	private Integer rewardKillBountyMultiplicatorDiamonds;
	private Integer rewardKillBountyMultiplicatorPoints;
	private Boolean areHungerGamesEnabled;

	public DynamicConfiguration(
			String scoreboardTitle,
			Integer rewardBestBeaconDefenseDiamonds,
			Integer rewardBestBeaconDefensePoints,
			Integer rewardBeaconRaidSuccessDiamonds,
			Integer rewardBeaconRaidSuccessPoints,
			Integer rewardKillBountyMultiplicatorDiamonds,
			Integer rewardKillBountyMultiplicatorPoints,
			Boolean areHungerGamesEnabled
	){
		this.scoreboardTitle = scoreboardTitle;
		this.rewardBeaconDefenseTotalDiamonds = rewardBestBeaconDefenseDiamonds;
		this.rewardBeaconDefenseTotalPoints = rewardBestBeaconDefensePoints;
		this.rewardBeaconRaidSuccessDiamonds = rewardBeaconRaidSuccessDiamonds;
		this.rewardBeaconRaidSuccessPoints = rewardBeaconRaidSuccessPoints;
		this.rewardKillBountyMultiplicatorDiamonds = rewardKillBountyMultiplicatorDiamonds;
		this.rewardKillBountyMultiplicatorPoints = rewardKillBountyMultiplicatorPoints;
		this.areHungerGamesEnabled = areHungerGamesEnabled;
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

	@Override
	public Integer getRewardKillBountyMultiplicatorDiamonds() {
		return rewardKillBountyMultiplicatorDiamonds;
	}

	@Override
	public Integer getRewardKillBountyMultiplicatorPoints() {
		return rewardKillBountyMultiplicatorPoints;
	}
	
	@Override
	public Boolean areHungerGamesEnabled() {
		return areHungerGamesEnabled;
	}
}
