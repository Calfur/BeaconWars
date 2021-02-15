package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main instance;
	private final PlayerConfig playerConfig = new PlayerConfig();
	private static ScoreboardLoader scoreboardLoader;
	@Override
	public void onEnable() {
		instance = this;
		new FeatureDisabler(); //FeatureDisabler disableFeatures = new 
		//this.getServer().getPluginManager().registerEvents(new EnchantingDisabler(), this);
		this.getCommand("collect").setExecutor(new CommandCollect());
		this.getCommand("player").setExecutor(new CommandPlayer());
		this.getCommand("team").setExecutor(new CommandTeam());
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
