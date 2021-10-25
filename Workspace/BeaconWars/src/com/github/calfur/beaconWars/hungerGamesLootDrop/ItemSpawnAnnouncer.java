package com.github.calfur.beaconWars.hungerGamesLootDrop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.commands.CommandProjectStart;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class ItemSpawnAnnouncer implements Runnable{
	IConfiguration configuration = Main.getInstance().getConfiguration();
	private CommandProjectStart commandProjectStart = Main.getInstance().getCommandProjectStart();
	
	@Override
	public void run() {
		if(configuration.areHungerGamesEnabled() && commandProjectStart.isProjectActive()) {	
			Bukkit.broadcastMessage(ChatColor.GOLD + "In 5 Minuten spawnt in der Mitte ein " + ChatColor.AQUA + "Diamant" + ChatColor.GOLD + ", falls mindestens " + StringFormatter.teamWord(configuration.getHungerGamesMinimumAmountOfRequiredOnlineTeams()) + " online " + StringFormatter.singularOrPlural(configuration.getHungerGamesMinimumAmountOfRequiredOnlineTeams(), "ist.", "sind."));
			Bukkit.broadcastMessage(ChatColor.GOLD + "Benutze den Befehl " + ChatColor.WHITE + "/compass spawn" + ChatColor.GOLD + " um einen Kompass dorthin zu bekommen");
		}
	}
}
