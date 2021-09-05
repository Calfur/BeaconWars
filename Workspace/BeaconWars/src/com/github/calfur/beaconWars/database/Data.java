package com.github.calfur.beaconWars.database;

import java.util.HashMap;
import java.util.Map;

public class Data {
	private Map<String, Object> data = new HashMap<>();
	
	public void add(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}
}
