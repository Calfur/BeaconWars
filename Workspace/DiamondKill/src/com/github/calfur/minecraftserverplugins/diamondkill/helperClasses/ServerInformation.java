package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;

public class ServerInformation {

	public static List<String> getPlayerNamesFromOnlinePlayers() {
		List<String> names = new ArrayList<String>();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
		for (Player player : players) {
			if(!playerDbConnection.isPlayerSpectator(player.getName())){				
				names.add(player.getName());
			}
		}
		return names;
	}
}
