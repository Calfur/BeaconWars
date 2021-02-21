package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;

public class CommandTeam implements CommandExecutor {

	private TeamDbConnection teamDbConnection = Main.getInstance().teamDbConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;						
			if(args.length > 1) {
				String subCommand = args[0].toLowerCase();
				switch(subCommand) {
					case "info":
						return sendTeamInfo(player, args);
					case "delete":
						if(player.hasPermission("admin")) {	
							return deleteTeam(player, args);
						}else {
							player.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					case "add":
						if(player.hasPermission("admin")) {	
							return addTeam(player, args);
						}else {
							player.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					case "edit":
						if(player.hasPermission("admin")) {	
							return editTeam(player, args);
						}else {
							player.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					default:
						player.sendMessage(ChatColor.RED + subCommand + " ist kein vorhandener Command");
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendTeamInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Teamnummer Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(ChatColor.RED + "Dieses Team ist nicht registriert");
			return false;
		}
		TeamJson teamJson = teamDbConnection.getTeam(teamNumber);
		executor.sendMessage(ChatColor.AQUA + "Name: " + teamNumber); 
		executor.sendMessage(ChatColor.AQUA + "Color: " + teamJson.getColor().name());
		//executor.sendMessage(ChatColor.AQUA + "Discord Name: " + teamJson.getBeaconPosition().toString());
		return true;
	}
	
	private boolean deleteTeam(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Teamnummer Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(ChatColor.RED + "Dieses Team ist nicht registriert");
			return false;
		}
		teamDbConnection.removeTeam(teamNumber);
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " gelöscht.");
		return true;
	}
	
	private boolean editTeam(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Teamnummer Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(ChatColor.RED + "Dieses Team ist nicht registriert");
			return false;
		}
		teamDbConnection.addTeam(teamNumber, new TeamJson(ChatColor.DARK_PURPLE/*, new Location(Bukkit.getWorld("minecraft:overworld"), 0, 100, 0)*/));
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " editiert.");
		return true;
	}
	
	private boolean addTeam(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Teamnummer Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(ChatColor.RED + "Dieses Team wurde bereits registriert");	
			return false;
		}
		teamDbConnection.addTeam(teamNumber, new TeamJson(ChatColor.DARK_PURPLE/*, new Location(Bukkit.getWorld("minecraft:overworld"), 0, 100, 0)*/));
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " registriert.");
		return true;
	}
}