package com.github.calfur.beaconWars.disabling;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.github.calfur.beaconWars.disabling.ForbiddenLootItem.EnchantmentLevel;

public class FishingNerf implements Listener{
	private List<ForbiddenLootItem> forbiddenLoot = Arrays.asList(
			new ForbiddenLootItem(Material.BOW, new ItemStack(Material.BOW), EnchantmentLevel.noEnchantments) 
	);
	
	@EventHandler
	public void onFish(PlayerFishEvent event) {
		if(!event.isCancelled()) {
			Entity lootEntity = event.getCaught();
			if(lootEntity != null && lootEntity.getType() == EntityType.DROPPED_ITEM) {
				Item lootItem = (Item) lootEntity;
				ItemStack lootItemStack = lootItem.getItemStack();
				ItemStack newItemStack = replaceIfForbidden(lootItemStack);
				lootItem.setItemStack(newItemStack);
			}
		}
	}

	private ItemStack replaceIfForbidden(ItemStack lootItemStack) {
		for(ForbiddenLootItem forbiddenLootItem : forbiddenLoot) {
			if(lootItemStack.getType() == forbiddenLootItem.getItem()) {
				return forbiddenLootItem.getSubstituteWithAdjustedEnchantments(lootItemStack.getEnchantments());
			}
		}
		return lootItemStack;
	}
}
