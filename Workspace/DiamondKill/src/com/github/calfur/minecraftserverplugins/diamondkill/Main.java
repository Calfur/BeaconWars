package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main instance;
	private final PlayerConfig playerConfig = new PlayerConfig();
	@Override
	public void onEnable() {
		instance = this;
		this.getCommand("collect").setExecutor(new CommandCollect());
		this.getCommand("player").setExecutor(new CommandPlayer());
		getPlayerConfig().loadConfig();
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
