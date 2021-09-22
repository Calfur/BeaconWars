package com.github.calfur.beaconWars.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFileManager {

	private File configFile;
	private FileConfiguration configFileConfiguration;
	
	public DynamicConfiguration getDynamicConfiguration() {
		return new DynamicConfiguration(
			getScoreboardTitle(), 
			0, 
			0
		);
	}
	
	private String getScoreboardTitle() {
		return (String)configFileConfiguration.get("ScoreboardTitle");
	}

	public int getRewardBestBeaconDefenseDiamonds() {
		return (int)configFileConfiguration.get("RewardBestBeaconDefenseDiamonds");
	}

	public int getRewardBestBeaconDefensePoints() {
		return (int)configFileConfiguration.get("RewardBestBeaconDefensePoints");
	}

	public boolean loadConfigFile() {
		configFile = new File(ConstantConfiguration.pluginFolder, "config.yml");
		
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		configFileConfiguration = YamlConfiguration.loadConfiguration(configFile);
		if(configFileConfiguration.saveToString() == "") {
			return false;
		}
		return true;
	}
}
