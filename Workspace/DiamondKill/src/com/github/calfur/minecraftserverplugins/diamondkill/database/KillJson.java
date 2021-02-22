package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class KillJson implements ConfigurationSerializable {
	String killer;
	String victim;
	LocalDateTime dateTime;
	
	public KillJson(String killer, String victim, LocalDateTime dateTime) {
		super();
		this.killer = killer;
		this.victim = victim;
		this.dateTime = dateTime;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("killer", this.killer);
		data.put("victim", this.victim);
		data.put("dateTime", this.dateTime);
		return data;
	}
	
	public static KillJson deserialize(Map<String, Object> args) {
		return new KillJson(
				(String) args.get("killer"),
				(String) args.get("victim"),
				(LocalDateTime) args.get("dateTime")
			);
	}
}
