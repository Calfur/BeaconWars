package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.md_5.bungee.api.ChatColor;

public class BlockBreakEvents implements Listener {
	private List<Material> unbreakableBlocks = Arrays.asList(Material.NETHERITE_BLOCK, Material.BEACON);
	
	@EventHandler
	public void onBlockBreaks(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(unbreakableBlocks.contains(block.getType())) {
			if(block.getType() == Material.BEACON) {
				if(Main.getInstance().getBeaconFightManager().isBeaconEventActive()) {
					Player player = event.getPlayer();
					Location location = event.getBlock().getLocation();
					if(BeaconManager.isBeaconFromAnotherTeam(player, location)) {						
						Main.getInstance().getBeaconFightManager().addBeaconBreak(player, location);
						return;
					}else {
						event.getPlayer().sendMessage(ChatColor.RED + "Du kannst deinen eigenen Beacon nicht abbauen");
					}
				}				
			}
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