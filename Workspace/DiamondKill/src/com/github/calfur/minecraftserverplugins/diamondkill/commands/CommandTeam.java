package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.BeaconManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.ScoreboardLoader;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandTeam implements CommandExecutor {

	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length >= 1) {
				String subCommand = args[0].toLowerCase();
				switch(subCommand) {
					case "info":
						return sendTeamInfo(executor, args);
					case "delete":
					case "remove":
						if(executor.hasPermission("admin")) {	
							return deleteTeam(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
							return true;
						}
					case "add":
						if(executor.hasPermission("admin")) {	
							return addTeam(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
							return true;
						}
					case "edit":
						if(executor.hasPermission("admin")) {	
							return editTeam(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
							return true;
						}
					case "list":
						return sendTeamList(executor, args);
					default:
						executor.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendTeamList(Player executor, String[] args) {
		if(args.length != 1) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		Map<String, TeamJson> teams = teamDbConnection.getTeams();
		executor.sendMessage(ChatColor.BOLD + "" + teams.size() + " Teams gefunden:");
		for (Entry<String, TeamJson> team : teams.entrySet()) {
			int x = team.getValue().getBeaconLocation().getBlockX();
			int y = team.getValue().getBeaconLocation().getBlockY();
			int z = team.getValue().getBeaconLocation().getBlockZ();
			executor.sendMessage(team.getValue().getColor() + "Team " + team.getKey() + ChatColor.RESET + ": Beacon Koords: (x: " + x + " y: " + y + " z: " + z + ")");
		}
		return true;
	}

	private boolean sendTeamInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(StringFormatter.error("Dieses Team ist nicht registriert"));
			return false;
		}
		TeamJson teamJson = teamDbConnection.getTeam(teamNumber);
		Location beaconLocation = teamJson.getBeaconLocation();
		executor.sendMessage(ChatColor.RESET + "Name: " + ChatColor.BOLD + "Team " + teamNumber); 
		executor.sendMessage(ChatColor.RESET + "Teamleader: " + teamJson.getTeamLeader()); 
		executor.sendMessage(ChatColor.RESET + "Farbe: " + teamJson.getColor() + teamJson.getColor().name());
		executor.sendMessage(ChatColor.RESET + "Beacon Position: XYZ= " + beaconLocation.getBlockX() + " / " + beaconLocation.getBlockY() + " / " + beaconLocation.getBlockZ());
		return true;
	}
	
	private boolean deleteTeam(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(teamNumber == -1) {
			executor.sendMessage(StringFormatter.error("-1 ist das Spectator Team. Es kann nicht gelöscht werden."));
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(StringFormatter.error("Dieses Team ist nicht registriert"));
			return false;
		}
		TeamJson team = teamDbConnection.getTeam(teamNumber);
		BeaconManager.removeLevelOneBeacon(team.getBeaconLocation());
		teamDbConnection.removeTeam(teamNumber);
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " gelöscht.");
		return true;
	}
	
	private boolean editTeam(Player executor, String[] args) {
		if(args.length != 7) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int teamNumber;
		ChatColor chatColor;
		int beaconLocationX;
		int beaconLocationY;
		int beaconLocationZ;
		String teamLeader;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}try {
			chatColor = ChatColor.valueOf(args[2].toUpperCase());
		}catch(IllegalArgumentException e){
			executor.sendMessage(StringFormatter.error(args[2] + " ist keine gültige Farbe. Siehe: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html"));
			return false;
		}
		try {
			beaconLocationX = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Beacon_x Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationY = Integer.parseInt(args[4]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Beacon_y Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationZ = Integer.parseInt(args[5]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Beacon_z Parameter muss dem Typ int entsprechen"));
			return false;
		}
		teamLeader = args[6];
		if(teamNumber == -1) {
			executor.sendMessage(StringFormatter.error("-1 ist das Spectator Team. Es kann nicht bearbeitet werden."));
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(StringFormatter.error("Dieses Team ist nicht registriert"));
			return false;
		}
		TeamJson oldTeam = teamDbConnection.getTeam(teamNumber);
		BeaconManager.removeLevelOneBeacon(oldTeam.getBeaconLocation());
		Location beaconLocation = new Location(executor.getWorld(), beaconLocationX, beaconLocationY, beaconLocationZ);
		BeaconManager.placeLevelOneBeacon(beaconLocation);
		teamDbConnection.addTeam(teamNumber, new TeamJson(chatColor, teamLeader, beaconLocation));
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " editiert.");
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		return true;
	}
	
	private boolean addTeam(Player executor, String[] args) {
		if(args.length != 7) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int teamNumber;
		ChatColor chatColor;
		int beaconLocationX;
		int beaconLocationY;
		int beaconLocationZ;
		String teamLeader;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		try {
			chatColor = ChatColor.valueOf(args[2].toUpperCase());
		}catch(IllegalArgumentException e){
			executor.sendMessage(StringFormatter.error(args[2] + " ist keine gültige Farbe. Siehe: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html"));
			return false;
		}
		try {
			beaconLocationX = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Beacon_x Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationY = Integer.parseInt(args[4]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Beacon_y Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationZ = Integer.parseInt(args[5]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Beacon_z Parameter muss dem Typ int entsprechen"));
			return false;
		}
		teamLeader = args[6];
		if(teamDbConnection.existsTeam(teamNumber)) {
			executor.sendMessage(StringFormatter.error("Dieses Team wurde bereits registriert"));	
			return false;
		}
		World world = executor.getWorld();
		Location beaconLocation = new Location(world, beaconLocationX, beaconLocationY, beaconLocationZ);
		BeaconManager.placeLevelOneBeacon(beaconLocation);
		teamDbConnection.addTeam(teamNumber, new TeamJson(chatColor, teamLeader, beaconLocation));
		executor.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " registriert.");
		return true;
	}
	
}