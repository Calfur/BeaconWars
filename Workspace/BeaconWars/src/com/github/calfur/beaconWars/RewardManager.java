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
	
	public void addReward(String playerName, Reward reward, String reason) {
		PlayerJson playerJson = playerDbConnection.getPlayer(playerName);
		int teamId = playerJson.getTeamId();
		if(teamId == 0) {
			Bukkit.broadcastMessage(StringFormatter.error("Error: Es wurde versucht, einem Spectator eine Belohnung gutzuschreiben"));
			return;
		}
		TeamJson teamJson = teamDbConnection.getTeam(teamId);
		if(reward.getDiamonds() != 0) {			
			playerJson.addCollectableDiamonds(reward.getDiamonds());
			playerDbConnection.addPlayer(playerName, playerJson);
		}
		if(reward.getPoints() != 0) {
			teamJson.addPoints(reward.getPoints());
			teamDbConnection.addTeam(teamId, teamJson);
		}
		
		TransactionJson transactionJson = new TransactionJson(
			playerName, 
			teamId, 
			reward.getDiamonds(), 
			reward.getPoints(), 
			reason
		);
		transactionDbConnection.addTransaction(transactionJson);
		
		scoreboardLoader.reloadScoreboardForAllOnlinePlayers();
	}
}
