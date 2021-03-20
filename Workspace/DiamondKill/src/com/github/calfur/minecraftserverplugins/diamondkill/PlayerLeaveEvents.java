package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.BeaconFightManager;

public class PlayerLeaveEvents implements Listener {
	BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	
	@EventHandler
	public void onPlayerJoins(PlayerQuitEvent event) {
		Player leaver = event.getPlayer();
		if(BeaconManager.removeOneBeaconFromInventory(leaver)) {
			if(beaconFightManager.isBeaconEventActive()) {	
				beaconFightManager.getOngoingBeaconFight().removeBeaconRaidsByDestructor(leaver);
			}
		}
	}
}
