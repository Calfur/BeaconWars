package com.github.calfur.beaconWars.configuration;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFileManager {

	private File configFile = new File(ConstantConfiguration.pluginFolder, "config.yml");
	private FileConfiguration configFileConfiguration = YamlConfiguration.loadConfiguration(configFile);
	
	public DynamicConfiguration getDynamicConfiguration() {
		return new DynamicConfiguration(
			getScoreboardTitle()
		);
	}
	
	private String getScoreboardTitle() {
		return (String)configFileConfiguration.get("ScoreboardTitle");
	}
}
