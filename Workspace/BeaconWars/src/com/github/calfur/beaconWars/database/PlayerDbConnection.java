package com.github.calfur.beaconWars.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDbConnection extends DbConnection<PlayerJson>{
	
	public PlayerDbConnection() {
		super("player.json");
	}

	@Override
	protected PlayerJson deserialize(Map<String, Object> serializedJson) {
		return PlayerJson.deserialize((Map<String, Object>) serializedJson);
	}
	
	public boolean existsPlayer(String key) {
		if(jsons != null && jsons.get(key.toLowerCase()) != null) {
			return true;
		}
		return false;
	}
	
	public PlayerDbConnection addPlayer(String key, PlayerJson value) {
		key = key.toLowerCase();
		addJson(key, value);
		return this;
	}	
	
	public PlayerDbConnection removePlayer(String key) {
		key = key.toLowerCase();
		removeJson(key);
		return this;
	}
	
	public PlayerJson getPlayer(String key) {
		return jsons.get(key.toLowerCase());
	}
	
	public HashMap<String, PlayerJson> getPlayers() {
		HashMap<String, PlayerJson> filteredPlayers = getAllNonSpectatorPlayers();
		return filteredPlayers;
	}

	private HashMap<String, PlayerJson> getAllNonSpectatorPlayers() {
		HashMap<String, PlayerJson> result = new HashMap<String, PlayerJson>(jsons);
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
