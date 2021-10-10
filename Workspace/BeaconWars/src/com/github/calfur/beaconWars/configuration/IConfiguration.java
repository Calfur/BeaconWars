package com.github.calfur.beaconWars.configuration;

import org.bukkit.Location;

public interface IConfiguration {
	public String getScoreboardTitle();
	public Integer getDeductionBeaconRaidLostDefensePoints();
	public Integer getRewardBeaconRaidSuccessDiamonds();
	public Integer getRewardBeaconRaidSuccessPoints();
	public Integer getRewardKillBountyMultiplicatorDiamonds();
	public Integer getRewardKillBountyMultiplicatorPoints();
	public Boolean areHungerGamesEnabled();
	public Location getHungerGamesLocation();
	public Integer getHungerGamesMinimumAmountOfRequiredOnlineTeams();
	public Boolean areTeamPointsDisplayedInScoreboard();
	public Integer getTotemCooldownLengthInMinutes();
	public Integer getAttackDurationInSeconds();
	public Integer getTimeUntilDeathCountsNotAsKillInSeconds();
	public Integer getBuildModeCooldownInMinutes();
	public Integer getBuildModeBaseRangeRadiusInBlocks();
	public Integer getDeathBanDurationInMinutes();
	public Integer getDeathBanDurationDuringBeaconFightInMinutes();
}
