package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class CommandRegistrator {
	public CommandRegistrator() {
		Main plugin = Main.getInstance();
		plugin.getCommand("collect").setExecutor(new CommandCollect());
		plugin.getCommand("player").setExecutor(new CommandPlayer());
		plugin.getCommand("team").setExecutor(new CommandTeam());
		plugin.getCommand("playerkill").setExecutor(new CommandKill());
		plugin.getCommand("buildmode").setExecutor(new CommandBuildMode());
		plugin.getCommand("beaconfight").setExecutor(new CommandBeaconFight());
	}
}
