package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class TeamJson implements ConfigurationSerializable {
	private ChatColor color;
	private Location beaconPosition;

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public Location getBeaconPosition() {
		return beaconPosition;
	}

	public void setBeaconPosition(Location beaconPosition) {
		this.beaconPosition = beaconPosition;
	}
	
	public TeamJson(ChatColor color, Location beaconPosition) {
		super();
		this.color = color;
		this.beaconPosition = beaconPosition;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("color", this.color);
		data.put("collectableDiamonds", this.beaconPosition);
		return data;
	}
	
	public static TeamJson deserialize(Map<String, Object> args) {
		return new TeamJson(
				(ChatColor) args.get("team"),
				(Location) args.get("beaconPosition")
			);
	}
}
