package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerConfig;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandPlayer implements CommandExecutor {
	
	private PlayerConfig playerConfig = Main.getInstance().getPlayerConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(player.hasPermission("admin")) {				
				if(args.length > 1) {
					String subCommand = args[0].toLowerCase();
					String name;
					switch(subCommand) {
						case "info":
						case "delete":
							if(args.length != 2) {
								player.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
								return false;
							}
							name = args[1];
							if(!playerConfig.existsPlayer(name)) {
								player.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht registriert");
								return false;
							}
							switch(subCommand) {
								case "info":
									PlayerJson jsonPlayer = playerConfig.getPlayer(name);
									player.sendMessage(ChatColor.AQUA + "Name: " + name); 
									player.sendMessage(ChatColor.AQUA + "Team: " + jsonPlayer.getTeam());
									player.sendMessage(ChatColor.AQUA + "Discord Name: " + jsonPlayer.getDiscordName());
									return true;
								case "delete":
									playerConfig.removePlayer(name);
									playerConfig.saveConfig().loadConfig();
									return true;
							}
							return false;
						case "add":
						case "edit":
							if(args.length != 4) {
								player.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
								return false;
							}
							int team;
							String discordName;
							try {
								name = args[1];
								team = Integer.parseInt(args[2]);
								discordName = args[3].toLowerCase();
							}catch(NumberFormatException e) {
								player.sendMessage(ChatColor.RED + "Der Team Parameter muss dem Typ Int entsprechen");
								return false;
							}
							switch(subCommand) {
								case "edit":
									if(!playerConfig.existsPlayer(name)) {
										player.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht vorhanden");		
									}
									break;
								case "add":
									if(playerConfig.existsPlayer(name)) {
										player.sendMessage(ChatColor.RED + "Dieser Spieler wurde bereits registriert");										
									}
									break;
								default: 
									player.sendMessage(ChatColor.RED + "Unerwarteter Fehler");
									return false;
							}
							playerConfig.addPlayer(name, new PlayerJson(team, discordName, 0));
							playerConfig.saveConfig().loadConfig();
							return true;
						default:
							player.sendMessage(ChatColor.RED + subCommand + " ist kein vorhandener Command");
							return false;
					}
				}
			}else {
				player.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
				return true;
			}
		}
		return false;
	}
}
