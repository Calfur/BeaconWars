package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.calfur.minecraftserverplugins.diamondkill.CompassManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class CompassRightClickEvent implements Listener  {
	
	CompassManager compassManager = Main.getInstance().getCompassManager();
	
	@EventHandler
	public void onPlayerTeleportsFromEnderPearl(PlayerInteractEvent event) {	
		Player player = event.getPlayer();
		
		if(event.getMaterial() == Material.COMPASS && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			compassManager.updateCompass(player, event.getItem());
		}
	}
}
