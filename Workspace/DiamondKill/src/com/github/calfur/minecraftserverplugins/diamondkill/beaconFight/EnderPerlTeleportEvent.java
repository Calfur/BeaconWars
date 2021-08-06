package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class EnderPerlTeleportEvent implements Listener {
	
	@EventHandler
	public void onPlayerTeleportsFromEnderPearl(PlayerInteractEvent event) {	
		Player player = event.getPlayer();
		
		if(event.getMaterial() == Material.ENDER_PEARL && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Inventory inventory = player.getInventory();
			if(inventory.contains(Material.BEACON)) {
				player.sendMessage(StringFormatter.Error("Du kannst keine Enderperle benutzen während du einen Beacon im Inventar hast"));
				event.setCancelled(true);
				return;
			}
		}
	}
}
