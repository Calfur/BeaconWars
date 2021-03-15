package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.calfur.minecraftserverplugins.diamondkill.BeaconManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class BeaconItemEvents implements Listener{
	BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	
	@EventHandler
	public void onBeaconGetsPlaced(BlockPlaceEvent event) {
		if(event.getBlock().getType() == Material.BEACON) {
			BeaconFight ongoingBeaconFight = beaconFightManager.getOngoingBeaconFight();
			if(ongoingBeaconFight == null) {
				BeaconManager.removeOneBeaconFromInventory(event.getPlayer());
			}else {
				ongoingBeaconFight.addBeaconPlacement(event.getPlayer(), event.getBlockAgainst().getLocation());
			}
			event.setCancelled(true);
		}
	}
	
}
