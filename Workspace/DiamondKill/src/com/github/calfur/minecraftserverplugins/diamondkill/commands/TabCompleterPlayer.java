package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.ServerInformation;


public class TabCompleterPlayer extends TabCompleterBase {

	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		HashMap<String, PlayerJson> players = playerDbConnection.getPlayers();
		HashMap<String, TeamJson> teams = teamDbConnection.getTeams();
		
		switch (previousParameters.length) {
		case 1:
			completions.add("info");
			completions.add("list");
			if(sender.hasPermission("Admin")) {				
				completions.add("add");
				completions.add("edit");
				completions.add("delete");
			}
			break;
		case 2:
			switch (previousParameters[0]) {
			case "add":
				if(sender.hasPermission("Admin")) {	
					completions.addAll(ServerInformation.getPlayerNamesFromOnlinePlayers());
				}
				break;
			case "info":	
				for (Entry<String, PlayerJson> player : players.entrySet()) {
					completions.add(player.getKey());
				}
				break;
			case "edit":
			case "delete":
				if(sender.hasPermission("Admin")) {		
					for (Entry<String, PlayerJson> player : players.entrySet()) {
						completions.add(player.getKey());
					}
				}
				break;
			case "list":
				break;
			default:
				break;
			}
			break;
		case 3:
			if(sender.hasPermission("Admin")) {	
				switch (previousParameters[0]) {
				case "add":
				case "edit":
						for (Entry<String, TeamJson> team : teams.entrySet()) {
							completions.add(team.getKey());
						}
					break;
				}
			}
			break;
		case 4:
			if(sender.hasPermission("Admin")) {	 
				completions.add("[RealName]");
			}
			break;
		default:
			break;
		}
		return completions;
	}
	
}
