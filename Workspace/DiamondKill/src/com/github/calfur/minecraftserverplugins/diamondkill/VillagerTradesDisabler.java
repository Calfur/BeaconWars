package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;


public class VillagerTradesDisabler implements Listener {
	@EventHandler
	public void onVillagerAcquiresTrade(VillagerAcquireTradeEvent event) {
		switch (event.getRecipe().getResult().getType()){
			case DIAMOND_AXE:
			case DIAMOND_BOOTS:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_HOE: 
			case DIAMOND_HELMET:
			case DIAMOND_LEGGINGS:
			case DIAMOND_PICKAXE:
			case DIAMOND_SHOVEL:
			case DIAMOND_SWORD:
			case IRON_SWORD:
			case BOW:
				event.setCancelled(true);
//				Bukkit.broadcastMessage("Cancelled Trade");
				break;
		default:
			break;
		}
		
	}
}
