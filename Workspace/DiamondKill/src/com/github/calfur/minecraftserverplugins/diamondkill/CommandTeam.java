package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTeam implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(player.hasPermission("admin")) {				
				if(args.length > 1) {
					String subCommand = args[0].toLowerCase();
					switch(subCommand) {
						case "info":
							if(args.length == 2) {
								int number = Integer.parseInt(args[1]);
								player.sendMessage(ChatColor.AQUA + "Team number : " + number);
							}
							return true;
					}
				}
			}
		}
		return false;
	}
}