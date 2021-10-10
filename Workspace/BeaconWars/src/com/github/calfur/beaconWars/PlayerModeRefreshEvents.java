package com.github.calfur.beaconWars;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerModeRefreshEvents implements Listener {
	private PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
	private List<PotionEffectType> unremovableEffects = Arrays.asList(PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.GLOWING);

	@EventHandler
	public void onEffectGetsChangedBecauseOfMilk(EntityPotionEffectEvent event) {
		if(event.getCause() == EntityPotionEffectEvent.Cause.MILK) {
			PotionEffectType oldEffect = event.getOldEffect().getType();
			if(unremovableEffects.contains(oldEffect)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEffectGetsChangedBecauseOfTotem(EntityResurrectEvent event) {
		if(!event.isCancelled()) {			
			if(event.getEntityType() == EntityType.PLAYER) {
				Player player = (Player)event.getEntity();
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
	
					@Override
					public void run() {
						playerModeManager.reloadPlayerMode(player);
					}
					
				});
			}
		}
	}
}
