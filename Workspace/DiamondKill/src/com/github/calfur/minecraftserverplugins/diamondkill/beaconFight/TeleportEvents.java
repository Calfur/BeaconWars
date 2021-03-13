package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class TeleportEvents implements Listener{
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(beaconFightManager.isBeaconEventActive()) {			
			if (event.getFrom().getWorld() != event.getTo().getWorld()) {	    	
				event.setCancelled(true);
			}
		}
	}
}
