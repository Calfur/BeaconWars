package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
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
				event.setCancelled(true);
				Bukkit.broadcastMessage("Cancelled Trade");
				break;
			case IRON_SWORD:
				setMaxEntchantmentLevel(event.getRecipe().getResult().getEnchantments(),1);
				Bukkit.broadcastMessage("IRON_SWORD");
				break;
			case BOW:
				setMaxEntchantmentLevel(event.getRecipe().getResult().getEnchantments(),1);
				Bukkit.broadcastMessage("BOW");
				break;
		default:
			break;
		}
		
	}
	
	private void setMaxEntchantmentLevel(Map<Enchantment,Integer> enchantments, int maxValue) {
		Bukkit.broadcastMessage("" + maxValue);
		Map.Entry<Enchantment,Integer> editEnchantment;
		for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
			Bukkit.broadcastMessage("Enchntmen Level:" + enchantment.getValue());
			if (enchantment.getValue() > maxValue) {
				editEnchantment = enchantment;
				editEnchantment.setValue(maxValue);
				Bukkit.broadcastMessage("Nerfed Enchantment");
			}
		}
	}
}
