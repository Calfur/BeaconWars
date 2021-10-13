package com.github.calfur.beaconWars.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class PlayerJson implements ConfigurationSerializable {
	private int team;	
	private String realName;
	private int collectableDiamonds;
	private boolean isBuildModeActive = false;
	private LocalDateTime buildModeCooldownEnd = LocalDateTime.now();
	
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
	
	public boolean isBuildModeActive() {
		return isBuildModeActive;
	}

	public void setBuildModeActive(boolean isBuildModeActive) {
		this.isBuildModeActive = isBuildModeActive;
	}

	public LocalDateTime getBuildModeCooldownEnd() {
		return buildModeCooldownEnd;
	}

	public void setBuildModeCooldownEnd(LocalDateTime buildModeCooldownEnd) {
		this.buildModeCooldownEnd = buildModeCooldownEnd;
	}
	
	public PlayerJson(int team, String realName, int collectableDiamonds, boolean isBuildModeActive, LocalDateTime buildModeCooldownEnd) {
		this.team = team;
		this.realName = realName;
		this.collectableDiamonds = collectableDiamonds;
		this.isBuildModeActive = isBuildModeActive;
		this.buildModeCooldownEnd = buildModeCooldownEnd;
	}

	public PlayerJson(int team, String realName, int collectableDiamonds) {
		this.team = team;
		this.realName = realName;
		this.collectableDiamonds = collectableDiamonds;
	}
	
	public PlayerJson(int team, String realName) {
		this.team = team;
		this.realName = realName;
		this.collectableDiamonds = 0;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("team", this.team);
		data.put("realName", this.realName);
		data.put("collectableDiamonds", this.collectableDiamonds);
		data.put("isBuildModeActive", this.isBuildModeActive);
		data.put("buildModeCooldownEnd", this.buildModeCooldownEnd.toString());
		return data;
	}

	public static PlayerJson deserialize(Map<String, Object> args) {
		return new PlayerJson(
				NumberConversions.toInt(args.get("team")), 
				(String) args.get("realName"), 
				NumberConversions.toInt(args.get("collectableDiamonds")), 
				(boolean) args.get("isBuildModeActive"), 
				LocalDateTime.parse((CharSequence) args.get("buildModeCooldownEnd"))
			);
	}
}
