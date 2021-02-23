package com.github.calfur.minecraftserverplugins.diamondkill;

import com.github.calfur.minecraftserverplugins.diamondkill.commands.CommandCollect;
import com.github.calfur.minecraftserverplugins.diamondkill.commands.CommandKill;
import com.github.calfur.minecraftserverplugins.diamondkill.commands.CommandPlayer;
import com.github.calfur.minecraftserverplugins.diamondkill.commands.CommandTeam;

public class CommandRegistrator {
	public CommandRegistrator() {
		Main plugin = Main.getInstance();
		plugin.getCommand("collect").setExecutor(new CommandCollect());
		plugin.getCommand("player").setExecutor(new CommandPlayer());
		plugin.getCommand("team").setExecutor(new CommandTeam());
		plugin.getCommand("playerkill").setExecutor(new CommandKill());
	}
}
