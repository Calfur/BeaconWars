package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

public class KillData implements IData {
	private Map<String, Object> kill = new HashMap<>();
	
	@Override
	public void add(Map<String, Object> data) {
		kill = data;
	}

	@Override
	public Map<String, Object> getData() {
		return kill;
	}
}
