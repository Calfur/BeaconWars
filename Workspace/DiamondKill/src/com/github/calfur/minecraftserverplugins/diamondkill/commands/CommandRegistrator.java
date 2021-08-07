package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class CommandRegistrator {
	public CommandRegistrator() {
		Main plugin = Main.getInstance();
		
		// /collect
		plugin.getCommand("collect").setExecutor(new CommandCollect());
		plugin.getCommand("collect").setTabCompleter(new TabCompleterCollect());
		
		// /player
		plugin.getCommand("player").setExecutor(new CommandPlayer());
		plugin.getCommand("player").setTabCompleter(new TabCompleterPlayer());
		
		// /team
		plugin.getCommand("team").setExecutor(new CommandTeam());
		plugin.getCommand("team").setTabCompleter(new TabCompleterTeam());

		// /playerkill
		plugin.getCommand("playerkill").setExecutor(new CommandKill());
		plugin.getCommand("playerkill").setTabCompleter(new TabCompleterPlayerKill());

		// /beaconfight
		plugin.getCommand("beaconfight").setExecutor(new CommandBeaconFight());
		plugin.getCommand("beaconfight").setTabCompleter(new TabCompleterBeaconfight());
		
		// /buildmode
		plugin.getCommand("buildmode").setExecutor(new CommandBuildMode());
		plugin.getCommand("buildmode").setTabCompleter(new TabCompleterBuildMode());
		
		// /projectstart
		CommandProjectStart commandProjectStart = new CommandProjectStart();
		plugin.setCommandProjectStart(commandProjectStart);
		plugin.getCommand("projectStart").setExecutor(commandProjectStart);
		plugin.getCommand("projectStart").setTabCompleter(new TabCompleterProjectStart());
		
		// /startprojectforplayers
		plugin.getCommand("startProjectForPlayers").setExecutor(new CommandStartProjectForPlayers());
		plugin.getCommand("startProjectForPlayers").setTabCompleter(new TabCompleterStartProjectForPlayers());
		
		// /totemcooldown
		plugin.getCommand("totemcooldown").setExecutor(new CommandTotemCooldown());
		plugin.getCommand("totemcooldown").setTabCompleter(new TabCompleterTotemCooldown());
		
		// /nametag
		plugin.getCommand("namenametag").setExecutor(new CommandNameNametag());
		plugin.getCommand("namenametag").setTabCompleter(new TabCompleterNameNametag());
	}
}
