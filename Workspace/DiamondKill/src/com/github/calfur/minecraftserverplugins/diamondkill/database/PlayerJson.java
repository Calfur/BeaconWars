package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class PlayerJson implements ConfigurationSerializable {
	private int team;	
	private String discordName;
	private int collectableDiamonds;
	
	public String getDiscordName() {
		return discordName;
	}

	public int getTeamId() {
		return team;
	}
	
	public int getCollectableDiamonds() {
		return collectableDiamonds;
	}
	
	public PlayerJson(int team, String discordName, int collectableDiamonds) {
		this.team = team;
		this.discordName = discordName;
		this.collectableDiamonds = collectableDiamonds;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("team", this.team);
		data.put("discordName", this.discordName);
		data.put("collectableDiamonds", this.collectableDiamonds);
		return data;
	}
	
	public static PlayerJson deserialize(Map<String, Object> args) {
		return new PlayerJson(
				NumberConversions.toInt(args.get("team")), 
				(String) args.get("discordName"), 
				NumberConversions.toInt(args.get("collectableDiamonds"))
			);
	}
}
