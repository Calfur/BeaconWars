package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public class TeamDbConnection extends DbConnection<TeamData> {

	private HashMap<String, TeamJson> teams = new HashMap<>();
	
	public TeamDbConnection() {
		super("team.json", new TeamData(), TeamData.class);
	}
		
	@SuppressWarnings("unchecked")
	public void loadConfig() {
		if(folder.exists()) {
			if(file.exists()) {
				if(read() != null) {
					read().getData().entrySet().forEach(entry -> {						
						teams.put(entry.getKey(), TeamJson.deserialize((Map<String, Object>) entry.getValue()));
						data.getData().put(entry.getKey(), entry.getValue());
						Bukkit.broadcastMessage("Key reloaded: " + entry.getKey());
					});
				}
			}else {
				createFile().saveConfig().loadConfig();
			}
		}else {
			createFolder().loadConfig();
		}
	}
		
	public boolean existsTeam(int teamNumber) {
		String key = Integer.toString(teamNumber);
		if(teams != null && teams.get(key) != null) {
			return true;
		}
		return false;
	}
	
	public TeamDbConnection addTeam(int teamNumber, TeamJson value) {
		String key = Integer.toString(teamNumber);
		data.getData().put(key, value.serialize());
		teams.put(key, value);
		gson.toJson(data, writer());
		saveConfig();
		return this;
	}	
	
	public TeamDbConnection removeTeam(int teamNumber) {
		String key = Integer.toString(teamNumber);
		data.getData().remove(key);
		teams.remove(key);
		gson.toJson(data, writer());
		saveConfig();
		return this;
	}
	
	public TeamJson getTeam(int teamNumber) {
		String key = Integer.toString(teamNumber);
		return teams.get(key.toLowerCase());
	}
}
