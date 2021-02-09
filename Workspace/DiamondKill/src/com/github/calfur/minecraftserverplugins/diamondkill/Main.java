package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getCommand("collect").setExecutor(new CommandCollect());
	}
	@Override
	public void onDisable() {
		
	}
}
