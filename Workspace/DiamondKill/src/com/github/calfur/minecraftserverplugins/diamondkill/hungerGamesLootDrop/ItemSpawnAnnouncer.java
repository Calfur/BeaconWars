package com.github.calfur.minecraftserverplugins.diamondkill.hungerGamesLootDrop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemSpawnAnnouncer extends BukkitRunnable{

	@Override
	public void run() {
		Bukkit.broadcastMessage(ChatColor.GOLD + "In 5 Minuten spawnt in der Mitte ein " + ChatColor.AQUA + "Diamant" + ChatColor.GOLD + ", falls mindestens " + ItemSpawner.minimumOfRequiredTeams + " Teams online sind");
	}

}
