package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

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

public class DiamondOreDisabler implements Listener {
	private List<Material> blocksWithDisabledDrops = Arrays.asList(
			Material.DIAMOND_ORE,
			Material.DEEPSLATE_DIAMOND_ORE
	);
	
	@EventHandler
	public void onDiamondOreBreaks(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(blocksWithDisabledDrops.contains(block.getType())) {			
			//event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.STONE, 1));
			event.setDropItems(false);
		}
	}	
	@EventHandler
	public void onDiamondOreExplodes(BlockExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(blocksWithDisabledDrops.contains(block.getType())) {
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
			if(blocksWithDisabledDrops.contains(block.getType())) {
				Bukkit.broadcastMessage("Jemand versuchte Diamanten zu sprengen. Nice Try!");
				event.setCancelled(true);
				return;
			}
		}
	}
}
