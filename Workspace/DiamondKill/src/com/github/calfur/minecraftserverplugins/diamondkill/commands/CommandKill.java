package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.TopKiller;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandKill implements CommandExecutor {

	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length >= 1) {
				String subCommand = args[0].toLowerCase();
				switch(subCommand) {
					case "info":
						return sendKillInfo(executor, args);
					case "list":
						return killList(executor, args);
					case "delete":
					case "remove":
						if(executor.hasPermission("admin")) {	
							return deleteKill(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
							return true;
						}
					case "add":
						if(executor.hasPermission("admin")) {	
							return addKill(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
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

	private boolean killList(Player executor, String[] args) {
		int page;
		int lastAvailablePage = killDbConnection.getAmountOfAvailablePages();
		if(args.length == 1) {
			page = lastAvailablePage;
		}else if (args.length == 2) {
			try {
				page = Integer.parseInt(args[1]);
			}catch (Exception e) {
				executor.sendMessage(StringFormatter.error("Der Parameter Page muss dem typ int entsprechen"));
				return false;
			}
			if(page < 1 || page > lastAvailablePage) {
				executor.sendMessage(StringFormatter.error("Seite " + page + " wurde nicht gefunden. Es sind " + lastAvailablePage + " Seiten verfügbar"));
				return false;
			}
		}else {		
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		sendKillList(executor, page);
		return true;
	}

	private void sendKillList(Player executor, int page) {
		executor.sendMessage(ChatColor.BOLD + "Seite " + page + " von " + killDbConnection.getAmountOfAvailablePages() + ":");
		
		int firstKillToShow = page * 10 - 9;
		int lastKillToShow = page * 10;
		for (int i = firstKillToShow; i <= lastKillToShow; i++) {
			if(killDbConnection.existsKill(i)) {	
				KillJson kill = killDbConnection.getKill(i);

				ChatColor killerColor = getPlayerColor(kill.getKiller());
				ChatColor victimColor = getPlayerColor(kill.getVictim());
				
				executor.sendMessage(
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


	private boolean sendKillInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			executor.sendMessage(StringFormatter.error("Dieser Kill ist nicht registriert"));
			return false;
		}
		KillJson killJson = killDbConnection.getKill(killId);
		executor.sendMessage(StringFormatter.bold("Id: " + killId)); 
		executor.sendMessage(ChatColor.RESET + "Killer: " + StringFormatter.firstLetterToUpper(killJson.getKiller())); 
		executor.sendMessage(ChatColor.RESET + "Opfer: " + StringFormatter.firstLetterToUpper(killJson.getVictim()));
		executor.sendMessage(ChatColor.RESET + "Zeit: " + killJson.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm")));
		return true;
	}
	
	private boolean deleteKill(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Kill Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			executor.sendMessage(StringFormatter.error("Dieser Kill ist nicht registriert"));
			return false;
		}
		killDbConnection.removeKill(killId);
		Main.getInstance().getScoreboardLoader().setTopKiller(TopKiller.getCurrentTopKiller());
		executor.sendMessage(ChatColor.GREEN + "Kill " + killId + " gelöscht.");
		return true;
	}
	
	private boolean addKill(Player executor, String[] args) {
		if(args.length < 4) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		String killer = args[1];
		String victim = args[2];
		ArrayList<String> reasonWords = new ArrayList<>(Arrays.asList(args));
		reasonWords.remove(0);
		reasonWords.remove(0);
		reasonWords.remove(0);
		String reason = String.join(" ", reasonWords);
		LocalDateTime dateTime = LocalDateTime.now();
		
		if(!playerDbConnection.existsPlayer(killer)) {
			executor.sendMessage(StringFormatter.error("Der Killer " + killer + " ist nicht registriert"));
			return false;
		}
		
		if(!playerDbConnection.existsPlayer(victim)) {
			executor.sendMessage(StringFormatter.error("Das Opfer " + victim + " ist nicht registriert"));
			return false;
		}
				
		PlayerJson killerJson = playerDbConnection.getPlayer(killer);
		
		int bounty = killDbConnection.getBounty(victim);
		killerJson.addCollectableDiamonds(bounty);
		playerDbConnection.addPlayer(killer, killerJson);
		transactionDbConnection.addTransaction(new TransactionJson(killer, killerJson.getTeamId(), bounty, bounty*100, reason));

		String killId = killDbConnection.addKill(new KillJson(killer, victim, dateTime));
		
		Main.getInstance().getScoreboardLoader().setTopKiller(TopKiller.getCurrentTopKiller());

		executor.sendMessage(ChatColor.GREEN + "Kill " + killId + " registriert. " + ChatColor.RESET + bounty + " Diamanten Kompfgeld an " + killer + " ausgezahlt");
		return true;
	}
}
