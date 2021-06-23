package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.ScoreboardLoader;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;

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
							executor.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					case "add":
						if(executor.hasPermission("admin")) {	
							return addPlayer(executor, args);
						}else {
							executor.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					case "edit":
						if(executor.hasPermission("admin")) {	
							return editPlayer(executor, args);
						}else {
							executor.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					default:
						executor.sendMessage(ChatColor.RED + subCommand + " ist kein vorhandener Subcommand");
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendPlayerList(Player executor, String[] args) {
		if(args.length != 1) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		Map<String, PlayerJson> players = playerDbConnection.getPlayers();
		executor.sendMessage(ChatColor.BOLD + "" + players.size() + " Spieler gefunden:");
		for (Entry<String, PlayerJson> player : players.entrySet()) {
			executor.sendMessage((teamDbConnection.getTeam(player.getValue().getTeamId()).getColor() + StringFormatter.FirstLetterToUpper(player.getKey())) + (ChatColor.RESET + " " + killDbConnection.getAmountOfKills(player.getKey()) + " Kills / " + killDbConnection.getAmountOfDeaths(player.getKey()) + " Tode (durch Gegner)") + (ChatColor.AQUA + " " + killDbConnection.getBounty(player.getKey()) + " Dias") + (ChatColor.RESET + " Kopfgeld"));
		}
		return true;
	}

	private boolean sendPlayerInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		String name = args[1];
		if(!playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht registriert");
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
		
		executor.sendMessage((ChatColor.RESET + "Name: ") + (ChatColor.BOLD +  StringFormatter.FirstLetterToUpper(name))); 
		executor.sendMessage((ChatColor.RESET + "Team: ") + (teamDbConnection.getTeam(playerJson.getTeamId()).getColor() + "" + playerJson.getTeamId()));
		executor.sendMessage((ChatColor.RESET + "Discord Name: ") + (ChatColor.BOLD + playerJson.getDiscordName()));
		executor.sendMessage((ChatColor.RESET + "Guthaben: ") + ChatColor.AQUA + playerJson.getCollectableDiamonds() + " Dias");
		executor.sendMessage((ChatColor.RESET + "K/D (durch Gegner): " + (ChatColor.BOLD + "" + killDbConnection.getAmountOfKills(name) + "/" + killDbConnection.getAmountOfDeaths(name))));
		executor.sendMessage((ChatColor.RESET + "Tode gesammt: ") + (ChatColor.BOLD + "" + deaths));
		executor.sendMessage((ChatColor.RESET + "Spielzeit: ") + (ChatColor.BOLD + "" + playtime));
		executor.sendMessage((ChatColor.RESET + "Kopfgeld:") + (ChatColor.AQUA + " " + killDbConnection.getBounty(name) + " Dias"));
		return true;
	}
	
	private boolean deletePlayer(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		String name = args[1];
		if(!playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht registriert");
			return false;
		}
		playerDbConnection.removePlayer(name);
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		executor.sendMessage(ChatColor.GREEN + name + " gelöscht.");
		return true;
	}
	
	private boolean editPlayer(Player executor, String[] args) {
		if(args.length != 4) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		String name;
		int team;
		String discordName;
		try {
			name = args[1];
			team = Integer.parseInt(args[2]);
			discordName = args[3].toLowerCase();
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Team Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht vorhanden");
			return false;
		}
		playerDbConnection.addPlayer(name, new PlayerJson(team, discordName, 0));
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		executor.sendMessage(ChatColor.GREEN + name + " editiert.");
		return true;
	}
	
	private boolean addPlayer(Player executor, String[] args) {
		if(args.length != 4) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		String name;
		int team;
		String discordName;
		try {
			name = args[1];
			team = Integer.parseInt(args[2]);
			discordName = args[3].toLowerCase();
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Team Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(ChatColor.RED + "Dieser Spieler wurde bereits registriert");	
			return false;
		}
		if(!teamDbConnection.existsTeam(team)) {
			executor.sendMessage(ChatColor.RED + "Ein Team mit der Id " + team + " existiert nicht.");	
			return false;
		}
		playerDbConnection.addPlayer(name, new PlayerJson(team, discordName, 0));
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
		executor.sendMessage(ChatColor.GREEN + name + " registriert.");
		return true;
	}
}
