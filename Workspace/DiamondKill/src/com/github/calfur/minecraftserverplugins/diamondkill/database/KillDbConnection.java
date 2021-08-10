package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class KillDbConnection extends DbConnection<KillData, KillJson> {

	public KillDbConnection() {
		super("kill.json", new KillData(), KillData.class);
	}

	@Override
	protected KillJson deserialize(Map<String, Object> serializedJson) {
		return KillJson.deserialize((Map<String, Object>) serializedJson);
	}
		
	public boolean existsKill(int killNumber) {
		String key = Integer.toString(killNumber);
		if(jsons != null && jsons.get(key) != null) {
			return true;
		}
		return false;
	}
	
	public KillDbConnection addKill(int killNumber, KillJson value) {
		String key = Integer.toString(killNumber);
		addJson(key, value);
		return this;
	}	
	
	public KillDbConnection removeKill(int killNumber) {
		String key = Integer.toString(killNumber);
		removeJson(key);
		return this;
	}

	public KillJson getKill(int killNumber) {
		String key = Integer.toString(killNumber);
		return jsons.get(key.toLowerCase());
	}
	
	public HashMap<String, KillJson> getKills() {
		return jsons;
	}

	public int getNextId() {
		int heighestId = 0;
		for (Entry<String, KillJson> kill : jsons.entrySet()) {
			int id = Integer.parseInt(kill.getKey());
			if(heighestId < id) {
				heighestId = id;
			}
		}
		return heighestId + 1;
	}

	public int getAmountOfKills(String killer) {
		killer = killer.toLowerCase();
		int result = 0;
		for (Entry<String, KillJson> kill : jsons.entrySet()) {
			String currentKiller = kill.getValue().getKiller();
			if(currentKiller.equals(killer)) {
				result++;
			}
		}
		return result;
	}
	
	public int getAmountOfDeaths(String victim) {
		victim = victim.toLowerCase();
		int result = 0;
		for (Entry<String, KillJson> kill : jsons.entrySet()) {
			String currentVictim = kill.getValue().getVictim();
			if(currentVictim.equals(victim)) {
				result++;
			}
		}
		return result;
	}
	
	public int getBounty(String player) {
		int kills = getAmountOfKills(player);
		int deaths = getAmountOfDeaths(player);
		if(deaths < 1) {
			deaths = 1;
		}
		int result = Math.round(kills/deaths);
		if(result < 1) {
			result = 1;
		}
		return result;
	}
}
