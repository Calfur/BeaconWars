package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class TransactionJson implements ConfigurationSerializable {
	private String playerName;
	private int team;
	private String reason;
	private int transactedDiamonds;
	private int transactedPoints;
	
	public String getPlayerName() {
		return playerName;
	}

	public int getTeam() {
		return team;
	}

	public String getReason() {
		return reason;
	}

	public int getTransactedDiamonds() {
		return transactedDiamonds;
	}

	public int getTransactedPoints() {
		return transactedPoints;
	}	

	public TransactionJson(String playerName, int team, String reason, int transactedDiamonds, int transactedPoints) {
		this.playerName = playerName;
		this.team = team;
		this.reason = reason;
		this.transactedDiamonds = transactedDiamonds;
		this.transactedPoints = transactedPoints;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerName", this.playerName);
		data.put("team", this.team);
		data.put("reason", this.reason);
		data.put("transactedDiamonds", this.transactedDiamonds);
		data.put("transactedPoints", this.transactedPoints);
		return data;
	}
	
	public static TransactionJson deserialize(Map<String, Object> args) {
		return new TransactionJson(
				(String) args.get("playerName"), 
				NumberConversions.toInt(args.get("team")), 
				(String) args.get("reason"), 
				NumberConversions.toInt(args.get("transactedDiamonds")),
				NumberConversions.toInt(args.get("transactedPoints"))
			);
	}
}
