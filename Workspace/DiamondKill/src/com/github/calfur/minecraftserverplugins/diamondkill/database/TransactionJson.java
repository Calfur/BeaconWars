package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class TransactionJson implements ConfigurationSerializable {
	private String playerName;
	private int team;
	private String reason;
	private int transactedDiamonds;
	private int transactedPoints;
	private LocalDateTime dateTime;
	
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
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public TransactionJson(String playerName, int team, int transactedDiamonds, int transactedPoints, String reason) {
		this.playerName = StringFormatter.firstLetterToUpper(playerName);
		this.team = team;
		this.reason = reason;
		this.transactedDiamonds = transactedDiamonds;
		this.transactedPoints = transactedPoints;
		this.dateTime = LocalDateTime.now().withNano(0);
	}
	
	private TransactionJson(String playerName, int team, int transactedDiamonds, int transactedPoints, String reason, LocalDateTime dateTime) {
		this.playerName = playerName;
		this.team = team;
		this.reason = reason;
		this.transactedDiamonds = transactedDiamonds;
		this.transactedPoints = transactedPoints;
		this.dateTime = dateTime;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerName", this.playerName);
		data.put("team", this.team);
		data.put("reason", this.reason);
		data.put("transactedDiamonds", this.transactedDiamonds);
		data.put("transactedPoints", this.transactedPoints);
		data.put("dateTime", this.dateTime.toString());
		return data;
	}
	
	public static TransactionJson deserialize(Map<String, Object> args) {
		return new TransactionJson(
				(String) args.get("playerName"), 
				NumberConversions.toInt(args.get("team")), 
				NumberConversions.toInt(args.get("transactedDiamonds")),
				NumberConversions.toInt(args.get("transactedPoints")),
				(String) args.get("reason"),
				LocalDateTime.parse((CharSequence) args.get("dateTime"))
			);
	}
}
