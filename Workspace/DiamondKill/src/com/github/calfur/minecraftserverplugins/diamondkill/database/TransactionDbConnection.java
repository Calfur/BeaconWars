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

	public boolean existsTransaction(int transactionId) {
		String key = Integer.toString(transactionId);
		if(jsons != null && jsons.get(key) != null) {
			return true;
		}
		return false;
	}

	public TransactionDbConnection addTransaction(TransactionJson value) {
		String key = Integer.toString(getNextId());
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

	private int getNextId() {
		int heighestId = 0;
		for (Entry<String, TransactionJson> transaction : jsons.entrySet()) {
			int id = Integer.parseInt(transaction.getKey());
			if(heighestId < id) {
				heighestId = id;
			}
		}
		return heighestId + 1;
	}

	public int getAmountOfAvailablePages() {
		return (int) Math.ceil((getNextId()-1) / 10.0);
	}
}
