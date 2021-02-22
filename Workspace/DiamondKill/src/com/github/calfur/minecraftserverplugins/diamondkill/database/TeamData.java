package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

public class TeamData implements IData {
	private Map<String, Object> team = new HashMap<>();
	
	@Override
	public void add(Map<String, Object> data) {
		team = data;
	}

	@Override
	public Map<String, Object> getData() {
		return team;
	}
}
