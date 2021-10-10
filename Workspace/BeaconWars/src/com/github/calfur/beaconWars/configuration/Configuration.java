package com.github.calfur.beaconWars.configuration;

import org.bukkit.Location;

public class Configuration implements IConfiguration{
	private DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
	private DynamicConfiguration dynamicConfiguration;

	public void setDynamicConfiguration(DynamicConfiguration dynamicConfiguration) {
		this.dynamicConfiguration = dynamicConfiguration;
	}

	public Configuration(DynamicConfiguration dynamicConfiguration) {
		this.dynamicConfiguration = dynamicConfiguration;
	}

	@Override
	public String getScoreboardTitle() {
		return getPreferredConfiguration(
				dynamicConfiguration.getScoreboardTitle(), 
				defaultConfiguration.getScoreboardTitle()
		);
	}

	@Override
	public Integer getDeductionBeaconRaidLostDefensePoints() {
		return getPreferredConfiguration(
				dynamicConfiguration.getDeductionBeaconRaidLostDefensePoints(), 
				defaultConfiguration.getDeductionBeaconRaidLostDefensePoints()
		);
	}

	@Override
	public Integer getRewardBeaconRaidSuccessDiamonds() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardBeaconRaidSuccessDiamonds(), 
				defaultConfiguration.getRewardBeaconRaidSuccessDiamonds()
		);
	}

	@Override
	public Integer getRewardBeaconRaidSuccessPoints() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardBeaconRaidSuccessPoints(), 
				defaultConfiguration.getRewardBeaconRaidSuccessPoints()
		);
	}

	@Override
	public Integer getRewardKillBountyMultiplicatorDiamonds() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardKillBountyMultiplicatorDiamonds(), 
				defaultConfiguration.getRewardKillBountyMultiplicatorDiamonds()
		);
	}

	@Override
	public Integer getRewardKillBountyMultiplicatorPoints() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardKillBountyMultiplicatorPoints(), 
				defaultConfiguration.getRewardKillBountyMultiplicatorPoints()
		);
	}

	@Override
	public Boolean areHungerGamesEnabled() {
		return getPreferredConfiguration(
				dynamicConfiguration.areHungerGamesEnabled(), 
				defaultConfiguration.areHungerGamesEnabled()
		);
	}

	@Override
	public Location getHungerGamesLocation() {
		return getPreferredConfiguration(
				dynamicConfiguration.getHungerGamesLocation(), 
				defaultConfiguration.getHungerGamesLocation()
		);
	}

	@Override
	public Integer getHungerGamesMinimumAmountOfRequiredOnlineTeams() {
		return getPreferredConfiguration(
				dynamicConfiguration.getHungerGamesMinimumAmountOfRequiredOnlineTeams(), 
				defaultConfiguration.getHungerGamesMinimumAmountOfRequiredOnlineTeams()
		);
	}

	@Override
	public Boolean areTeamPointsDisplayedInScoreboard() {
		return getPreferredConfiguration(
				dynamicConfiguration.areTeamPointsDisplayedInScoreboard(), 
				defaultConfiguration.areTeamPointsDisplayedInScoreboard()
		);
	}

	@Override
	public Integer getTotemCooldownLengthInMinutes() {
		return getPreferredConfiguration(
				dynamicConfiguration.getTotemCooldownLengthInMinutes(), 
				defaultConfiguration.getTotemCooldownLengthInMinutes()
		);
	}

	@Override
	public Integer getAttackDurationInSeconds() {
		return getPreferredConfiguration(
				dynamicConfiguration.getAttackDurationInSeconds(), 
				defaultConfiguration.getAttackDurationInSeconds()
		);
	}

	@Override
	public Integer getTimeUntilDeathCountsNotAsKillInSeconds() {
		return getPreferredConfiguration(
				dynamicConfiguration.getTimeUntilDeathCountsNotAsKillInSeconds(), 
				defaultConfiguration.getTimeUntilDeathCountsNotAsKillInSeconds()
		);
	}

	@Override
	public Integer getBuildModeCooldownInMinutes() {
		return getPreferredConfiguration(
				dynamicConfiguration.getBuildModeCooldownInMinutes(), 
				defaultConfiguration.getBuildModeCooldownInMinutes()
		);
	}

	@Override
	public Integer getBuildModeBaseRangeRadiusInBlocks() {
		return getPreferredConfiguration(
				dynamicConfiguration.getBuildModeBaseRangeRadiusInBlocks(), 
				defaultConfiguration.getBuildModeBaseRangeRadiusInBlocks()
		);
	}

	private <T> T getPreferredConfiguration(T preferred, T alternative) {
		if(preferred != null) {
			return preferred;
		}
		return alternative;
	}
}
