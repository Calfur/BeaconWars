package com.github.calfur.beaconWars.beaconFight;

import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.github.calfur.beaconWars.Main;

public class TeleportEvents implements Listener{
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(beaconFightManager.isBeaconEventActive()) {			
			if (event.getTo().getWorld().getEnvironment() != Environment.NORMAL) {	    	
				event.setCancelled(true);
			}
		}
	}
}
