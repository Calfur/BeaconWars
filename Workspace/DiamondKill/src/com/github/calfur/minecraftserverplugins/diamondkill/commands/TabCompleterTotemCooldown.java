package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.ServerInformation;

public class TabCompleterTotemCooldown extends TabCompleterBase {

	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		switch (previousParameters.length) {
		case 1:
			completions.addAll(ServerInformation.getPlayerNamesFromOnlinePlayers());
			break;
		}
		return completions;
	}
	
}
