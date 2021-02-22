package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

public class KillDbConnection extends DbConnection<KillData> {

	private HashMap<String, KillJson> kills = new HashMap<>();
	
	public KillDbConnection() {
		super("kill.json", new KillData(), KillData.class);
	}
		
	@SuppressWarnings("unchecked")
	public void loadConfig() {
		if(folder.exists()) {
			if(file.exists()) {
				if(read() != null) {
					read().getData().entrySet().forEach(entry -> {						
						kills.put(entry.getKey(), KillJson.deserialize((Map<String, Object>) entry.getValue()));
						data.getData().put(entry.getKey(), entry.getValue());
					});
				}
			}else {
				createFile().saveConfig().loadConfig();
			}
		}else {
			createFolder().loadConfig();
		}
	}
		
	public boolean existsKill(int killNumber) {
		String key = Integer.toString(killNumber);
		if(kills != null && kills.get(key) != null) {
			return true;
		}
		return false;
	}
	
	public KillDbConnection addKill(int killNumber, KillJson value) {
		String key = Integer.toString(killNumber);
		data.getData().put(key, value.serialize());
		kills.put(key, value);
		gson.toJson(data, writer());
		saveConfig();
		return this;
	}	
	
	public KillDbConnection removeKill(int killNumber) {
		String key = Integer.toString(killNumber);
		data.getData().remove(key);
		kills.remove(key);
		gson.toJson(data, writer());
		saveConfig();
		return this;
	}

	public KillJson getKill(int killNumber) {
		String key = Integer.toString(killNumber);
		return kills.get(key.toLowerCase());
	}
	
	public HashMap<String, KillJson> getKills() {
		return kills;
	}
}
