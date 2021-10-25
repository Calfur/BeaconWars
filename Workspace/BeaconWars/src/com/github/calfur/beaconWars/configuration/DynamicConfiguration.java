package com.github.calfur.beaconWars.configuration;

import org.bukkit.Location;

public class DynamicConfiguration implements IConfiguration {
	private String scoreboardTitle;
	private Integer deductionBeaconRaidLostDefensePoints;
	private Integer rewardBeaconRaidSuccessDiamonds;
	private Integer rewardBeaconRaidSuccessPoints;
	private Integer rewardKillBountyMultiplicatorDiamonds;
	private Integer rewardKillBountyMultiplicatorPoints;
	private Boolean areHungerGamesEnabled;
	private Location hungerGamesLocation;
	private Integer hungerGamesMinimumAmountOfRequiredOnlineTeams;
	private Boolean areTeamPointsDisplayedInScoreboard;
	private Integer totemCooldownLengthInMinutes;
	private Integer attackDurationInSeconds;
	private Integer timeUntilDeathCountsNotAsKillInSeconds;
	private Integer buildModeCooldownInMinutes;
	private Integer buildModeBaseRangeRadiusInBlocks;
	public Integer deathBanDurationInMinutes;
	public Integer deathBanDurationDuringBeaconFightInMinutes;
	private String beaconEventWorldName;

	public DynamicConfiguration(
			String scoreboardTitle,
			Integer rewardBestBeaconDefensePoints,
			Integer rewardBeaconRaidSuccessDiamonds,
			Integer rewardBeaconRaidSuccessPoints,
			Integer rewardKillBountyMultiplicatorDiamonds,
			Integer rewardKillBountyMultiplicatorPoints,
			Boolean areHungerGamesEnabled,
			Location hungerGamesLocation,
			Integer hungerGamesMinimumAmountOfRequiredOnlineTeams,
			Boolean areTeamPointsDisplayedInScoreboard,
			Integer totemCooldownLengthInMinutes,
			Integer attackDurationInSeconds,
			Integer timeUntilDeathCountsNotAsKillInSeconds,
			Integer buildModeCooldownInMinutes,
			Integer buildModeBaseRangeRadiusInBlocks,
			Integer deathBanDurationInMinutes,
			Integer deathBanDurationDuringBeaconFightInMinutes,
			String beaconEventWorldName
	){
		this.scoreboardTitle = scoreboardTitle;
		this.deductionBeaconRaidLostDefensePoints = rewardBestBeaconDefensePoints;
		this.rewardBeaconRaidSuccessDiamonds = rewardBeaconRaidSuccessDiamonds;
		this.rewardBeaconRaidSuccessPoints = rewardBeaconRaidSuccessPoints;
		this.rewardKillBountyMultiplicatorDiamonds = rewardKillBountyMultiplicatorDiamonds;
		this.rewardKillBountyMultiplicatorPoints = rewardKillBountyMultiplicatorPoints;
		this.areHungerGamesEnabled = areHungerGamesEnabled;
		this.hungerGamesLocation = hungerGamesLocation;
		this.hungerGamesMinimumAmountOfRequiredOnlineTeams = hungerGamesMinimumAmountOfRequiredOnlineTeams;
		this.areTeamPointsDisplayedInScoreboard = areTeamPointsDisplayedInScoreboard;
		this.totemCooldownLengthInMinutes = totemCooldownLengthInMinutes;
		this.attackDurationInSeconds = attackDurationInSeconds;
		this.timeUntilDeathCountsNotAsKillInSeconds = timeUntilDeathCountsNotAsKillInSeconds;
		this.buildModeCooldownInMinutes = buildModeCooldownInMinutes;
		this.buildModeBaseRangeRadiusInBlocks = buildModeBaseRangeRadiusInBlocks;
		this.deathBanDurationInMinutes = deathBanDurationInMinutes;
		this.deathBanDurationDuringBeaconFightInMinutes = deathBanDurationDuringBeaconFightInMinutes;
		this.beaconEventWorldName = beaconEventWorldName;
	}

	@Override
	public String getScoreboardTitle() {
		return scoreboardTitle;
	}

	@Override
	public Integer getDeductionBeaconRaidLostDefensePoints() {
		return deductionBeaconRaidLostDefensePoints;
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

	@Override
	public Integer getAttackDurationInSeconds() {
		return attackDurationInSeconds;
	}

	@Override
	public Integer getTimeUntilDeathCountsNotAsKillInSeconds() {
		return timeUntilDeathCountsNotAsKillInSeconds;
	}

	@Override
	public Integer getBuildModeCooldownInMinutes() {
		return buildModeCooldownInMinutes;
	}

	@Override
	public Integer getBuildModeBaseRangeRadiusInBlocks() {
		return buildModeBaseRangeRadiusInBlocks;
	}

	@Override
	public Integer getDeathBanDurationInMinutes() {
		return deathBanDurationInMinutes;
	}

	@Override
	public Integer getDeathBanDurationDuringBeaconFightInMinutes() {
		return deathBanDurationDuringBeaconFightInMinutes;
	}

	@Override
	public String getBeaconEventWorldName() {
		return beaconEventWorldName;
	}
}
