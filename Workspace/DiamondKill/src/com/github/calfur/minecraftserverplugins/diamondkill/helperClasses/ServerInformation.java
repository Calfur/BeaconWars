package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerInformation {

	public static List<String> getPlayerNamesFromOnlinePlayers() {
		List<String> names = new ArrayList<String>();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			names.add(player.getName());
		}
		return names;
	}
}
