package com.github.calfur.beaconWars.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.helperClasses.ServerInformation;

public class TabCompleterStartProjectForPlayers extends TabCompleterBase {

	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		switch (previousParameters.length) {
		case 1:
			if(sender.hasPermission("Admin")) {	
				completions.addAll(ServerInformation.getPlayerNamesFromOnlinePlayers());
			}
			break;
		}
		return completions;
	}
	
}
