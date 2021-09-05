package com.github.calfur.beaconWars;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnEvents implements Listener {
	private PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				playerModeManager.reloadPlayerMode(player);			
			}
			
		}, 20);
	}
}
