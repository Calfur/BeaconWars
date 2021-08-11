package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionJson;

public class TabCompleterTransaction extends TabCompleterBase {
	
	TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	
	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		HashMap<String, TransactionJson> transactions = transactionDbConnection.getTransactions();
		
		switch (previousParameters.length) {
		case 1:
			completions.add("info");
			completions.add("list");
			break;
		case 2: // TransactionId
			switch (previousParameters[0]) {
			case "info":
				for (Entry<String, TransactionJson> transaction : transactions.entrySet()) {
					completions.add(transaction.getKey());
				}
				break;
			}
			break;
		}
		return completions;
	}
}
