package com.github.calfur.beaconWars.hungerGamesLootDrop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.configuration.IConfiguration;

public class ItemSpawnAnnouncer implements Runnable{
	IConfiguration configuration = Main.getInstance().getConfiguration();
	
	@Override
	public void run() {
		if(configuration.areHungerGamesEnabled()) {	
			Bukkit.broadcastMessage(ChatColor.GOLD + "In 5 Minuten spawnt in der Mitte ein " + ChatColor.AQUA + "Diamant" + ChatColor.GOLD + ", falls mindestens " + ItemSpawner.minimumOfRequiredTeams + " Teams online sind");
	
		}
	}
}
