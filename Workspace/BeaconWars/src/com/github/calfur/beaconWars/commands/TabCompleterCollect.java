package com.github.calfur.beaconWars.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;

public class TabCompleterCollect extends TabCompleterBase{

	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	
	@Override
	List<String> getSuggestions(String[] previousParameters, Player sender) {
		List<String> completions = new ArrayList<String>();
		switch (previousParameters.length) {
		case 1:
			completions.add("diamond");
			break;
		case 2:
			completions.add("1");
			if(playerDbConnection.existsPlayer(sender.getName()))
			{
				PlayerJson playerJson = playerDbConnection.getPlayer(sender.getName());
				int availableDiamonds = playerJson.getCollectableDiamonds();
				if(availableDiamonds > 0) {				
					completions.add("" + availableDiamonds);
				}
			}
			break;
		}
		return completions;
	}
	
    
}