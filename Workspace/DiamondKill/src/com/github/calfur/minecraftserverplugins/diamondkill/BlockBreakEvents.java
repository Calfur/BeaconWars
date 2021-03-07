package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockBreakEvents implements Listener {
	private List<Material> unbreakableBlocks = Arrays.asList(Material.NETHERITE_BLOCK, Material.BEACON);
	
	@EventHandler
	public void onBlockBreaks(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(unbreakableBlocks.contains(block.getType())) {			
			event.setCancelled(true);
		}
	}	
	@EventHandler
	public void onBlockExplodes(BlockExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(unbreakableBlocks.contains(block.getType())) {
				event.setCancelled(true);
				return;
			}
		}
	}	
	@EventHandler
	public void onBlockExplodes(EntityExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(unbreakableBlocks.contains(block.getType())) {
				event.setCancelled(true);
				return;
			}
		}
	}
}