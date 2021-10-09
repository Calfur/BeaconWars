package com.github.calfur.beaconWars.configuration;

import org.bukkit.Location;

public class DynamicConfiguration implements IConfiguration {
	private String scoreboardTitle;
	private Integer rewardBeaconDefenseTotalDiamonds;
	private Integer rewardBeaconDefenseTotalPoints;
	private Integer rewardBeaconRaidSuccessDiamonds;
	private Integer rewardBeaconRaidSuccessPoints;
	private Integer rewardKillBountyMultiplicatorDiamonds;
	private Integer rewardKillBountyMultiplicatorPoints;
	private Boolean areHungerGamesEnabled;
	private Location hungerGamesLocation;
	private Integer hungerGamesMinimumAmountOfRequiredOnlineTeams;
	private Boolean areTeamPointsDisplayedInScoreboard;
	private Integer totemCooldownLengthInMinutes;

	public DynamicConfiguration(
			String scoreboardTitle,
			Integer rewardBestBeaconDefenseDiamonds,
			Integer rewardBestBeaconDefensePoints,
			Integer rewardBeaconRaidSuccessDiamonds,
			Integer rewardBeaconRaidSuccessPoints,
			Integer rewardKillBountyMultiplicatorDiamonds,
			Integer rewardKillBountyMultiplicatorPoints,
			Boolean areHungerGamesEnabled,
			Location hungerGamesLocation,
			Integer hungerGamesMinimumAmountOfRequiredOnlineTeams,
			Boolean areTeamPointsDisplayedInScoreboard,
			Integer totemCooldownLengthInMinutes
	){
		this.scoreboardTitle = scoreboardTitle;
		this.rewardBeaconDefenseTotalDiamonds = rewardBestBeaconDefenseDiamonds;
		this.rewardBeaconDefenseTotalPoints = rewardBestBeaconDefensePoints;
		this.rewardBeaconRaidSuccessDiamonds = rewardBeaconRaidSuccessDiamonds;
		this.rewardBeaconRaidSuccessPoints = rewardBeaconRaidSuccessPoints;
		this.rewardKillBountyMultiplicatorDiamonds = rewardKillBountyMultiplicatorDiamonds;
		this.rewardKillBountyMultiplicatorPoints = rewardKillBountyMultiplicatorPoints;
		this.areHungerGamesEnabled = areHungerGamesEnabled;
		this.hungerGamesLocation = hungerGamesLocation;
		this.hungerGamesMinimumAmountOfRequiredOnlineTeams = hungerGamesMinimumAmountOfRequiredOnlineTeams;
		this.areTeamPointsDisplayedInScoreboard = areTeamPointsDisplayedInScoreboard;
		this.totemCooldownLengthInMinutes = totemCooldownLengthInMinutes;
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

	@Override
	public Location getHungerGamesLocation() {
		return hungerGamesLocation;
	}
	
	public Integer getHungerGamesMinimumAmountOfRequiredOnlineTeams() {
		return hungerGamesMinimumAmountOfRequiredOnlineTeams;
	}
	
	@Override
	public Boolean areTeamPointsDisplayedInScoreboard() {
		return areTeamPointsDisplayedInScoreboard;
	}

	@Override
	public Integer getTotemCooldownLengthInMinutes() {
		return totemCooldownLengthInMinutes;
	}
}
