package com.github.calfur.beaconWars.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TransactionDbConnection;
import com.github.calfur.beaconWars.database.TransactionJson;

public class TabCompleterTransaction extends TabCompleterBase {
	
	private TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	
	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		HashMap<String, TransactionJson> transactions = transactionDbConnection.getTransactions();
		HashMap<String, PlayerJson> players = playerDbConnection.getPlayers();
		
		switch (previousParameters.length) {
		case 1:
			completions.add("info");
			completions.add("list");
			if(sender.hasPermission("Admin")) {	 
				completions.add("add");
				completions.add("reverse");
			}
			break;
		case 2: // Id | page | diamonds | points
			switch (previousParameters[0]) {
			case "info":
				for (Entry<String, TransactionJson> transaction : transactions.entrySet()) {
					completions.add(transaction.getKey());
				}
				break;
			case "list":
				int lastAvailablePage = transactionDbConnection.getAmountOfAvailablePages();
				for(int i = 1; i <= lastAvailablePage; i++) {
					completions.add(Integer.toString(i));
				}
				break;
			case "add":
				if(sender.hasPermission("Admin")) {	
					completions.add("diamonds");
					completions.add("points");
				}
				break;
			case "reverse":
				if(sender.hasPermission("Admin")) {	
					for (Entry<String, TransactionJson> transaction : transactions.entrySet()) {
						completions.add(transaction.getKey());
					}
				}
				break;
			}
			break;
		case 3: // amount
			if(sender.hasPermission("Admin")) {	 
				switch (previousParameters[0]) {
				case "add":					
					switch (previousParameters[1]) {
					case "diamonds":
						completions.add("-3");
						completions.add("-1");
						completions.add("1");
						completions.add("3");
						break;
					case "points":
						completions.add("-300");
						completions.add("-100");
						completions.add("100");
						completions.add("300");
						break;
					}			
					break;
				case "reverse":
					completions.add("[reason]");
					break;
				}
			}
			break;
		case 4: // player
			if(!sender.hasPermission("Admin") 
					|| !previousParameters[0].equalsIgnoreCase("add")) {
				break;
			}	
			for (Entry<String, PlayerJson> player : players.entrySet()) {
				completions.add(player.getKey());
			}
			break;
		case 5: // reason
			if(!sender.hasPermission("Admin") 
					|| !previousParameters[0].equalsIgnoreCase("add")) {
				break;
			}	
			completions.add("[reason]");
			break;
		}
		return completions;
	}
}
