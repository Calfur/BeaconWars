package com.github.calfur.beaconWars.configuration;

public class ConfigurationManager {
	private ConfigFileManager configurationDbConnection = new ConfigFileManager();
	private Configuration configuration;
	
	public ConfigurationManager() {
		updateConfiguration();
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public void updateConfiguration() {
		DynamicConfiguration dynamicConfiguration = configurationDbConnection.getDynamicConfiguration();
		configuration = new Configuration(dynamicConfiguration);
	}
}
