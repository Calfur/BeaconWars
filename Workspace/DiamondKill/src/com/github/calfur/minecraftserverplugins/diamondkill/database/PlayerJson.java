package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class PlayerJson implements ConfigurationSerializable {
	private int team;	
	private String realName;
	private int collectableDiamonds;
	
	public String getRealName() {
		return realName;
	}

	public int getTeamId() {
		return team;
	}

	public int getCollectableDiamonds() {
		return collectableDiamonds;
	}
	
	public void addCollectableDiamonds(int amount) {
		collectableDiamonds += amount;		
	}
	
	public void removeCollectableDiamonds(int amount) {
		collectableDiamonds -= amount;		
	}
	
	public PlayerJson(int team, String realName, int collectableDiamonds) {
		this.team = team;
		this.realName = realName;
		this.collectableDiamonds = collectableDiamonds;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("team", this.team);
		data.put("realName", this.realName);
		data.put("collectableDiamonds", this.collectableDiamonds);
		return data;
	}
	
	public static PlayerJson deserialize(Map<String, Object> args) {
		return new PlayerJson(
				NumberConversions.toInt(args.get("team")), 
				(String) args.get("realName"), 
				NumberConversions.toInt(args.get("collectableDiamonds"))
			);
	}
}
