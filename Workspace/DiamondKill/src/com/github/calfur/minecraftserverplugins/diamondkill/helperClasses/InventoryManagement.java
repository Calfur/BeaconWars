package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryManagement {

	public static void renameItem(ItemStack itemStack, String name) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
	}
	
	public static boolean isInventoryFull(PlayerInventory inventory) {
		return inventory.firstEmpty() == -1;
	}
}
