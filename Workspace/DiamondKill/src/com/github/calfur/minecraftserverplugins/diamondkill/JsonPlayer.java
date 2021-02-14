package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class JsonPlayer implements ConfigurationSerializable {
	private int team;	
	private String discordName;
	
	public String getDiscordName() {
		return discordName;
	}
	
	public int getTeam() {
		return team;
	}
	
	public JsonPlayer(int team, String discordName) {
		this.team = team;
		this.discordName = discordName;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("team", this.team);
		data.put("discordName", this.discordName);
		return data;
	}

	public static JsonPlayer deserialize(Map<String, Object> args) {
		return new JsonPlayer(NumberConversions.toInt(args.get("team")), (String) args.get("discordName"));
	}
}
