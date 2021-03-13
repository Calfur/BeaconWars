package com.github.calfur.minecraftserverplugins.diamondkill;

import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.TeleportEvents;

public class EventRegistrator {
	public EventRegistrator() {
		registrateEvents();
	}
	private void registrateEvents() {
		Main plugin = Main.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new KillEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerRespawnEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new BlockBreakEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new MilkEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new TeleportEvents(), plugin);
	}
}
