package com.github.calfur.minecraftserverplugins.diamondkill.hungerGamesLootDrop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ItemSpawnAnnouncer implements Runnable{

	@Override
	public void run() {
		Bukkit.broadcastMessage(ChatColor.GOLD + "In 5 Minuten spawnt in der Mitte ein " + ChatColor.AQUA + "Diamant" + ChatColor.GOLD + ", falls mindestens " + ItemSpawner.minimumOfRequiredTeams + " Teams online sind");
	}

}
