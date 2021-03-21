package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent ;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class MoveBeaconIntoChestEvent implements Listener {
	private List<InventoryType> exceptedInventoryTypes = Arrays.asList(InventoryType.PLAYER, InventoryType.CREATIVE, InventoryType.CRAFTING);
	
	@EventHandler
	public void onPlayerTeleportsFromEnderPearl(InventoryClickEvent event) {
		ItemStack affectedItem = event.getCurrentItem();
		if(affectedItem == null) {
			return;
		}
		if(!exceptedInventoryTypes.contains(event.getInventory().getType())) {
			boolean beaconItemIsAffected = affectedItem.getType() == Material.BEACON;
			if(beaconItemIsAffected) {
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "In diesem Inventartyp kann der Beacon nicht verschoben werden");
				event.setCancelled(true);
				return;
			}
			boolean inventoryContainsBeacon = event.getWhoClicked().getInventory().contains(Material.BEACON) || event.getInventory().contains(Material.BEACON) || event.getWhoClicked().getInventory().getItemInOffHand().getType() == Material.BEACON;
			if((event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) && inventoryContainsBeacon) {
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "Während du einen Beacon im inventar hast, ist diese Funktion gesperrt");
				event.setCancelled(true);
				return;
			}
		}
	}
}
