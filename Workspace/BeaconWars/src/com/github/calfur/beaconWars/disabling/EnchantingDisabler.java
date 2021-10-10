package com.github.calfur.beaconWars.disabling;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantingDisabler implements Listener {

	@EventHandler
	public void onEnchantItem(EnchantItemEvent event) {
		if(event.whichButton() != 0) {
			event.setCancelled(true);
		}		
	}

}
