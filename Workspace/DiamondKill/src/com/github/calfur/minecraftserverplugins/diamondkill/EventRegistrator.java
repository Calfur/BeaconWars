package com.github.calfur.minecraftserverplugins.diamondkill;

public class EventRegistrator {
	public EventRegistrator() {
		registrateEvents();
	}
	private void registrateEvents() {
		Main plugin = Main.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new KillEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerRespawnEvents(), plugin);
	}
}
