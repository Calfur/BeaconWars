package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

public class NaturalDiamondDisabler implements Listener {
	@EventHandler
	public void onDiamondOreBreaks(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(block.getType() == Material.DIAMOND_ORE) {			
			//event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.STONE, 1));
			event.setDropItems(false);
		}
	}	
	@EventHandler
	public void onDiamondOreExplodes(BlockExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(block.getType() == Material.DIAMOND_ORE) {
				Bukkit.broadcastMessage("Jemand versuchte Diamanten zu sprengen. Nice Try!");
				event.setCancelled(true);
				return;
			}
		}
	}	
	@EventHandler
	public void onDiamondOreExplodes(EntityExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(block.getType() == Material.DIAMOND_ORE) {
				Bukkit.broadcastMessage("Jemand versuchte Diamanten zu sprengen. Nice Try!");
				event.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler
	public void onDiamondLootGenerate(LootGenerateEvent event) {
		List<ItemStack> loot = event.getLoot();
		boolean containsDiamondstuff = false;
		for(ItemStack itemStack : loot){
			if(itemStack.getType() == Material.DIAMOND) {
				containsDiamondstuff = true;
				break;
			}
		}
		if(containsDiamondstuff) {	
			Bukkit.broadcastMessage("Loot gelöscht, diamant enthalten");
			event.setLoot(null);
		}
	}
}
