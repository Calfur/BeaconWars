package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ForbiddenItem {
	private Material item;
	private ItemStack substitute;
	private EnchantmentLevel enchantmentLevel = EnchantmentLevel.noEnchantments;
	
	public Material getItem() {
		return item;
	}
	public void setItem(Material item) {
		this.item = item;
	}
	public ItemStack getSubstitute() {
		return substitute;
	}
	public void setSubstitute(ItemStack substitute) {
		this.substitute = substitute;
	}

	public EnchantmentLevel getEnchantmentLevel() {
		return enchantmentLevel;
	}
	
	public ForbiddenItem(Material item, ItemStack substitute) {
		this.item = item;
		this.substitute = substitute;
	}
	public ForbiddenItem(Material item, ItemStack substitute, EnchantmentLevel enchantmentLevel) {
		this(item, substitute);
		this.enchantmentLevel = enchantmentLevel;
	}
	public static Map<Enchantment, Integer> nerfEnchantments(Map<Enchantment, Integer> enchantments){
		Map<Enchantment, Integer> nerfedEnchantments;
		for(Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
			/*switch(enchantment.getKey()) {
			case Enchantment.PROTECTION_ENVIRONMENTAL:
				if(enchantment.getValue() > 2) {
					enchantment.setValue(2);
				}
				break;
			}*/
		}
		return enchantments;
	}
	
	enum EnchantmentLevel{
		noEnchantments,
		nerfedEnchantments,
		copiedEnchantments
	}
}
