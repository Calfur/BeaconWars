package com.github.calfur.beaconWars.configuration;

import com.github.calfur.beaconWars.Main;

public class ConfigurationManager {
	private ConfigFileLoader configurationDbConnection = new ConfigFileLoader();
	private Configuration configuration;
	
	public ConfigurationManager() {
		configurationDbConnection.loadConfigFile();
		configuration = new Configuration(configurationDbConnection.getDynamicConfiguration());
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public boolean updateConfiguration() {
		boolean success = updateDynamicConfiguration();
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
		return success;
	}
	
	private boolean updateDynamicConfiguration() {
		boolean success = configurationDbConnection.loadConfigFile();
		DynamicConfiguration dynamicConfiguration = configurationDbConnection.getDynamicConfiguration();
		configuration.setDynamicConfiguration(dynamicConfiguration);	
		return success;
	}
}
