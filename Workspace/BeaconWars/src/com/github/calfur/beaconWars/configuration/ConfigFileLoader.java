package com.github.calfur.beaconWars.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
			getDeductionBeaconRaidLostDefensePoints(),
			getRewardBeaconRaidSuccessDiamonds(),
			getRewardBeaconRaidSuccessPoints(),
			getRewardKillBountyMultiplicatorDiamonds(),
			getRewardKillBountyMultiplicatorPoints(),
			areHungerGamesEnabled(),
			getHungerGamesLocation(),
			getHungerGamesMinimumAmountOfRequiredOnlineTeams(),
			areTeamPointsDisplayedInScoreboard(),
			getTotemCooldownLengthInMinutes(),
			getAttackDurationInSeconds(),
			getTimeUntilDeathCountsNotAsKillInSeconds(),
			getBuildModeCooldownInMinutes(),
			getBuildModeBaseRangeRadiusInBlocks(),
			getDeathBanDurationInMinutes(),
			getDeathBanDurationDuringBeaconFightInMinutes(),
			getBeaconEventWorldName()
		);
	}

	private String getScoreboardTitle() {
		return getStringConfiguration("ScoreboardTitle");
	}

	private Integer getDeductionBeaconRaidLostDefensePoints() {
		return getIntegerConfiguration("DeductionBeaconRaidLostDefensePoints");
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

	private Boolean areHungerGamesEnabled() {
		return getBooleanConfiguration("AreHungerGamesEnabled");
	}
	
	private Location getHungerGamesLocation() {
		Double hungerGamesLocationX = getDoubleConfiguration("HungerGamesLocationX");
		Double hungerGamesLocationY = getDoubleConfiguration("HungerGamesLocationY");
		Double hungerGamesLocationZ = getDoubleConfiguration("HungerGamesLocationZ");
		String hungerGamesLocationWorld = getStringConfiguration("HungerGamesLocationWorld");

		if(hungerGamesLocationX == null
			|| hungerGamesLocationY == null
			|| hungerGamesLocationZ == null
			|| hungerGamesLocationWorld == null) {
			return null;
		}
		
		return new Location(
			Bukkit.getWorld(hungerGamesLocationWorld),
			hungerGamesLocationX,
			hungerGamesLocationY,
			hungerGamesLocationZ
		);
	}

	private Integer getHungerGamesMinimumAmountOfRequiredOnlineTeams() {
		return getIntegerConfiguration("HungerGamesMinimumAmountOfRequiredOnlineTeams");
	}
	
	private Boolean areTeamPointsDisplayedInScoreboard() {
		return getBooleanConfiguration("AreTeamPointsDisplayedInScoreboard");
	}
	
	private Integer getTotemCooldownLengthInMinutes() {
		return getIntegerConfiguration("TotemCooldownLengthInMinutes");
	}

	private Integer getAttackDurationInSeconds() {
		return getIntegerConfiguration("AttackDurationInSeconds");
	}

	private Integer getTimeUntilDeathCountsNotAsKillInSeconds() {
		return getIntegerConfiguration("TimeUntilDeathCountsNotAsKillInSeconds");
	}

	private Integer getBuildModeCooldownInMinutes() {
		return getIntegerConfiguration("BuildModeCooldownInMinutes");
	}

	private Integer getBuildModeBaseRangeRadiusInBlocks() {
		return getIntegerConfiguration("BuildModeBaseRangeRadiusInBlocks");
	}

	public Integer getDeathBanDurationInMinutes() {
		return getIntegerConfiguration("DeathBanDurationInMinutes");
	}

	public Integer getDeathBanDurationDuringBeaconFightInMinutes() {
		return getIntegerConfiguration("DeathBanDurationDuringBeaconFightInMinutes");
	}
	
	private String getBeaconEventWorldName() {
		return getStringConfiguration("BeaconEventWorldName");
	}

	public boolean loadConfigFile() {
		createConfigFileFolderIfNotExists();
	    createConfigFileIfNotExists();
		
		configFileConfiguration = YamlConfiguration.loadConfiguration(configFile);
		if(configFileConfiguration.saveToString() == "") {
			return false;
		}
		return true;
	}

	private void createConfigFileFolderIfNotExists() {
		File directory = new File(ConstantConfiguration.pluginFolder);
	    if (! directory.exists()){
	        directory.mkdirs();
	    }
	}

	private void createConfigFileIfNotExists() {
		configFile = new File(ConstantConfiguration.pluginFolder, "config.yml");
		
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getStringConfiguration(String configName) {
		String value = (String) configFileConfiguration.get(configName);
		if(value == null) {
			return null;
		}
		if(value.isEmpty()) {
			return null;
		}
		return value;
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

	private Double getDoubleConfiguration(String configName) {
		try {			
			return (double)configFileConfiguration.get(configName);
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
