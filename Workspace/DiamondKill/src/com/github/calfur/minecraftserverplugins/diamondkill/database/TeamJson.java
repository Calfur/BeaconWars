package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class TeamJson implements ConfigurationSerializable {
	private ChatColor color;
	private Location beaconLocation;
	private String teamLeader;

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public Location getBeaconPosition() {
		return beaconLocation;
	}

	public void setBeaconPosition(Location beaconPosition) {
		this.beaconLocation = beaconPosition;
	}

	public String getTeamLeader() {
		return teamLeader;
	}

	public void setTeamLeader(String teamLeader) {
		this.teamLeader = teamLeader;
	}
	
	public TeamJson(ChatColor color, String teamLeader, Location beaconLocation) {
		super();
		this.teamLeader = teamLeader;
		this.color = color;
		this.beaconLocation = beaconLocation;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("color", this.color);
		data.put("teamLeader", this.teamLeader);
		data.put("beaconLocation_world", this.beaconLocation.getWorld().getName());
		data.put("beaconLocation_x", this.beaconLocation.getX());
		data.put("beaconLocation_y", this.beaconLocation.getY());
		data.put("beaconLocation_z", this.beaconLocation.getZ());
		return data;
	}
	
	public static TeamJson deserialize(Map<String, Object> args) {
		Location location = new Location(
				Bukkit.getWorld((String) args.get("beaconLocation_world")), 
				NumberConversions.toInt(args.get("beaconLocation_x")), 
				NumberConversions.toInt(args.get("beaconLocation_y")), 
				NumberConversions.toInt(args.get("beaconLocation_z"))
			);
		return new TeamJson(
				ChatColor.valueOf((String) args.get("color")),
				(String) args.get("teamLeader"),
				location
			);
	}
}
