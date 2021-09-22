package com.github.calfur.beaconWars.configuration;

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
	public int getRewardBeaconDefenseTotalDiamonds() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardBeaconDefenseTotalDiamonds(), 
				defaultConfiguration.getRewardBeaconDefenseTotalDiamonds()
		);
	}

	@Override
	public int getRewardBeaconDefenseTotalPoints() {
		return getPreferredConfiguration(
				dynamicConfiguration.getRewardBeaconDefenseTotalPoints(), 
				defaultConfiguration.getRewardBeaconDefenseTotalPoints()
		);
	}
	
	private <T> T getPreferredConfiguration(T preferred, T alternative) {
		if(preferred != null) {
			return preferred;
		}
		return alternative;
	}

}
