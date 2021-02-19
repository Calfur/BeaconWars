package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;


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
//				event.getRecipe().getResult().setType(Material.LIGHT_BLUE_DYE);
				event.setCancelled(true);
				Bukkit.broadcastMessage("Cancelled Trade");
				break;
			case IRON_SWORD:
				ItemStack itemStack = new ItemStack(Material.DIAMOND, 2);
				MerchantRecipe merchantRecipe = new MerchantRecipe(itemStack, 10);
				event.setRecipe(merchantRecipe);
//				for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
//					event.getRecipe().getResult().getEnchantments().put(enchantment.getKey(),1);
//					Bukkit.broadcastMessage("Replaced");
//				}
//				setMaxEntchantmentLevel(,1);
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
		for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
			Bukkit.broadcastMessage("Enchntmen Level:" + enchantment.getValue());
			if (enchantment.getValue() > maxValue) {
				enchantment.setValue(maxValue);
				Bukkit.broadcastMessage("Nerfed Enchantment");
			}
		}
	}
}
