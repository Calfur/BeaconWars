package com.github.calfur.beaconWars.commands;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.Reward;
import com.github.calfur.beaconWars.configuration.ConstantConfiguration;
import com.github.calfur.beaconWars.database.KillDbConnection;
import com.github.calfur.beaconWars.database.KillJson;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.helperClasses.KillHelper;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;
import com.github.calfur.beaconWars.pvp.TopKiller;

public class CommandKill implements CommandExecutor {

	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {					
		if(args.length >= 1) {
			String subCommand = args[0].toLowerCase();
			switch(subCommand) {
				case "info":
					return sendKillInfo(sender, args);
				case "list":
					return killList(sender, args);
				case "delete":
				case "remove":
					if(sender.hasPermission("admin")) {	
						return deleteKill(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				case "add":
					if(sender.hasPermission("admin")) {	
						return addKill(sender, args);
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

	private boolean killList(CommandSender sender, String[] args) {
		int page;
		int lastAvailablePage = killDbConnection.getAmountOfAvailablePages();
		if(args.length == 1) {
			page = lastAvailablePage;
		}else if (args.length == 2) {
			try {
				page = Integer.parseInt(args[1]);
			}catch (Exception e) {
				sender.sendMessage(StringFormatter.error("Der Parameter Page muss dem typ int entsprechen"));
				return false;
			}
			if(page < 1 || page > lastAvailablePage) {
				sender.sendMessage(StringFormatter.error("Seite " + page + " wurde nicht gefunden. Es sind " + lastAvailablePage + " Seiten verfügbar"));
				return false;
			}
		}else {		
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		sendKillList(sender, page);
		return true;
	}

	private void sendKillList(CommandSender sender, int page) {
		sender.sendMessage(ChatColor.BOLD + "Seite " + page + " von " + killDbConnection.getAmountOfAvailablePages() + ":");
		
		int firstKillToShow = page * 10 - 9;
		int lastKillToShow = page * 10;
		for (int i = firstKillToShow; i <= lastKillToShow; i++) {
			if(killDbConnection.existsKill(i)) {	
				KillJson kill = killDbConnection.getKill(i);

				ChatColor killerColor = getPlayerColor(kill.getKiller());
				ChatColor victimColor = getPlayerColor(kill.getVictim());
				
				sender.sendMessage(
						ChatColor.GOLD + "Id: " + ChatColor.RESET + i + 
						ChatColor.GOLD + ", Killer: " + killerColor + StringFormatter.firstLetterToUpper(kill.getKiller()) + 
						ChatColor.GOLD + ", Opfer: " + victimColor + StringFormatter.firstLetterToUpper(kill.getVictim()));
			}
		}
	}

	private ChatColor getPlayerColor(String playerName) {
		ChatColor playerColor = ChatColor.WHITE;
		PlayerJson playerJson = playerDbConnection.getPlayer(playerName);
		if(playerJson == null) {
			return playerColor;
		}
		TeamJson playerTeam = teamDbConnection.getTeam(playerJson.getTeamId());
		if(playerTeam == null) {				
			return playerColor;
		}
		playerColor = playerTeam.getColor();			
		return playerColor;
	}


	private boolean sendKillInfo(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			sender.sendMessage(StringFormatter.error("Dieser Kill ist nicht registriert"));
			return false;
		}
		KillJson killJson = killDbConnection.getKill(killId);
		sender.sendMessage(StringFormatter.bold("Id: " + killId)); 
		sender.sendMessage(ChatColor.RESET + "Killer: " + StringFormatter.firstLetterToUpper(killJson.getKiller())); 
		sender.sendMessage(ChatColor.RESET + "Opfer: " + StringFormatter.firstLetterToUpper(killJson.getVictim()));
		sender.sendMessage(ChatColor.RESET + "Zeit: " + killJson.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm")));
		return true;
	}
	
	private boolean deleteKill(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Kill Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			sender.sendMessage(StringFormatter.error("Dieser Kill ist nicht registriert"));
			return false;
		}
		killDbConnection.removeKill(killId);
		Main.getInstance().getScoreboardLoader().setTopKiller(TopKiller.getCurrentTopKiller());
		sender.sendMessage(ChatColor.GREEN + "Kill " + killId + " gelöscht.");
		return true;
	}
	
	private boolean addKill(CommandSender sender, String[] args) {
		if(args.length < 4) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		String killer = args[1];
		String victim = args[2];
		ArrayList<String> reasonWords = new ArrayList<>(Arrays.asList(args));
		reasonWords.remove(0);
		reasonWords.remove(0);
		reasonWords.remove(0);
		String reason = String.join(" ", reasonWords);
		
		if(!playerDbConnection.existsPlayer(killer)) {
			sender.sendMessage(StringFormatter.error("Der Killer " + killer + " ist nicht registriert"));
			return false;
		}
		
		if(!playerDbConnection.existsPlayer(victim)) {
			sender.sendMessage(StringFormatter.error("Das Opfer " + victim + " ist nicht registriert"));
			return false;
		}

		PlayerJson victimJson = playerDbConnection.getPlayer(victim);
		if(victimJson.getTeamId() == ConstantConfiguration.spectatorTeamNumber) {
			sender.sendMessage(StringFormatter.error("Der Spectator " + victim + " kann nicht als Opfer benutzt werden"));
			return false;
		}
		
		PlayerJson killerJson = playerDbConnection.getPlayer(killer);
		if(killerJson.getTeamId() == ConstantConfiguration.spectatorTeamNumber) {
			sender.sendMessage(StringFormatter.error("Dem Spectator " + killer + " kann kein Kill gutgeschrieben werden"));
			return false;
		}
		
		Reward payedReward = KillHelper.addKill(
			killer, 
			victim, 
			"Für den per command hinzugefügten Kill an " + victim + ", " + reason,
			Main.getInstance()
		);		

		sender.sendMessage(ChatColor.GREEN + "Kill registriert.");
		sender.sendMessage(ChatColor.RESET + StringFormatter.rewardText(payedReward) + " Kopfgeld an " + killer + " ausgezahlt");
		return true;
	}
}
