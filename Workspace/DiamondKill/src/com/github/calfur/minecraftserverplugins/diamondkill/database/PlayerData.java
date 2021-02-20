package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
	private Map<String, Object> player = new HashMap<>();
	
	public void addPlayer(Map<String, Object> player){
		this.player = player;
	}
	
	public Map<String, Object> getPlayer() {
		return player;
	}
}
