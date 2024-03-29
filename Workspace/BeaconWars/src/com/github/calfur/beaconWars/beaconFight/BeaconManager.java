package com.github.calfur.beaconWars.beaconFight;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.PlayerKicker;
import com.github.calfur.beaconWars.configuration.ConstantConfiguration;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;
import com.github.calfur.beaconWars.pvp.Team;

public class BeaconManager {

	public static Location getBeaconLocationByPlayer(Player player) {
		PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
		String name = player.getName();
		if(playerDbConnection.existsPlayer(name) && !playerDbConnection.isPlayerSpectator(name)) {			
			PlayerJson playerJson = playerDbConnection.getPlayer(name);
			return Main.getInstance().getTeamDbConnection().getTeam(playerJson.getTeamId()).getBeaconLocation();
		}
		return null;
	}
	
	public static void placeLevelOneBeacon(Location location) {
		World world = location.getWorld();
		int beaconX = location.getBlockX();
		int beaconY = location.getBlockY();
		int beaconZ = location.getBlockZ();
		
		replaceBlock(world, beaconX, beaconY, beaconZ, Material.BEACON);
		
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {				
				replaceBlock(world, beaconX + x, beaconY - 1, beaconZ + z, Material.NETHERITE_BLOCK);
			}
		}
	}
	
	public static void removeLevelOneBeacon(Location location) {
		World world = location.getWorld();
		int beaconX = location.getBlockX();
		int beaconY = location.getBlockY();
		int beaconZ = location.getBlockZ();
		
		replaceBlock(world, beaconX, beaconY, beaconZ, Material.AIR);
		
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {				
				replaceBlock(world, beaconX + x, beaconY - 1, beaconZ + z, Material.AIR);
			}
		}
	}
	
	public static void replaceBlock(World world, int x, int y, int z, Material material) {
		Block block = new Location(world, x, y, z).getBlock();
		block.setType(material);
	}
	
	/**
	 * 
	 * @param beaconLocation
	 * @return Team with same beaconLocation, or null if there is no team which matches
	 */
	public static Team getTeamByBeaconLocation(Location beaconLocation) {
		HashMap<String, TeamJson> teams = Main.getInstance().getTeamDbConnection().getTeams();
		for (Entry<String, TeamJson> team : teams.entrySet()) {
			if(team.getValue().getBeaconLocation().equals(beaconLocation)) {
				int teamId = Integer.parseInt(team.getKey());
				return new Team(teamId, team.getValue().getColor());
			}
		}
		return null;
	}

	public static boolean isBeaconFromAnotherTeam(Player player, Location location) {
		if(!Main.getInstance().getPlayerDbConnection().existsPlayer(player.getName())) {			
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(player));	
			return false;
		}
		int teamIdOfPlayer = Main.getInstance().getPlayerDbConnection().getPlayer(player.getName()).getTeamId();
		if(teamIdOfPlayer == ConstantConfiguration.spectatorTeamNumber) {
			player.sendMessage(StringFormatter.error("Du bist Spectator und kannst darum keinen Beacon abbauen"));
			return false;
		}
		int teamIdOfBeacon;
		try {			
			teamIdOfBeacon = getTeamByBeaconLocation(location).getId();
		}catch(Exception e) {
			player.sendMessage(StringFormatter.error("Error, unregistrierter Beacon"));
			return false;
		}
		if(teamIdOfBeacon == teamIdOfPlayer) {
			player.sendMessage(StringFormatter.error("Du kannst deinen eigenen Beacon nicht abbauen"));
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param player
	 * @return true if beacon was removed, false if not
	 */
	public static boolean removeOneBeaconFromInventory(Player player) {
		Inventory inventory = player.getInventory();
		if(!inventory.contains(Material.BEACON)) {			
			return false;
		}
		inventory.removeItem(new ItemStack(Material.BEACON, 1));	
		return true;
	}

	public static boolean removeBeaconsFromInventory(Player leaver) {
		boolean result = false;
		while(removeOneBeaconFromInventory(leaver)){
			result = true;
		}
		return result;
	}

	public static void teleportPlayerToBeacon(Player player) {
		Location beaconLocation = getBeaconLocationByPlayer(player);
		if(beaconLocation == null) { // spectator team
			return;
		}
		Location teleportLocation = new Location(beaconLocation.getWorld(), beaconLocation.getX() + 1.5, beaconLocation.getY(), beaconLocation.getZ() + 0.5);	
		replaceBlock(beaconLocation.getWorld(), beaconLocation.getBlockX() + 1, beaconLocation.getBlockY(), beaconLocation.getBlockZ(), Material.AIR);
		replaceBlock(beaconLocation.getWorld(), beaconLocation.getBlockX() + 1, beaconLocation.getBlockY() + 1, beaconLocation.getBlockZ(), Material.AIR);
		player.teleport(teleportLocation);
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 3));
	}

	public static void teleportAllOnlinePlayersToBeacon() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			BeaconManager.teleportPlayerToBeacon(player);
		}
	}

	public static void setBeaconAsRespawnLocation(Player player) {
		Location beaconLocation = getBeaconLocationByPlayer(player);
		if(beaconLocation == null) { // spectator team
			return;
		}
		Location respawnLocation = new Location(beaconLocation.getWorld(), beaconLocation.getX() + 1.5, beaconLocation.getY(), beaconLocation.getZ() + 0.5);
		player.setBedSpawnLocation(respawnLocation, true);
		
	}
}
