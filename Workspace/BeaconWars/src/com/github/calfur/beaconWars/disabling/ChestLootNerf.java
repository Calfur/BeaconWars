package com.github.calfur.beaconWars.disabling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.github.calfur.beaconWars.disabling.ForbiddenLootItem.EnchantmentLevel;

public class ChestLootNerf implements Listener {

	private Color diamondColor = Color.fromRGB(124, 255, 243); //8191987 / #7cfff3
	private List<ForbiddenLootItem> forbiddenLoot = Arrays.asList(
			new ForbiddenLootItem(Material.DIAMOND, new ItemStack(Material.LIGHT_BLUE_DYE)), 
			new ForbiddenLootItem(Material.DIAMOND_BOOTS, createColorizedLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), diamondColor), EnchantmentLevel.copiedEnchantments),
			new ForbiddenLootItem(Material.DIAMOND_LEGGINGS, createColorizedLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), diamondColor), EnchantmentLevel.copiedEnchantments),
			new ForbiddenLootItem(Material.DIAMOND_CHESTPLATE, createColorizedLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), diamondColor), EnchantmentLevel.copiedEnchantments),
			new ForbiddenLootItem(Material.DIAMOND_HELMET, createColorizedLeatherArmor(new ItemStack(Material.LEATHER_HELMET), diamondColor), EnchantmentLevel.copiedEnchantments),
			new ForbiddenLootItem(Material.DIAMOND_SWORD, new ItemStack(Material.WOODEN_SWORD), EnchantmentLevel.copiedEnchantments),
			new ForbiddenLootItem(Material.IRON_SWORD, new ItemStack(Material.IRON_SWORD), EnchantmentLevel.nerfedEnchantments),
			new ForbiddenLootItem(Material.IRON_BOOTS, new ItemStack(Material.IRON_BOOTS), EnchantmentLevel.nerfedEnchantments),
			new ForbiddenLootItem(Material.IRON_LEGGINGS, new ItemStack(Material.IRON_LEGGINGS), EnchantmentLevel.nerfedEnchantments),
			new ForbiddenLootItem(Material.IRON_CHESTPLATE, new ItemStack(Material.IRON_CHESTPLATE), EnchantmentLevel.nerfedEnchantments),
			new ForbiddenLootItem(Material.IRON_HELMET, new ItemStack(Material.IRON_HELMET), EnchantmentLevel.nerfedEnchantments)
	);

	@EventHandler
	public void onDiamondLootGenerate(LootGenerateEvent event) {
		List<ItemStack> loot = event.getLoot();
		ArrayList<ItemStack> newLoot = new ArrayList<>();
		for(ItemStack itemStack : loot){
			newLoot.add(nerfIfNeeded(itemStack));
		}
		event.setLoot(newLoot);
	}

	private ItemStack nerfIfNeeded(ItemStack itemStack) {
		for(ForbiddenLootItem forbiddenItem : forbiddenLoot) {
			if(itemStack.getType() == forbiddenItem.getItem()) {
				return forbiddenItem.getSubstituteWithAdjustedEnchantments(itemStack.getEnchantments());
			}
		}
		return itemStack;
	}

	
	private ItemStack createColorizedLeatherArmor(ItemStack itemStack, Color color) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta instanceof LeatherArmorMeta) {			
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
			leatherArmorMeta.setColor(color);
			itemStack.setItemMeta(leatherArmorMeta);
		}
		return itemStack;
	}
}
