package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class JsonPlayer implements ConfigurationSerializable {
	private int team;	
	
	public JsonPlayer(int team) {
		this.team = team;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("team", this.team);
		return data;
	}

	public static JsonPlayer deserialize(Map<String, Object> args) {
		return new JsonPlayer(NumberConversions.toInt(args.get("team")));
	}
}
