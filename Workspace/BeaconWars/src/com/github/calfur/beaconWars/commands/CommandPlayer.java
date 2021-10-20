package com.github.calfur.beaconWars.commands;

import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.ScoreboardLoader;
import com.github.calfur.beaconWars.database.KillDbConnection;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandPlayer implements CommandExecutor {

	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection();
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length >= 1) {
			String subCommand = args[0].toLowerCase();
			switch(subCommand) {
				case "info":
					return sendPlayerInfo(sender, args);
				case "list":
					return sendPlayerList(sender, args);
				case "delete":
				case "remove":
					if(sender.hasPermission("admin")) {	
						return deletePlayer(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				case "add":
					if(sender.hasPermission("admin")) {	
						return addPlayer(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				case "edit":
					if(sender.hasPermission("admin")) {	
						return editPlayer(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				default:
					sender.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
					return false;
			}
		}	
		return false;
	}

	private boolean sendPlayerList(CommandSender sender, String[] args) {
		if(args.length != 1) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		Map<String, PlayerJson> players = playerDbConnection.getPlayers();
		sender.sendMessage(ChatColor.BOLD + "" + players.size() + " Spieler gefunden:");
		for (Entry<String, PlayerJson> player : players.entrySet()) {
			sender.sendMessage((teamDbConnection.getTeam(player.getValue().getTeamId()).getColor() + StringFormatter.firstLetterToUpper(player.getKey())) + (ChatColor.RESET + " " + killDbConnection.getAmountOfKills(player.getKey()) + " Kills / " + killDbConnection.getAmountOfDeaths(player.getKey()) + " Tode (durch Gegner)") + (ChatColor.AQUA + " " + killDbConnection.getBounty(player.getKey()) + " Dias") + (ChatColor.RESET + " Kopfgeld"));
		}
		return true;
	}

	private boolean sendPlayerInfo(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		String name = args[1];
		if(!playerDbConnection.existsPlayer(name)) {
			sender.sendMessage(StringFormatter.error("Dieser Spieler ist nicht registriert"));
			return false;
		}
		PlayerJson playerJson = playerDbConnection.getPlayer(name);
		
		Player player = Bukkit.getPlayerExact(name);
		String deaths = "";
		String playtime = "";
		if(player != null) {			
			deaths = "" + player.getStatistic(Statistic.DEATHS);
			playtime = "" + player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60/60 + "h";
		}
		
		sender.sendMessage((ChatColor.RESET + "Name: ") + (ChatColor.BOLD +  StringFormatter.firstLetterToUpper(name))); 
		sender.sendMessage((ChatColor.RESET + "Team: ") + (teamDbConnection.getTeam(playerJson.getTeamId()).getColor() + "" + playerJson.getTeamId()));
		sender.sendMessage((ChatColor.RESET + "Real Name: ") + (ChatColor.BOLD + playerJson.getRealName()));
		sender.sendMessage((ChatColor.RESET + "Guthaben: ") + ChatColor.AQUA + playerJson.getCollectableDiamonds() + " Dias");
		sender.sendMessage((ChatColor.RESET + "K/D (durch Gegner): " + (ChatColor.BOLD + "" + killDbConnection.getAmountOfKills(name) + "/" + killDbConnection.getAmountOfDeaths(name))));
		sender.sendMessage((ChatColor.RESET + "Tode gesammt: ") + (ChatColor.BOLD + "" + deaths));
		sender.sendMessage((ChatColor.RESET + "Spielzeit: ") + (ChatColor.BOLD + "" + playtime));
		sender.sendMessage((ChatColor.RESET + "Kopfgeld:") + (ChatColor.AQUA + " " + killDbConnection.getBounty(name) + " Dias"));
		return true;
	}
	
	private boolean deletePlayer(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		String name = args[1];
		if(!playerDbConnection.existsPlayer(name)) {
			sender.sendMessage(StringFormatter.error("Dieser Spieler ist nicht registriert"));
			return false;
		}
		playerDbConnection.removePlayer(name);
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		sender.sendMessage(ChatColor.GREEN + name + " gelöscht.");
		return true;
	}
	
	private boolean editPlayer(CommandSender sender, String[] args) {
		if(args.length != 4) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		String name;
		int team;
		String realName;
		try {
			name = args[1];
			team = Integer.parseInt(args[2]);
			realName = args[3];
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Team Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!playerDbConnection.existsPlayer(name)) {
			sender.sendMessage(StringFormatter.error("Dieser Spieler ist nicht vorhanden"));
			return false;
		}
		int collectableDiamonds = playerDbConnection.getPlayer(name).getCollectableDiamonds();
		playerDbConnection.addPlayer(name, new PlayerJson(team, realName, collectableDiamonds));
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		sender.sendMessage(ChatColor.GREEN + name + " editiert.");
		return true;
	}
	
	private boolean addPlayer(CommandSender sender, String[] args) {
		if(args.length != 4) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		String name;
		int team;
		String realName;
		try {
			name = args[1];
			team = Integer.parseInt(args[2]);
			realName = args[3];
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Team Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(playerDbConnection.existsPlayer(name)) {
			sender.sendMessage(StringFormatter.error("Dieser Spieler wurde bereits registriert"));	
			return false;
		}
		if(!teamDbConnection.existsTeam(team)) {
			sender.sendMessage(StringFormatter.error("Ein Team mit der Id " + team + " existiert nicht."));	
			return false;
		}
		playerDbConnection.addPlayer(name, new PlayerJson(team, realName));
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		sender.sendMessage(ChatColor.GREEN + name + " registriert.");
		return true;
	}
}
