package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

public class TransactionData implements IData {
	private Map<String, Object> transaction = new HashMap<>();
	
	@Override
	public void add(Map<String, Object> data) {
		this.transaction = data;
	}

	@Override
	public Map<String, Object> getData() {
		return transaction;
	}

}
