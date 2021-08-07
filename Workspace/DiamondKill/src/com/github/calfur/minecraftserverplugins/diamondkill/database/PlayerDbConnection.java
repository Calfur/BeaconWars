package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDbConnection extends DbConnection<PlayerData>{
	
	private HashMap<String, PlayerJson> players = new HashMap<>();
	
	public PlayerDbConnection() {
		super("player.json", new PlayerData(), PlayerData.class);
	}
	
	@SuppressWarnings("unchecked")
	public void loadConfig() {
		if(folder.exists()) {
			if(file.exists()) {
				PlayerData playerData = read();
				if(playerData != null) {
					playerData.getData().entrySet().forEach(entry -> {						
						players.put(entry.getKey(), PlayerJson.deserialize((Map<String, Object>) entry.getValue()));
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
	
	public boolean existsPlayer(String key) {
		if(players != null && players.get(key.toLowerCase()) != null) {
			return true;
		}
		return false;
	}
	
	public PlayerDbConnection addPlayer(String key, PlayerJson value) {
		key = key.toLowerCase();
		data.getData().put(key, value.serialize());
		players.put(key, value);
		gson.toJson(data, writer());
		saveConfig();
		return this;
	}	
	
	public PlayerDbConnection removePlayer(String key) {
		key = key.toLowerCase();
		data.getData().remove(key);
		players.remove(key);
		gson.toJson(data, writer());
		saveConfig();
		return this;
	}
	
	public PlayerJson getPlayer(String key) {
		return players.get(key.toLowerCase());
	}
	
	public HashMap<String, PlayerJson> getPlayers() {
		HashMap<String, PlayerJson> filteredPlayers = getAllNonSpectatorPlayers();
		return filteredPlayers;
	}

	private HashMap<String, PlayerJson> getAllNonSpectatorPlayers() {
		HashMap<String, PlayerJson> result = new HashMap<String, PlayerJson>(players);
		List<String> playersToRemove = new ArrayList<String>();
		result.forEach((id, player) -> {
			if(player.getTeamId() == TeamDbConnection.spectatorTeamNumber) {				
				playersToRemove.add(id);
			}
		});	
		playersToRemove.forEach(id -> {
			result.remove(id);
		});
		return result;
	}

	public boolean isPlayerSpectator(String key) {
		if(getPlayer(key).getTeamId() == TeamDbConnection.spectatorTeamNumber) {
			return true;
		}
		return false;
	}

}
