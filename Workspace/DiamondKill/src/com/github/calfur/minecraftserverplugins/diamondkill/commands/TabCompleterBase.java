package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class TabCompleterBase implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(sender instanceof Player) {			
			List<String> completions = getSuggestions(args, (Player)sender);
			List<String> filteredCompletions = filterMatchingWords(completions, args[args.length-1]);
			return filteredCompletions;
		}
		return null;
	}

	abstract List<String> getSuggestions(String[] previousParameters, Player sender);
	
	private List<String> filterMatchingWords(List<String> words, String filter){
		List<String> filteredWords = new ArrayList<String>();
		for (String word : words) {
			if(word.toLowerCase().startsWith(filter.toLowerCase())) {
				filteredWords.add(word);
			}
		}
		return filteredWords;
	}
}
