package com.github.calfur.beaconWars.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandReloadConfig implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length == 0) {
				reloadConfig(executor);
				return true;
			}else {
				executor.sendMessage(StringFormatter.error("Zu viele Parameter angegeben"));
				return false;
			}
		}
		return false;
	}

	private void reloadConfig(Player executor) {
		boolean success = Main.getInstance().reloadConfiguration();			
		if(!success){
			executor.sendMessage(StringFormatter.error("Das config.yml file entweder leer oder nicht valid"));
			return;
		}
		executor.sendMessage(ChatColor.GREEN + "Konfigurationen aus dem config.yml File geladen");
	}
}
