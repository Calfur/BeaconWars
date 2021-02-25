package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Map;
import java.util.Map.Entry;

import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;

public class TopKiller {
	private String name;
	private int diamondValue;
	private boolean multipleTopKiller = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDiamondValue() {
		return diamondValue;
	}
	public void setDiamondValue(int diamondValue) {
		this.diamondValue = diamondValue;
	}

	public TopKiller(String name, int diamondValue) {
		this.name = name;
		this.diamondValue = diamondValue;
	}
	
	private TopKiller() {
		this.name = "404";
		this.diamondValue = 404;
		this.multipleTopKiller = true;
	}
	
	public static TopKiller getCurrentTopKiller() {
		Main plugin = Main.getInstance();
		Map<String, PlayerJson> players = plugin.getPlayerDbConnection().getPlayers();
		KillDbConnection killDbConnection = plugin.getKillDbConnection();
		float heighestKd = 0;
		String playerWithHeighestBounty = "";
		boolean equalHeighKd = false;
		for (Entry<String, PlayerJson> player : players.entrySet()) {			
			int amountOfKills = killDbConnection.getAmountOfKills(player.getKey());
			int amountOfDeaths = killDbConnection.getAmountOfKills(player.getKey());
			float kd = amountOfKills / amountOfDeaths;
			if(kd > heighestKd) {
				heighestKd = kd;
				playerWithHeighestBounty = player.getKey();
				equalHeighKd = false;
			}else if(kd == heighestKd) {
				equalHeighKd = true;
			}
		}
		if(equalHeighKd) {
			return new TopKiller(); //multiple TopKiller
		}
		return new TopKiller(playerWithHeighestBounty, killDbConnection.getBounty(playerWithHeighestBounty));
	}
}
