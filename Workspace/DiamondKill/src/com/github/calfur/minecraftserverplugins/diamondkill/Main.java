package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main instance;
	private final PlayerConfig playerConfig = new PlayerConfig();
	private static ScoreboardLoader scoreboardLoader;
	@Override
	public void onEnable() {
		instance = this;
		new FeatureDisabler(); 
		new CommandRegistrator();
		
		getPlayerConfig().loadConfig();
		scoreboardLoader = new ScoreboardLoader();
		scoreboardLoader.setTopKiller(new TopKiller("Calfur", 52));
	}
	@Override
	public void onDisable() {
		
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public PlayerConfig getPlayerConfig() {
		return playerConfig;
	}
}
