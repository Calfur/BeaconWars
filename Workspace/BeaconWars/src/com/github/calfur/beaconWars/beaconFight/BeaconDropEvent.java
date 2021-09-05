package com.github.calfur.beaconWars.beaconFight;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class BeaconDropEvent implements Listener {

	@EventHandler
	public void onPlayerDropsItem(PlayerDropItemEvent event) {	
		if(event.getItemDrop().getItemStack().getType() == Material.BEACON) {
			event.getPlayer().sendMessage(StringFormatter.error("Du kannst den Beacon nicht droppen"));
			event.setCancelled(true);
		}
	}
}
