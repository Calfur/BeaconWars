package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public class MilkEvents implements Listener {
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
}
