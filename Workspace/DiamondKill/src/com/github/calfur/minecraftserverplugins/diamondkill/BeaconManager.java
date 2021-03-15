package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;

import net.md_5.bungee.api.ChatColor;

public class BeaconManager {

	public static Location getBeaconLocationByPlayer(Player player) {
		return Main.getInstance().getTeamDbConnection().getTeam(Main.getInstance().getPlayerDbConnection().getPlayer(player.getName()).getTeamId()).getBeaconPosition();
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
		Block block = world.getBlockAt(new Location(world, x, y, z));
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
			if(team.getValue().getBeaconPosition().equals(beaconLocation)) {
				int teamId = Integer.parseInt(team.getKey());
				return new Team(teamId, team.getValue().getColor());
			}
		}
		return null;
	}

	public static boolean isBeaconFromAnotherTeam(Player player, Location location) {
		int teamIdOfBeacon;
		try {			
			teamIdOfBeacon = getTeamByBeaconLocation(location).getId();
		}catch(Exception e) {
			player.sendMessage(ChatColor.DARK_RED + "Error, unregistrierter Beacon");
			return false;
		}
		int teamIdOfPlayer = Main.getInstance().getPlayerDbConnection().getPlayer(player.getName()).getTeamId();
		if(teamIdOfBeacon != teamIdOfPlayer) {
			return true;
		}
		return false;
	}
}
