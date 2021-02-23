package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;

public class CommandTeam implements CommandExecutor {

	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;						
			if(args.length >= 1) {
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
					case "list":
						return sendTeamList(player, args);
					default:
						player.sendMessage(ChatColor.RED + subCommand + " ist kein vorhandener Command");
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendTeamList(Player executor, String[] args) {
		if(args.length != 1) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		Map<String, TeamJson> teams = teamDbConnection.getTeams();
		executor.sendMessage(ChatColor.AQUA + "" + teams.size() + " Teams gefunden:");
		for (Entry<String, TeamJson> team : teams.entrySet()) {
			executor.sendMessage(team.getValue().getColor() + "Team " + team.getKey());
		}
		return true;
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
		Location beaconLocation = teamJson.getBeaconPosition();
		executor.sendMessage(ChatColor.AQUA + "Name: " + teamNumber); 
		executor.sendMessage(ChatColor.AQUA + "Farbe: " + teamJson.getColor().name());
		executor.sendMessage(ChatColor.AQUA + "Beacon Position: XYZ= " + beaconLocation.getX() + " / " + beaconLocation.getY() + " / " + beaconLocation.getBlockZ());
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
		if(args.length != 6) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int teamNumber;
		ChatColor chatColor;
		int beaconLocationX;
		int beaconLocationY;
		int beaconLocationZ;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Teamnummer Parameter muss dem Typ Int entsprechen");
			return false;
		}try {
			chatColor = ChatColor.valueOf(args[2]);
		}catch(IllegalArgumentException e){
			executor.sendMessage(ChatColor.RED + args[2] + " ist keine gültige Farbe. Siehe: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html");
			return false;
		}
		try {
			beaconLocationX = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Beacon_x Parameter muss dem Typ int entsprechen");
			return false;
		}
		try {
			beaconLocationY = Integer.parseInt(args[4]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Beacon_y Parameter muss dem Typ int entsprechen");
			return false;
		}
		try {
			beaconLocationZ = Integer.parseInt(args[5]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Beacon_z Parameter muss dem Typ int entsprechen");
			return false;
		}
		
		if(!teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(ChatColor.RED + "Dieses Team ist nicht registriert");
			return false;
		}
		teamDbConnection.addTeam(teamNumber, new TeamJson(chatColor, new Location(executor.getWorld(), beaconLocationX, beaconLocationY, beaconLocationZ)));
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " editiert.");
		return true;
	}
	
	private boolean addTeam(Player executor, String[] args) {
		if(args.length != 6) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int teamNumber;
		ChatColor chatColor;
		int beaconLocationX;
		int beaconLocationY;
		int beaconLocationZ;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Teamnummer Parameter muss dem Typ Int entsprechen");
			return false;
		}try {
			chatColor = ChatColor.valueOf(args[2].toUpperCase());
		}catch(IllegalArgumentException e){
			executor.sendMessage(ChatColor.RED + args[2] + " ist keine gültige Farbe. Siehe: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html");
			return false;
		}
		try {
			beaconLocationX = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Beacon_x Parameter muss dem Typ int entsprechen");
			return false;
		}
		try {
			beaconLocationY = Integer.parseInt(args[4]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Beacon_y Parameter muss dem Typ int entsprechen");
			return false;
		}
		try {
			beaconLocationZ = Integer.parseInt(args[5]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Beacon_z Parameter muss dem Typ int entsprechen");
			return false;
		}
		
		if(teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(ChatColor.RED + "Dieses Team wurde bereits registriert");	
			return false;
		}
		teamDbConnection.addTeam(teamNumber, new TeamJson(chatColor, new Location(Bukkit.getWorld("world"), beaconLocationX, beaconLocationY, beaconLocationZ)));
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " registriert.");
		return true;
	}
}