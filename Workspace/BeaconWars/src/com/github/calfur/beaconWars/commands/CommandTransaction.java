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
import com.github.calfur.beaconWars.RewardManager;
import com.github.calfur.beaconWars.configuration.ConstantConfiguration;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.database.TransactionDbConnection;
import com.github.calfur.beaconWars.database.TransactionJson;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandTransaction implements CommandExecutor {

	private TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private RewardManager rewardManager = Main.getInstance().getRewardManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {				
		if(args.length >= 1) {
			String subCommand = args[0].toLowerCase();
			switch(subCommand) {
				case "info":
					return doTransactionInfo(sender, args);
				case "list":
					return doTransactionList(sender, args);
				case "add": 
					if(sender.hasPermission("admin")) {	
						return doAddReward(sender, args);
					}else {
						sender.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
						return true;
					}
				case "reverse":
					if(sender.hasPermission("admin")) {	
						return doReverse(sender, args);
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

	private boolean doTransactionList(CommandSender sender, String[] args) {
		int page;
		int lastAvailablePage = transactionDbConnection.getAmountOfAvailablePages();
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
		sendTransactionList(sender, page);
		return true;
	}

	private void sendTransactionList(CommandSender sender, int page) {
		sender.sendMessage(ChatColor.BOLD + "Seite " + page + " von " + transactionDbConnection.getAmountOfAvailablePages() + ":");
		
		int firstTransactionToShow = page * 10 - 9;
		int lastTransactionToShow = page * 10;
		for (int i = firstTransactionToShow; i <= lastTransactionToShow; i++) {
			if(transactionDbConnection.existsTransaction(i)) {				
				TransactionJson transactionJson = transactionDbConnection.getTransaction(i);
				ChatColor teamColor = getTeamColor(transactionJson.getTeam());
				sender.sendMessage( 
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
	
	private boolean doTransactionInfo(CommandSender sender, String[] args) {
		if(args.length != 2) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int transactionId;
		try {
			transactionId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!transactionDbConnection.existsTransaction(transactionId)) {
			sender.sendMessage(StringFormatter.error("Diese Transaktion ist nicht registriert"));
			return false;
		}
		TransactionJson transactionJson = transactionDbConnection.getTransaction(transactionId);
		if(!teamDbConnection.existsTeam(transactionJson.getTeam())) {
			sender.sendMessage(StringFormatter.error("Dieses Teams ist nicht registriert"));
			return false;
		}
		TeamJson teamJson = teamDbConnection.getTeam(transactionJson.getTeam());
		sender.sendMessage(StringFormatter.bold("Id: " + transactionId));
		sender.sendMessage(ChatColor.GOLD + "Zeit: " + ChatColor.RESET + transactionJson.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))); 
		sender.sendMessage(ChatColor.GOLD + "Spieler: " + teamJson.getColor() + transactionJson.getPlayerName());
		sender.sendMessage(ChatColor.GOLD + "Team: " + teamJson.getColor() + transactionJson.getTeam());
		sender.sendMessage(ChatColor.GOLD + "Transferierte Diamanten: " + ChatColor.AQUA + transactionJson.getTransactedDiamonds());
		sender.sendMessage(ChatColor.GOLD + "Transferierte Punkte: " + ChatColor.RESET + transactionJson.getTransactedPoints());
		sender.sendMessage(ChatColor.GOLD + "Grund: " + ChatColor.RESET + transactionJson.getReason());
		return true;
	}
	
	private boolean doAddReward(CommandSender sender, String[] args) {
		if(args.length < 5) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[2]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Parameter 'amount' muss dem Typ Int entsprechen"));
			return false;
		}
		if(amount == 0) {
			sender.sendMessage(StringFormatter.error("Der Parameter 'amount' darf nicht 0 sein"));
			return false;
		}
		String name = args[3];
		if(!playerDbConnection.existsPlayer(name)) {
			sender.sendMessage(StringFormatter.error("Dieser Spieler ist nicht vorhanden"));
			return false;
		}
		PlayerJson playerJson = playerDbConnection.getPlayer(name);
		if(playerJson.getTeamId() == ConstantConfiguration.spectatorTeamNumber) {
			sender.sendMessage(StringFormatter.error("Dem Spectator " + name + " kann keine Belohnung ausgezahlt werden"));
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
			rewardManager.addReward(name, new Reward(amount, 0), reason);
			break;
		case "points":
			rewardManager.addReward(name, new Reward(0, amount), reason);
			break;
		default:
			sender.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
			return false;
		}
		sender.sendMessage(ChatColor.GREEN + "Transaktion erfolgreich ausgeführt");
		return true;
	}

	private boolean doReverse(CommandSender sender, String[] args) {
		if(args.length < 3) {
			sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		int transactionId;
		try {
			transactionId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Parameter Id muss dem Typ Int entsprechen"));
			return false;
		}
		ArrayList<String> reasonWords = new ArrayList<>(Arrays.asList(args));
		reasonWords.remove(0); // revert
		reasonWords.remove(0); // Id
		String reason = String.join(" ", reasonWords);
		if(!transactionDbConnection.existsTransaction(transactionId)) {
			sender.sendMessage(StringFormatter.error("Diese Transaktion ist nicht registriert"));
			return false;
		}
		TransactionJson transactionJson = transactionDbConnection.getTransaction(transactionId);
		rewardManager.addReward(
				transactionJson.getPlayerName(), 
				new Reward(
					-transactionJson.getTransactedDiamonds(), 
					-transactionJson.getTransactedPoints()
				), 
				"Die Transaktion " + transactionId + " wurde rückgängig gemacht, weil: " + reason);
		sender.sendMessage(ChatColor.GREEN + "Transaktion erfolgreich rückgängig gemacht");
		return true;
	}
}
