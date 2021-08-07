package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class TabCompleterNameNametag extends TabCompleterBase {

	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		switch (previousParameters.length) {
		case 1:
			completions.add("[name]");
			break;
		}
		return completions;
	}

}
