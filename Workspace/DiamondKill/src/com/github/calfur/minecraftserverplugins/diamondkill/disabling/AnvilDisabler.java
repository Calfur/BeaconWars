package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilDisabler implements Listener{
	
	@EventHandler
	public void onAnvilInventoryOpens(InventoryOpenEvent event) {
		if (event.getInventory().getType() == InventoryType.ANVIL) {
			event.setCancelled(true);
		}
	}
}