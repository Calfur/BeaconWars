package com.github.calfur.beaconWars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.beaconFight.BeaconManager;

public class PlayerLeaveEvents implements Listener {
	BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player leaver = event.getPlayer();
		checkIfPlayerHasBeaconInInventory(leaver);
		removeDeathMessageIfBanned(leaver, event);
	}

	public void checkIfPlayerHasBeaconInInventory(Player player) {
		if(BeaconManager.removeBeaconsFromInventory(player)) {
			if(beaconFightManager.isBeaconEventActive()) {	
				beaconFightManager.getOngoingBeaconFight().removeBeaconRaidsByDestructor(player);
			}
		}
	}
	
	private void removeDeathMessageIfBanned(Player leaver, PlayerQuitEvent event) {
		if(leaver.getHealth() == 0) {
			event.setQuitMessage("");
		}
	}
}
