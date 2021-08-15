package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.RewardManager;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandTransaction implements CommandExecutor {

	private TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private RewardManager rewardManager = Main.getInstance().getRewardManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length >= 1) {
				String subCommand = args[0].toLowerCase();
				switch(subCommand) {
					case "info":
						return doTransactionInfo(executor, args);
					case "list":
						return doTransactionList(executor, args);
					case "add": 
						if(executor.hasPermission("admin")) {	
							return doAddReward(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
							return true;
						}
					case "reverse":
						if(executor.hasPermission("admin")) {	
							return doReverse(executor, args);
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

	private boolean doTransactionList(Player executor, String[] args) {
		int page;
		int lastAvailablePage = transactionDbConnection.getAmountOfAvailablePages();
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
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		sendTransactionList(executor, page);
		return true;
	}

	private void sendTransactionList(Player executor, int page) {
		executor.sendMessage(ChatColor.BOLD + "Seite " + page + " von " + transactionDbConnection.getAmountOfAvailablePages() + ":");
		
		int firstTransactionToShow = page * 10 - 9;
		int lastTransactionToShow = page * 10;
		for (int i = firstTransactionToShow; i <= lastTransactionToShow; i++) {
			if(transactionDbConnection.existsTransaction(i)) {				
				TransactionJson transactionJson = transactionDbConnection.getTransaction(i);
				ChatColor teamColor = getTeamColor(transactionJson.getTeam());
				executor.sendMessage( 
						ChatColor.GOLD + "Id: " + ChatColor.RESET + i + 
						ChatColor.GOLD + ", Zeit: " + ChatColor.RESET + transactionJson.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")) + 
						ChatColor.GOLD + ", Spieler: " + teamColor + transactionJson.getPlayerName() + ChatColor.RESET + 
						ChatColor.GOLD + ", Diamanten: " + ChatColor.AQUA + transactionJson.getTransactedDiamonds() + ChatColor.RESET + 
						ChatColor.GOLD + ", Punkte: " + ChatColor.RESET + transactionJson.getTransactedPoints());
			}
		}
	}

	private ChatColor getTeamColor(int teamId) {
		ChatColor teamColor = ChatColor.WHITE;
		TeamJson teamJson = teamDbConnection.getTeam(teamId);
		if(teamJson == null) {				
			return teamColor;
		}
		teamColor = teamJson.getColor();			
		return teamColor;
	}
	
	private boolean doTransactionInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int transactionId;
		try {
			transactionId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!transactionDbConnection.existsTransaction(transactionId)) {
			executor.sendMessage(StringFormatter.error("Diese Transaktion ist nicht registriert"));
			return false;
		}
		TransactionJson transactionJson = transactionDbConnection.getTransaction(transactionId);
		if(!teamDbConnection.existsTeam(transactionJson.getTeam())) {
			executor.sendMessage(StringFormatter.error("Dieses Teams ist nicht registriert"));
			return false;
		}
		TeamJson teamJson = teamDbConnection.getTeam(transactionJson.getTeam());
		executor.sendMessage(StringFormatter.bold("Id: " + transactionId));
		executor.sendMessage(ChatColor.GOLD + "Zeit: " + ChatColor.RESET + transactionJson.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))); 
		executor.sendMessage(ChatColor.GOLD + "Spieler: " + teamJson.getColor() + transactionJson.getPlayerName());
		executor.sendMessage(ChatColor.GOLD + "Team: " + teamJson.getColor() + transactionJson.getTeam());
		executor.sendMessage(ChatColor.GOLD + "Transferierte Diamanten: " + ChatColor.AQUA + transactionJson.getTransactedDiamonds());
		executor.sendMessage(ChatColor.GOLD + "Transferierte Punkte: " + ChatColor.RESET + transactionJson.getTransactedPoints());
		executor.sendMessage(ChatColor.GOLD + "Grund: " + ChatColor.RESET + transactionJson.getReason());
		return true;
	}
	
	private boolean doAddReward(Player executor, String[] args) {
		if(args.length < 5) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[2]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Parameter 'amount' muss dem Typ Int entsprechen"));
			return false;
		}
		if(amount == 0) {
			executor.sendMessage(StringFormatter.error("Der Parameter 'amount' darf nicht 0 sein"));
			return false;
		}
		String name = args[3];
		if(!playerDbConnection.existsPlayer(name)) {
			executor.sendMessage(StringFormatter.error("Dieser Spieler ist nicht vorhanden"));
			return false;
		}
		ArrayList<String> reasonWords = new ArrayList<>(Arrays.asList(args));
		reasonWords.remove(0); // add
		reasonWords.remove(0); // diamonds | points
		reasonWords.remove(0); // amount
		reasonWords.remove(0); // player
		String reason = String.join(" ", reasonWords);
		String subCommand = args[1];
		switch(subCommand) {
		case "diamonds": 
			rewardManager.addReward(name, amount, 0, reason);
			break;
		case "points":
			rewardManager.addReward(name, 0, amount, reason);
			break;
		default:
			executor.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
			return false;
		}
		executor.sendMessage(ChatColor.GREEN + "Transaktion erfolgreich ausgeführt");
		return true;
	}

	private boolean doReverse(Player executor, String[] args) {
		if(args.length < 3) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int transactionId;
		try {
			transactionId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Parameter Id muss dem Typ Int entsprechen"));
			return false;
		}
		ArrayList<String> reasonWords = new ArrayList<>(Arrays.asList(args));
		reasonWords.remove(0); // revert
		reasonWords.remove(0); // Id
		String reason = String.join(" ", reasonWords);
		if(!transactionDbConnection.existsTransaction(transactionId)) {
			executor.sendMessage(StringFormatter.error("Diese Transaktion ist nicht registriert"));
			return false;
		}
		TransactionJson transactionJson = transactionDbConnection.getTransaction(transactionId);
		rewardManager.addReward(
				transactionJson.getPlayerName(), 
				-transactionJson.getTransactedDiamonds(), 
				-transactionJson.getTransactedPoints(), 
				"Die Transaktion " + transactionId + " wurde rückgängig gemacht, weil: " + reason);
		executor.sendMessage(ChatColor.GREEN + "Transaktion erfolgreich rückgängig gemacht");
		return true;
	}
}
