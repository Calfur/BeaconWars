package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandTransaction implements CommandExecutor {

	private TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
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
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		sendTransactionList(executor, page);
		return true;
	}

	private void sendTransactionList(Player executor, int page) {
		executor.sendMessage(ChatColor.BOLD + "Seite " + page + " von " + transactionDbConnection.getAmountOfAvailablePages() + ":");
		HashMap<String, TeamJson> teams = teamDbConnection.getTeams();

		int firstTransactionToShow = page * 10 - 9;
		int lastTransactionToShow = page * 10;
		for (int i = firstTransactionToShow; i <= lastTransactionToShow; i++) {
			if(transactionDbConnection.existsTransaction(i)) {				
				TransactionJson transactionJson = transactionDbConnection.getTransaction(i);
				TeamJson team = teams.get(Integer.toString(transactionJson.getTeam()));
				ChatColor teamColor = ChatColor.WHITE;
				if(team != null) {				
					teamColor = team.getColor();
				}
				executor.sendMessage( 
						ChatColor.GOLD + "Id: " + ChatColor.RESET + i + 
						ChatColor.GOLD + ", Zeit: " + ChatColor.RESET + transactionJson.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")) + 
						ChatColor.GOLD + ", Spieler: " + teamColor + transactionJson.getPlayerName() + ChatColor.RESET + 
						ChatColor.GOLD + ", Diamanten: " + ChatColor.AQUA + transactionJson.getTransactedDiamonds() + ChatColor.RESET + 
						ChatColor.GOLD + ", Punkte: " + ChatColor.RESET + transactionJson.getTransactedPoints());
			}
		}
	}

	private boolean doTransactionInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
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
}
