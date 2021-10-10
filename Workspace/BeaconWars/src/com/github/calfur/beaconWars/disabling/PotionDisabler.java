package com.github.calfur.beaconWars.disabling;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class PotionDisabler implements Listener {
	private static DisabledPotion[] disabledPotions = {
			new DisabledPotion(Material.FERMENTED_SPIDER_EYE, PotionType.INSTANT_HEAL),
			new DisabledPotion(Material.FERMENTED_SPIDER_EYE, PotionType.POISON),
			new DisabledPotion(Material.GLOWSTONE_DUST, PotionType.STRENGTH)
	};
	
	@EventHandler
	public void onBrewEvent(BrewEvent event) {
		BrewerInventory inventory = event.getContents();
		Material ingredient = inventory.getIngredient().getType();
		// Bukkit.broadcastMessage("Zutat: " + ingredient.name());

		List<PotionType> potionTypes = getUsedPotions(inventory);
		for (PotionType potionType : potionTypes) {
			// Bukkit.broadcastMessage("Type: " + potionType);
			for (DisabledPotion disabledPotion : disabledPotions) {
				if(potionType == disabledPotion.getPotionType() && ingredient == disabledPotion.getIngerdient()) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	private List<PotionType> getUsedPotions(BrewerInventory inventory) {
		List<ItemStack> itemStacks = new ArrayList<ItemStack>(); 
		itemStacks.add(inventory.getItem(0));
		itemStacks.add(inventory.getItem(1));
		itemStacks.add(inventory.getItem(2));
		
		List<PotionType> potionTypes = new ArrayList<PotionType>(); 
		
		for (ItemStack itemStack : itemStacks) {	
			PotionType potionType = getPotionType(itemStack);
			if(potionType != null) {				
				potionTypes.add(potionType);
			}
		}
		return potionTypes;
	}
	
	private PotionType getPotionType(ItemStack itemStack) {
		PotionMeta potionMeta;
		try {			
			potionMeta = (PotionMeta) itemStack.getItemMeta();
		}catch (Exception e) {
			return null;
		}
		PotionData potionData = potionMeta.getBasePotionData();
		PotionType potionType = potionData.getType();
		return potionType;
	}
}
