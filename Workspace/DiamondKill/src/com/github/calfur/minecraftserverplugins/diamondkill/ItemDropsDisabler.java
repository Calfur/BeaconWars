package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDropsDisabler implements Listener{
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Bukkit.broadcastMessage("PlayerDeathEvent");
		for (ItemStack itemStack: event.getDrops()) {
			Bukkit.broadcastMessage("oldType: " + itemStack.getType());
			switch (itemStack.getType()) {
			case DIAMOND_BOOTS:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_HELMET: 
			case DIAMOND_LEGGINGS:
				Bukkit.broadcastMessage("setType");
				itemStack.setType(Material.LIGHT_BLUE_DYE);
			default:
				break;
			}
			
		}
		
		
	}
}
