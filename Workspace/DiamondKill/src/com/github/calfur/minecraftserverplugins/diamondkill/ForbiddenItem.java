package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ForbiddenItem {
	private Material item;
	private ItemStack substitute;
	private boolean copyEnchantments = false;
	
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
	public boolean isCopyEnchantments() {
		return copyEnchantments;
	}
	public void setCopyEnchantments(boolean copyEnchantments) {
		this.copyEnchantments = copyEnchantments;
	}

	public ForbiddenItem(Material item, ItemStack substitute) {
		this.item = item;
		this.substitute = substitute;
	}
	public ForbiddenItem(Material item, ItemStack substitute, boolean copyEnchantments) {
		this(item, substitute);
		this.copyEnchantments = copyEnchantments;
	}
}
