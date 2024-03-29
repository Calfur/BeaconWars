package com.github.calfur.beaconWars.helperClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.database.PlayerDbConnection;

public class ServerInformation {

	public static List<String> getPlayerNamesFromOnlinePlayers() {
		List<String> names = new ArrayList<String>();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
		for (Player player : players) {
			String name = player.getName();
			if(playerDbConnection.existsPlayer(name)) {
				if(!playerDbConnection.isPlayerSpectator(name)){				
					names.add(name);
				}				
			}else {
				names.add(name);
			}
			
		}
		return names;
	}
}
