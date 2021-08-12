package com.github.calfur.minecraftserverplugins.diamondkill;

import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionJson;

public class RewardManager {
	PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	
	public void addReward(String playerName, int amountOfDiamonds, int amountOfPoints, String reason) {
		PlayerJson playerJson = playerDbConnection.getPlayer(playerName);
		if(amountOfDiamonds != 0) {			
			playerJson.addCollectableDiamonds(amountOfDiamonds);
		}
		playerDbConnection.addPlayer(playerName, playerJson);
		TransactionJson transactionJson = new TransactionJson(
				playerName, 
				playerJson.getTeamId(), 
				amountOfDiamonds, 
				amountOfPoints, 
				reason);
		transactionDbConnection.addTransaction(transactionJson);
		
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
	}
}
