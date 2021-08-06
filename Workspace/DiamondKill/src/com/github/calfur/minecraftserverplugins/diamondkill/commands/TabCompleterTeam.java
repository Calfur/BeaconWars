package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;

public class TabCompleterTeam extends TabCompleterBase {

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
		case 2: // Teamnumber
			switch (previousParameters[0]) {
			case "add":
				if(!sender.hasPermission("Admin")) {
					return completions;
				}
				completions.add("[Teamnummer]");
				break;
			case "edit":
			case "delete":
				if(!sender.hasPermission("Admin")) {
					return completions;
				}
			case "info":	
				for (Entry<String, TeamJson> team : teams.entrySet()) {
					completions.add(team.getKey());
				}
				break;
			case "list":
				return completions;
			default:
				break;
			}
			break;
		case 3: // Color
			if(!sender.hasPermission("Admin") 
					|| (!previousParameters[0].equalsIgnoreCase("add") && !previousParameters[0].equalsIgnoreCase("edit"))) {
				return completions;
			}
			completions.add(ChatColor.AQUA.name());
			completions.add(ChatColor.BLUE.name());
			completions.add(ChatColor.DARK_AQUA.name());
			completions.add(ChatColor.DARK_BLUE.name());
			completions.add(ChatColor.DARK_GREEN.name());
			completions.add(ChatColor.DARK_PURPLE.name());
			completions.add(ChatColor.DARK_RED.name());
			completions.add(ChatColor.GREEN.name());
			completions.add(ChatColor.LIGHT_PURPLE.name());
			completions.add(ChatColor.RED.name());
			completions.add(ChatColor.WHITE.name());
			completions.add(ChatColor.YELLOW.name());
			break;
		case 4: // Beacon_x
			if(!sender.hasPermission("Admin") 
					|| (!previousParameters[0].equalsIgnoreCase("add") && !previousParameters[0].equalsIgnoreCase("edit"))) {
				return completions;
			}
			completions.add("[Beacon_x]");
			break;
		case 5: // Beacon_y
			if(!sender.hasPermission("Admin") 
					|| (!previousParameters[0].equalsIgnoreCase("add") && !previousParameters[0].equalsIgnoreCase("edit"))) {
				return completions;
			}
			completions.add("[Beacon_y]");
			break;
		case 6: // Beacon_z
			if(!sender.hasPermission("Admin") 
					|| (!previousParameters[0].equalsIgnoreCase("add") && !previousParameters[0].equalsIgnoreCase("edit"))) {
				return completions;
			}
			completions.add("[Beacon_z]");
			break;
		case 7: // TeamLeader
			if(!sender.hasPermission("Admin") 
					|| (!previousParameters[0].equalsIgnoreCase("add") && !previousParameters[0].equalsIgnoreCase("edit"))) {
				return completions;
			}
			for (Entry<String, PlayerJson> player : players.entrySet()) {
				completions.add(player.getKey());
			}
			break;
		default:
			break;
		}
		return completions;
	}

}
