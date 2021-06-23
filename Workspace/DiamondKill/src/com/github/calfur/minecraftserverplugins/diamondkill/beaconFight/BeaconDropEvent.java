package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class BeaconDropEvent implements Listener {

	@EventHandler
	public void onPlayerTeleportsFromEnderPearl(PlayerDropItemEvent event) {	
		if(event.getItemDrop().getItemStack().getType() == Material.BEACON) {
			event.getPlayer().sendMessage(StringFormatter.Error("Du kannst den Beacon nicht droppen"));
			event.setCancelled(true);
		}
	}
}
