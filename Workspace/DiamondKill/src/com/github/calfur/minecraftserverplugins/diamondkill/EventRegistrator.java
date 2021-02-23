package com.github.calfur.minecraftserverplugins.diamondkill;

public class EventRegistrator {
	public EventRegistrator() {
		registrateEvents();
	}
	private void registrateEvents() {
		Main plugin = Main.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new KillEvent(), plugin);
	}
}
