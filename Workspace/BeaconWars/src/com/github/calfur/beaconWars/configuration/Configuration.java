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
	public Integer getRewardBeaconDefenseTotalDiamonds() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardBeaconDefenseTotalDiamonds(), 
				defaultConfiguration.getRewardBeaconDefenseTotalDiamonds()
		);
	}

	@Override
	public Integer getRewardBeaconDefenseTotalPoints() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardBeaconDefenseTotalPoints(), 
				defaultConfiguration.getRewardBeaconDefenseTotalPoints()
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

	private <T> T getPreferredConfiguration(T preferred, T alternative) {
		if(preferred != null) {
			return preferred;
		}
		return alternative;
	}
}
