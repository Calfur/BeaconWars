package com.github.calfur.beaconWars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandReloadConfig implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			reloadConfig(sender);
			return true;
		}else {
			sender.sendMessage(StringFormatter.error("Zu viele Parameter angegeben"));
			return false;
		}
	}

	private void reloadConfig(CommandSender sender) {
		boolean success = Main.getInstance().reloadConfiguration();			
		if(!success){
			sender.sendMessage(StringFormatter.error("Das config.yml file entweder leer oder nicht valid"));
			return;
		}
		sender.sendMessage(ChatColor.GREEN + "Konfigurationen aus dem config.yml File geladen");
	}
}
