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
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length >= 1) {
				String subCommand = args[0].toLowerCase();
				switch(subCommand) {
					case "info":
						return sendPlayerInfo(executor, args);
					case "list":
						return sendPlayerList(executor, args);
					case "delete":
					case "remove":
						if(executor.hasPermission("admin")) {	
							return deletePlayer(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung f�r diesen Command"));
							return true;
						}
					case "add":
						if(executor.hasPermission("admin")) {	
							return addPlayer(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung f�r diesen Command"));
							return true;
						}
					case "edit":
						if(executor.hasPermission("admin")) {	
							return editPlayer(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung f�r diesen Command"));
							return true;
						}
					default:
						executor.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendPlayerList(Player executor, String[] args) {
		if(args.length != 1) {
			executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige Anzahl Parameter"));
			return false;
		}
		Map<String, PlayerJson> players = playerDbConnection.getPlayers();
		executor.sendMessage(ChatColor.BOLD + "" + players.size() + " Spieler gefunden:");
		for (Entry<String, PlayerJson> player : players.entrySet()) {
			executor.sendMessage((teamDbConnection.getTeam(player.getValue().getTeamId()).getColor() + StringFormatter.firstLetterToUpper(player.getKey())) + (ChatColor.RESET + " " + killDbConnection.getAmountOfKills(player.getKey()) + " Kills / " + killDbConnection.getAmountOfDeaths(player.getKey()) + " Tode (durch Gegner)") + (ChatColor.AQUA + " " + killDbConnection.getBounty(player.getKey()) + " Dias") + (ChatColor.RESET + " Kopfgeld"));
		}
		return true;
	}

	private boolean sendPlayerInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige Anzahl Parameter"));
			return false;
		}
		String name = args[1];
		if(!playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(StringFormatter.error("Dieser Spieler ist nicht registriert"));
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
		
		executor.sendMessage((ChatColor.RESET + "Name: ") + (ChatColor.BOLD +  StringFormatter.firstLetterToUpper(name))); 
		executor.sendMessage((ChatColor.RESET + "Team: ") + (teamDbConnection.getTeam(playerJson.getTeamId()).getColor() + "" + playerJson.getTeamId()));
		executor.sendMessage((ChatColor.RESET + "Real Name: ") + (ChatColor.BOLD + playerJson.getRealName()));
		executor.sendMessage((ChatColor.RESET + "Guthaben: ") + ChatColor.AQUA + playerJson.getCollectableDiamonds() + " Dias");
		executor.sendMessage((ChatColor.RESET + "K/D (durch Gegner): " + (ChatColor.BOLD + "" + killDbConnection.getAmountOfKills(name) + "/" + killDbConnection.getAmountOfDeaths(name))));
		executor.sendMessage((ChatColor.RESET + "Tode gesammt: ") + (ChatColor.BOLD + "" + deaths));
		executor.sendMessage((ChatColor.RESET + "Spielzeit: ") + (ChatColor.BOLD + "" + playtime));
		executor.sendMessage((ChatColor.RESET + "Kopfgeld:") + (ChatColor.AQUA + " " + killDbConnection.getBounty(name) + " Dias"));
		return true;
	}
	
	private boolean deletePlayer(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige Anzahl Parameter"));
			return false;
		}
		String name = args[1];
		if(!playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(StringFormatter.error("Dieser Spieler ist nicht registriert"));
			return false;
		}
		playerDbConnection.removePlayer(name);
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		executor.sendMessage(ChatColor.GREEN + name + " gel�scht.");
		return true;
	}
	
	private boolean editPlayer(Player executor, String[] args) {
		if(args.length != 4) {
			executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige Anzahl Parameter"));
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
			executor.sendMessage(StringFormatter.error("Der Team Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(StringFormatter.error("Dieser Spieler ist nicht vorhanden"));
			return false;
		}
		int collectableDiamonds = playerDbConnection.getPlayer(name).getCollectableDiamonds();
		playerDbConnection.addPlayer(name, new PlayerJson(team, realName, collectableDiamonds));
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		executor.sendMessage(ChatColor.GREEN + name + " editiert.");
		return true;
	}
	
	private boolean addPlayer(Player executor, String[] args) {
		if(args.length != 4) {
			executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige Anzahl Parameter"));
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
			executor.sendMessage(StringFormatter.error("Der Team Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(StringFormatter.error("Dieser Spieler wurde bereits registriert"));	
			return false;
		}
		if(!teamDbConnection.existsTeam(team)) {
			executor.sendMessage(StringFormatter.error("Ein Team mit der Id " + team + " existiert nicht."));	
			return false;
		}
		playerDbConnection.addPlayer(name, new PlayerJson(team, realName));
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		executor.sendMessage(ChatColor.GREEN + name + " registriert.");
		return true;
	}
}