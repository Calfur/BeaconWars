package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TransactionDbConnection extends DbConnection<TransactionJson>{
	
	public TransactionDbConnection() {
		super("transaction.json");
	}

	@Override
	protected TransactionJson deserialize(Map<String, Object> serializedJson) {
		return TransactionJson.deserialize((Map<String, Object>) serializedJson);
	}	

	public TransactionDbConnection addTransaction(int id, TransactionJson value) {
		String key = Integer.toString(id);
		addJson(key, value);
		return this;
	}	
	
	public TransactionDbConnection removeTransaction(int id) {
		String key = Integer.toString(id);
		removeJson(key);
		return this;
	}
	
	public TransactionJson getTransaction(int killNumber) {
		String key = Integer.toString(killNumber);
		return jsons.get(key.toLowerCase());
	}
	
	public HashMap<String, TransactionJson> getTransactions() {
		return jsons;
	}

	public int getNextId() {
		int heighestId = 0;
		for (Entry<String, TransactionJson> transaction : jsons.entrySet()) {
			int id = Integer.parseInt(transaction.getKey());
			if(heighestId < id) {
				heighestId = id;
			}
		}
		return heighestId + 1;
	}
}
