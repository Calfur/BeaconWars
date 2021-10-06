package com.github.calfur.beaconWars.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFileLoader {
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	
	private File configFile;
	private FileConfiguration configFileConfiguration;
	
	public DynamicConfiguration getDynamicConfiguration() {
		return new DynamicConfiguration(
			getScoreboardTitle(), 
			getRewardBestBeaconDefenseDiamonds(), 
			getRewardBestBeaconDefensePoints(),
			getRewardBeaconRaidSuccessDiamonds(),
			getRewardBeaconRaidSuccessPoints(),
			getRewardKillBountyMultiplicatorDiamonds(),
			getRewardKillBountyMultiplicatorPoints(),
			areHungerGamesEnabled()
		);
	}
	
	private String getScoreboardTitle() {
		return (String)configFileConfiguration.get("ScoreboardTitle");
	}

	private Integer getRewardBestBeaconDefenseDiamonds() {
		return getIntegerConfiguration("RewardBestBeaconDefenseDiamonds");
	}

	private Integer getRewardBestBeaconDefensePoints() {
		return getIntegerConfiguration("RewardBestBeaconDefensePoints");
	}

	private Integer getRewardBeaconRaidSuccessDiamonds() {
		return getIntegerConfiguration("RewardBeaconRaidSuccessDiamonds");
	}

	private Integer getRewardBeaconRaidSuccessPoints() {
		return getIntegerConfiguration("RewardBeaconRaidSuccessPoints");
	}

	private Integer getRewardKillBountyMultiplicatorDiamonds() {
		return getIntegerConfiguration("RewardKillBountyMultiplicatorDiamonds");
	}

	private Integer getRewardKillBountyMultiplicatorPoints() {
		return getIntegerConfiguration("RewardKillBountyMultiplicatorPoints");
	}

	public Boolean areHungerGamesEnabled() {
		return getBooleanConfiguration("AreHungerGamesEnabled");
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

	private Integer getIntegerConfiguration(String configName) {
		try {			
			return (int)configFileConfiguration.get(configName);
		}catch (NullPointerException e){
			return null;
		}catch(ClassCastException e) {
			System.out.println("[" + ConstantConfiguration.pluginName + "] Config file loader: " + ANSI_RED + "The number set for " + configName + " is not valid" + ANSI_RESET);
			return null;
		}
	}
	
	private Boolean getBooleanConfiguration(String configName) {
		try {			
			return (boolean)configFileConfiguration.get(configName);
		}catch (NullPointerException e){
			return null;
		}catch(ClassCastException e) {
			System.out.println("[" + ConstantConfiguration.pluginName + "] Config file loader: " + ANSI_RED + "The number set for " + configName + " is not valid" + ANSI_RESET);
			return null;
		}
	}
}
