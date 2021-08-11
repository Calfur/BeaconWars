package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
						return sendTransactionInfo(executor, args);
					case "list":
						return sendTransactionList(executor, args);
					default:
						executor.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendTransactionList(Player executor, String[] args) {
		if(args.length != 1) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		Map<String, TransactionJson> transactions = transactionDbConnection.getTransactions();
		executor.sendMessage(ChatColor.BOLD + "" + transactions.size() + " Transaktionen gefunden:");
		HashMap<String, TeamJson> teams = teamDbConnection.getTeams();
		for (Entry<String, TransactionJson> transaction : transactions.entrySet()) {
			TransactionJson transactionJson = transaction.getValue();
			TeamJson team = teams.get(Integer.toString(transactionJson.getTeam()));
			ChatColor teamColor = team.getColor();
			executor.sendMessage( 
					ChatColor.GOLD + "Id: " + ChatColor.RESET + transaction.getKey() + 
					ChatColor.GOLD + ", Zeit: " + ChatColor.RESET + transactionJson.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")) + 
					ChatColor.GOLD + ", Spieler: " + teamColor + transactionJson.getPlayerName() + ChatColor.RESET + 
					ChatColor.GOLD + ", Diamanten: " + ChatColor.AQUA + transactionJson.getTransactedDiamonds() + ChatColor.RESET + 
					ChatColor.GOLD + ", Punkte: " + ChatColor.RESET + transactionJson.getTransactedPoints());
		}
		return true;
	}

	private boolean sendTransactionInfo(Player executor, String[] args) {
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
		executor.sendMessage(ChatColor.GOLD + "Diamanten: " + ChatColor.AQUA + transactionJson.getTransactedDiamonds());
		executor.sendMessage(ChatColor.GOLD + "Punkte: " + ChatColor.RESET + transactionJson.getTransactedPoints());
		executor.sendMessage(ChatColor.GOLD + "Grund: " + ChatColor.RESET + transactionJson.getReason());
		return true;
	}
}
