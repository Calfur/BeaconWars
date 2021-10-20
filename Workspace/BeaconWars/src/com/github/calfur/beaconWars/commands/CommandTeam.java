package com.github.calfur.beaconWars.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.ScoreboardLoader;
import com.github.calfur.beaconWars.beaconFight.BeaconManager;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandTeam implements CommandExecutor {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length >= 1) {
			String subCommand = args[0].toLowerCase();
			switch(subCommand) {
				case "info":
					return sendTeamInfo(sender, args);
				case "delete":
				case "remove":
					if(sender.hasPermission("admin")) {	
						return deleteTeam(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				case "add":
					if(sender.hasPermission("admin")) {	
						return addTeam(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				case "edit":
					if(sender.hasPermission("admin")) {	
						return editTeam(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				case "list":
					return sendTeamList(sender, args);
				default:
					sender.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
					return false;
			}
		}
		return false;
	}

	private boolean sendTeamList(CommandSender sender, String[] args) {
		if(args.length != 1) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		Map<String, TeamJson> teams = teamDbConnection.getTeams();
		sender.sendMessage(ChatColor.BOLD + "" + teams.size() + " Teams gefunden:");
		for (Entry<String, TeamJson> team : teams.entrySet()) {
			int x = team.getValue().getBeaconLocation().getBlockX();
			int y = team.getValue().getBeaconLocation().getBlockY();
			int z = team.getValue().getBeaconLocation().getBlockZ();
			sender.sendMessage(team.getValue().getColor() + "Team " + team.getKey() + ChatColor.RESET + ": Beacon Koords: (x: " + x + " y: " + y + " z: " + z + ") Punkte: " + team.getValue().getPoints());
		}
		return true;
	}

	private boolean sendTeamInfo(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			sender.sendMessage(StringFormatter.error("Dieses Team ist nicht registriert"));
			return false;
		}
		TeamJson teamJson = teamDbConnection.getTeam(teamNumber);
		Location beaconLocation = teamJson.getBeaconLocation();
		sender.sendMessage(ChatColor.RESET + "Name: " + ChatColor.BOLD + "Team " + teamNumber); 
		sender.sendMessage(ChatColor.RESET + "Teamleader: " + teamJson.getTeamLeader()); 
		sender.sendMessage(ChatColor.RESET + "Farbe: " + teamJson.getColor() + teamJson.getColor().name());
		sender.sendMessage(ChatColor.RESET + "Punkte: " + teamJson.getPoints());
		sender.sendMessage(ChatColor.RESET + "Beacon Position: XYZ= " + beaconLocation.getBlockX() + " / " + beaconLocation.getBlockY() + " / " + beaconLocation.getBlockZ());
		return true;
	}
	
	private boolean deleteTeam(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int teamNumber;
		try {
			teamNumber = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(teamNumber == -1) {
			sender.sendMessage(StringFormatter.error("-1 ist das Spectator Team. Es kann nicht gelöscht werden."));
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			sender.sendMessage(StringFormatter.error("Dieses Team ist nicht registriert"));
			return false;
		}
		HashMap<String, PlayerJson> players = playerDbConnection.getPlayers();
		for (Entry<String, PlayerJson> player : players.entrySet()) {
			if(player.getValue().getTeamId() == teamNumber) {
				sender.sendMessage(StringFormatter.error("Dieses Team hat noch zugewiesene Spieler und kann darum nicht gelöscht werden. Entferne zuerst alle Mitglieder dieses Teams."));
				return false;
			}
		}
		TeamJson team = teamDbConnection.getTeam(teamNumber);
		BeaconManager.removeLevelOneBeacon(team.getBeaconLocation());
		teamDbConnection.removeTeam(teamNumber);
		sender.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " gelöscht.");
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		return true;
	}
	
	private boolean editTeam(CommandSender sender, String[] args) {
		if(args.length != 7) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
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
			sender.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}try {
			chatColor = ChatColor.valueOf(args[2].toUpperCase());
		}catch(IllegalArgumentException e){
			sender.sendMessage(StringFormatter.error(args[2] + " ist keine gültige Farbe. Siehe: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html"));
			return false;
		}
		try {
			beaconLocationX = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Beacon_x Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationY = Integer.parseInt(args[4]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Beacon_y Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationZ = Integer.parseInt(args[5]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Beacon_z Parameter muss dem Typ int entsprechen"));
			return false;
		}
		teamLeader = args[6];
		if(teamNumber == -1) {
			sender.sendMessage(StringFormatter.error("-1 ist das Spectator Team. Es kann nicht bearbeitet werden."));
			return false;
		}
		if(!teamDbConnection.existsTeam(teamNumber)) {
			sender.sendMessage(StringFormatter.error("Dieses Team ist nicht registriert"));
			return false;
		}
		TeamJson oldTeam = teamDbConnection.getTeam(teamNumber);
		BeaconManager.removeLevelOneBeacon(oldTeam.getBeaconLocation());
		Location beaconLocation = new Location(Bukkit.getWorld(configuration.getBeaconEventWorldName()), beaconLocationX, beaconLocationY, beaconLocationZ);
		BeaconManager.placeLevelOneBeacon(beaconLocation);
		teamDbConnection.addTeam(teamNumber, new TeamJson(chatColor, teamLeader, beaconLocation, oldTeam.getPoints()));
		sender.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " editiert.");
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		return true;
	}
	
	private boolean addTeam(CommandSender sender, String[] args) {
		if(args.length != 7) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
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
			sender.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		try {
			chatColor = ChatColor.valueOf(args[2].toUpperCase());
		}catch(IllegalArgumentException e){
			sender.sendMessage(StringFormatter.error(args[2] + " ist keine gültige Farbe. Siehe: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html"));
			return false;
		}
		try {
			beaconLocationX = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Beacon_x Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationY = Integer.parseInt(args[4]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Beacon_y Parameter muss dem Typ int entsprechen"));
			return false;
		}
		try {
			beaconLocationZ = Integer.parseInt(args[5]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Beacon_z Parameter muss dem Typ int entsprechen"));
			return false;
		}
		teamLeader = args[6];
		if(teamDbConnection.existsTeam(teamNumber)) {
			sender.sendMessage(StringFormatter.error("Dieses Team wurde bereits registriert"));	
			return false;
		}
		World world = Bukkit.getWorld(configuration.getBeaconEventWorldName());
		Location beaconLocation = new Location(world, beaconLocationX, beaconLocationY, beaconLocationZ);
		BeaconManager.placeLevelOneBeacon(beaconLocation);
		teamDbConnection.addTeam(teamNumber, new TeamJson(chatColor, teamLeader, beaconLocation));
		sender.sendMessage(ChatColor.GREEN + "Team " + teamNumber + " registriert.");
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		return true;
	}
	
}