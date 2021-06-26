package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ForbiddenLootItem {
	private Material item;
	private ItemStack substitute;
	private EnchantmentLevel enchantmentLevel = EnchantmentLevel.noEnchantments;
	
	public Material getItem() {
		return item;
	}
	
	public void setItem(Material item) {
		this.item = item;
	}
	
	public ItemStack getCloneOfSubstitute() {
		return substitute.clone();
	}
	
	public void setSubstitute(ItemStack substitute) {
		this.substitute = substitute;
	}

	public EnchantmentLevel getEnchantmentLevel() {
		return enchantmentLevel;
	}
	
	public ForbiddenLootItem(Material item, ItemStack substitute) {
		this.item = item;
		this.substitute = substitute;
	}
	
	public ForbiddenLootItem(Material item, ItemStack substitute, EnchantmentLevel enchantmentLevel) {
		this(item, substitute);
		this.enchantmentLevel = enchantmentLevel;
	}
	
	public static Map<Enchantment, Integer> nerfEnchantments(Map<Enchantment, Integer> enchantments){
		Map<Enchantment, Integer> nerfedEnchantments = new HashMap<Enchantment, Integer>();
		for(Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
			Integer newValue = enchantment.getValue();
			switch(enchantment.getKey().getKey().getKey()) {
				case "protection":
				case "projectile_protection":
					if(enchantment.getValue() > 2) {	
						newValue = 2;
					}
					break;
				case "sharpness":
					if(enchantment.getValue() > 3) {	
						newValue = 3;
					}
					break;
			}
			nerfedEnchantments.put(enchantment.getKey(), newValue);
		}
		return nerfedEnchantments;
	}
	
	public enum EnchantmentLevel{
		noEnchantments,
		nerfedEnchantments,
		copiedEnchantments
	}
}
