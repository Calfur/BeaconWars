package com.github.calfur.minecraftserverplugins.diamondkill.commands;

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
			return completions;
		}
		return null;
	}

	abstract List<String> getSuggestions(String[] previousParameters, Player player);
	
}
