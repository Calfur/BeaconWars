package com.github.calfur.beaconWars.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class TabCompleterBeaconEvent extends TabCompleterBase{

	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		if(sender.hasPermission("Admin")) {
			switch (previousParameters.length) {
			case 1:
				completions.add("delete");
				completions.add("add");
				break;
			case 2: // DateTime (yyyy-MM-dd_HH:mm)
				if(previousParameters[0].equals("add")) {
					completions.add(LocalDateTime.now().plusHours(2).withMinute(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")));
					completions.add(LocalDateTime.now().plusHours(1).withMinute(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")));
					completions.add(LocalDateTime.now().plusHours(1).withMinute(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")));
				}
				break;
			case 3: // Eventduration (Minutes)
				if(previousParameters[0].equals("add")) {
					completions.add("90");
				}
				break;
			case 4: // Attackduration (Minutes)
				if(previousParameters[0].equals("add")) {
					completions.add("12");
				}
				break;
			}
		}
		return completions;
	}

}
