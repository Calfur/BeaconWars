package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class KillJson implements ConfigurationSerializable {
	String killer;
	String victim;
	LocalDateTime dateTime;
	
	public String getKiller() {
		return killer;
	}

	public void setKiller(String killer) {
		this.killer = killer.toLowerCase();
	}

	public String getVictim() {
		return victim;
	}

	public void setVictim(String victim) {
		this.victim = victim.toLowerCase();
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public KillJson(String killer, String victim, LocalDateTime dateTime) {
		this.killer = killer.toLowerCase();
		this.victim = victim.toLowerCase();
		this.dateTime = dateTime;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("killer", this.killer);
		data.put("victim", this.victim);
		data.put("dateTime", this.dateTime.toString());
		return data;
	}
	
	public static KillJson deserialize(Map<String, Object> args) {
		return new KillJson(
				(String) args.get("killer"),
				(String) args.get("victim"),
				LocalDateTime.parse((CharSequence) args.get("dateTime"))
			);
	}
}
