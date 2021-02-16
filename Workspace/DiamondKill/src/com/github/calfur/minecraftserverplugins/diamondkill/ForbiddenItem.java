package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ForbiddenItem {
	private Material item;
	private ItemStack substitute;
	
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
	public ForbiddenItem(Material item, ItemStack substitute) {
		this.item = item;
		this.substitute = substitute;
	}
}
