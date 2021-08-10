package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

public class TeamDbConnection extends DbConnection<TeamJson> {

	public static final int spectatorTeamNumber = -1;
	
	public TeamDbConnection() {
		super("team.json");
	}

	@Override
	protected TeamJson deserialize(Map<String, Object> serializedJson) {
		return TeamJson.deserialize((Map<String, Object>) serializedJson);
	}
		
	public boolean existsTeam(int teamNumber) {
		String key = Integer.toString(teamNumber);
		if(jsons != null && jsons.get(key) != null || teamNumber == spectatorTeamNumber) {
			return true;
		}
		return false;
	}
	
	public TeamDbConnection addTeam(int teamNumber, TeamJson value) {
		String key = Integer.toString(teamNumber);
		addJson(key, value);
		return this;
	}	
	
	public TeamDbConnection removeTeam(int teamNumber) {
		String key = Integer.toString(teamNumber);
		removeJson(key);
		return this;
	}

	public TeamJson getTeam(int teamNumber) {
		String key = Integer.toString(teamNumber);
		if(teamNumber == spectatorTeamNumber) {
			return TeamJson.getSpectatorTeam();
		}
		return jsons.get(key.toLowerCase());
	}
	
	public HashMap<String, TeamJson> getTeams() {
		return jsons;
	}
}
