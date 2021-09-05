package com.github.calfur.beaconWars;

import org.bukkit.Bukkit;

import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TeamJson;
import com.github.calfur.beaconWars.database.TransactionDbConnection;
import com.github.calfur.beaconWars.database.TransactionJson;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class RewardManager {
	PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
	ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	
	public void addReward(String playerName, int amountOfDiamonds, int amountOfPoints, String reason) {
		PlayerJson playerJson = playerDbConnection.getPlayer(playerName);
		int teamId = playerJson.getTeamId();
		if(teamId == 0) {
			Bukkit.broadcastMessage(StringFormatter.error("Error: Es wurde versucht, einem Spectator eine Belohnung gutzuschreiben"));
			return;
		}
		TeamJson teamJson = teamDbConnection.getTeam(teamId);
		if(amountOfDiamonds != 0) {			
			playerJson.addCollectableDiamonds(amountOfDiamonds);
			playerDbConnection.addPlayer(playerName, playerJson);
		}
		if(amountOfPoints != 0) {
			teamJson.addPoints(amountOfPoints);
			teamDbConnection.addTeam(teamId, teamJson);
		}
		
		TransactionJson transactionJson = new TransactionJson(
				playerName, 
				teamId, 
				amountOfDiamonds, 
				amountOfPoints, 
				reason);
		transactionDbConnection.addTransaction(transactionJson);
		
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
	}
}
