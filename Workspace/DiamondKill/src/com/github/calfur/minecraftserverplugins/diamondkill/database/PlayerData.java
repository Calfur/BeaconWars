package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements IData {
	private Map<String, Object> player = new HashMap<>();
	
	@Override
	public void add(Map<String, Object> data) {
		player = data;
	}

	@Override
	public Map<String, Object> getData() {
		return player;
	}
}
