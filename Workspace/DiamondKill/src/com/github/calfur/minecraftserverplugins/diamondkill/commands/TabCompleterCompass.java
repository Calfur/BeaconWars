package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;

public class TabCompleterCompass extends TabCompleterBase {

	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		HashMap<String, TeamJson> teams = teamDbConnection.getTeams();
		
		switch (previousParameters.length) {
		case 1:
			completions.add("Base");
			completions.add("Beacon");
			completions.add("Spawn");
			break;
		case 2: // TeamNumber
			switch (previousParameters[0]) {
			case "Base":
			case "Beacon":
				for (Entry<String, TeamJson> team : teams.entrySet()) {
					completions.add(team.getKey());
				}
				break;
			}
			break;
		}
		return completions;
	}

}
