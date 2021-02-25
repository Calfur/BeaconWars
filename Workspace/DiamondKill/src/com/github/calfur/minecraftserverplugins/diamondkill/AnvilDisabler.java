package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilDisabler implements Listener{
	private List<Material> anvilTypes = Arrays.asList(Material.ANVIL, Material.DAMAGED_ANVIL, Material.CHIPPED_ANVIL);
	
	@EventHandler
	public void onAnvilBreaks(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(anvilTypes.contains(block.getType())) {			
			event.setDropItems(false);
			Bukkit.broadcastMessage("Jemand versuchte einen Amboss abzubauen. Nice Try!");
		}
	}	
	@EventHandler
	public void onAnvilExplodes(BlockExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(anvilTypes.contains(block.getType())) {
				Bukkit.broadcastMessage("Jemand versuchte einen Amboss zu sprengen. Nice Try!");
				event.setYield(0);
				return;
			}
		}
	}	
	@EventHandler
	public void onAnvilExplodes(EntityExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(anvilTypes.contains(block.getType())) {
				Bukkit.broadcastMessage("Jemand versuchte einen Amboss zu sprengen. Nice Try!");
				event.setYield(0);
				return;
			}
		}
	}
	@EventHandler
	public void onAnvilInventoryOpens(InventoryOpenEvent event) {
		if (event.getInventory().getType() == InventoryType.ANVIL) {
			event.setCancelled(true);
		}
	}
}