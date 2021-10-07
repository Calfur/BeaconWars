package com.github.calfur.beaconWars.hungerGamesLootDrop;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;

public class ItemSpawner implements Runnable {
	IConfiguration configuration = Main.getInstance().getConfiguration();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private ItemStack itemsToSpawn = new ItemStack(Material.DIAMOND, 1);
	public static final int minimumOfRequiredTeams = 2;

	@Override
	public void run() {
		if(configuration.areHungerGamesEnabled()) {			
			int amountOfOnlineTeams = getCurrentAmountOfOnlineTeams();
			if(amountOfOnlineTeams >= minimumOfRequiredTeams) {
				Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------------------------------------");
				Bukkit.broadcastMessage(ChatColor.GOLD + "Ein " + ChatColor.AQUA + "Diamant" + ChatColor.GOLD + " ist in der Mitte gespawn! Kämpft um ihn!");
				Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------------------------------------");
				configuration.getHungerGamesLocation().getWorld().dropItem(configuration.getHungerGamesLocation(), itemsToSpawn);
			}else {
				Bukkit.broadcastMessage(ChatColor.GOLD + "Kein " + ChatColor.AQUA + "Diamant" + ChatColor.GOLD + " gespawnt, es sind nur " + ChatColor.RESET + amountOfOnlineTeams + ChatColor.GOLD + " von " + ChatColor.RESET + minimumOfRequiredTeams + ChatColor.GOLD + " benötigten Teams online");
			}
		}
	}

	private int getCurrentAmountOfOnlineTeams() {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		HashSet<Integer> onlineTeams = new HashSet<Integer>();
		for (Player player : onlinePlayers) {
			PlayerJson playerJson = playerDbConnection.getPlayer(player.getName());
			if(playerJson != null) {
				int teamId = playerJson.getTeamId();
				if(teamId != TeamDbConnection.spectatorTeamNumber) {					
					onlineTeams.add(teamId);
				}
			}
		}
		int amountOfOnlineTeams = onlineTeams.size();
		return amountOfOnlineTeams;
	}

}
