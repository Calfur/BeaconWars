package com.github.calfur.beaconWars;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;

import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.helperClasses.InventoryManagement;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;
import com.github.calfur.beaconWars.hungerGamesLootDrop.HungerGamesManager;

public class CompassManager {

	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	
	public void addCompassToInventory(Player player, CompassType compassType) {
		switch (compassType) {
		case spawn:			
			addCompassToInventory(player, HungerGamesManager.spawnLocation, ChatColor.RESET + "Spawn");
			break;
		default:
			player.sendMessage(StringFormatter.error("Unerwarteter Fehler beim ausführen des Commands"));
			break;
		}	
		return;
	}

	public void addCompassToInventory(Player player, CompassType compassType, int teamNumber) {
		if(!teamDbConnection.existsTeam(teamNumber) || TeamDbConnection.spectatorTeamNumber == teamNumber) {				
			player.sendMessage(StringFormatter.error("Ein Team mit der Nummer ") + teamNumber + StringFormatter.error(" existiert nicht"));
			return;
		}
		TeamJson targetTeamJson = teamDbConnection.getTeam(teamNumber);
		switch (compassType) {
		case base:
			Location targetLocation = targetTeamJson.getBeaconLocation();
			addCompassToInventory(player, targetLocation, ChatColor.RESET + "Base " + targetTeamJson.getColor() + "Team " + teamNumber);
			break;
		case beacon:
			addBeaconCompassToInventory(player, teamNumber, targetTeamJson);
			break;
		default:
			player.sendMessage(StringFormatter.error("Unerwarteter Fehler beim ausführen des Commands"));
			break;
		}
	}
	
	private void addBeaconCompassToInventory(Player player, int teamNumber, TeamJson targetTeamJson) {
		Location targetLocation = getBeaconLocation(teamNumber, targetTeamJson);
		addCompassToInventory(player, targetLocation, ChatColor.RESET + "Beacon " + targetTeamJson.getColor() + "Team " + teamNumber);
		player.sendMessage("Rechtsklicke mit dem Kompass in der Hand, um ihn zu aktualisieren");
	}

	private Location getBeaconLocation(int teamNumber, TeamJson targetTeamJson) {
		if(beaconFightManager.isBeaconEventActive()) {
			Player beaconDestructor = beaconFightManager.getOngoingBeaconFight().getAttackerOfBeaconRaidByDefenderTeam(teamNumber);
			if(beaconDestructor != null) {
				return beaconDestructor.getLocation();
			}
		}
		return targetTeamJson.getBeaconLocation();
	}

	public void addCompassToInventory(Player player, Location targetLocation, String itemName) {
		PlayerInventory inventory = player.getInventory();
		if(InventoryManagement.isInventoryFull(inventory)) {
			player.sendMessage(StringFormatter.error("Keinen freien Inventar slot gefunden"));
			return;
		}
		
		ItemStack itemStack = getCompass(targetLocation, itemName);
		inventory.addItem(itemStack);
		player.sendMessage(ChatColor.GREEN + "Kompass zu deinem Inventar hinzugefügt");
	}
	
	public void updateCompass(Player player, ItemStack compass) {
		CompassMeta meta = (CompassMeta) compass.getItemMeta();
		String itemName = meta.getDisplayName();
		
		HashMap<String, TeamJson> teams = teamDbConnection.getTeams();
		for (Entry<String, TeamJson> team : teams.entrySet()) {			
			if(itemName.equals("Beacon " + team.getValue().getColor() + "Team " + team.getKey())) {	
				int teamNumber;
				try{					
					teamNumber = Integer.parseInt(team.getKey());
				}catch(Exception e){
					return;
				}
				Location targetLocation = getBeaconLocation(teamNumber, team.getValue());
				meta.setLodestone(targetLocation);
				compass.setItemMeta(meta);
				player.sendMessage(ChatColor.GREEN + "Kompass aktualisiert");
				return;
			}
		}
	}

	private ItemStack getCompass(Location targetLocation, String itemName) {
		ItemStack itemStack = getLodstoneCompass(targetLocation);
		InventoryManagement.renameItem(itemStack, itemName);
		return itemStack;
	}
	
	private ItemStack getLodstoneCompass(Location targetLocation) {
		ItemStack itemStack = new ItemStack(Material.COMPASS, 1);
		CompassMeta meta = (CompassMeta) itemStack.getItemMeta();
		meta.setLodestoneTracked(false);
		meta.setLodestone(targetLocation);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public enum CompassType{
		spawn,
		base,
		beacon
	}

}
