package com.github.calfur.beaconWars.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.database.KillDbConnection;
import com.github.calfur.beaconWars.database.KillJson;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;

public class TabCompleterPlayerKill extends TabCompleterBase {

	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection();
	
	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		HashMap<String, PlayerJson> players = playerDbConnection.getPlayers();
		HashMap<String, KillJson> kills = killDbConnection.getKills();
		
		switch (previousParameters.length) {
		case 1:
			completions.add("info");
			completions.add("list");
			if(sender.hasPermission("Admin")) {				
				completions.add("add");
				completions.add("delete");
			}
			break;
		case 2: // Id | Killer | Page
			switch (previousParameters[0]) {
			case "add": // Killer
				if(!sender.hasPermission("Admin")) {
					break;
				}
				for (Entry<String, PlayerJson> player : players.entrySet()) {
					String name = player.getKey();
					completions.add(name);
				}
				break;
			case "delete": // Id
				if(!sender.hasPermission("Admin")) {
					break;
				}
			case "info":
				for (Entry<String, KillJson> kill : kills.entrySet()) {
					completions.add(kill.getKey());
				}
				break;
			case "list":
				int lastAvailablePage = killDbConnection.getAmountOfAvailablePages();
				for(int i = 1; i <= lastAvailablePage; i++) {
					completions.add(Integer.toString(i));
				}
				break;
			}
			break;
		case 3: // Victim
			if(!sender.hasPermission("Admin") 
					|| !previousParameters[0].equalsIgnoreCase("add")) {
				break;
			}
			for (Entry<String, PlayerJson> player : players.entrySet()) {
				String name = player.getKey();
				if(!name.equalsIgnoreCase(previousParameters[1])) {					
					completions.add(name);
				}
			}
			break;
		case  4: // Reason
			if(!sender.hasPermission("Admin") 
					|| !previousParameters[0].equalsIgnoreCase("add")) {
				break;
			}
			completions.add("[Grund]");
			break;
		}
		return completions;
	}

}
