package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class TotemNerf implements Listener {
	private static List<TotemCooldown> totemCooldowns = new ArrayList<TotemCooldown>();
	
	public static long getRemainingCooldownSeconds(UUID uuid) {
		TotemCooldown totemCooldown = getTotemcooldown(uuid);
		if(totemCooldown != null) {			
			long cooldownSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), totemCooldown.getExpirationTime());
			if(cooldownSeconds > 0) {
				return cooldownSeconds;
			}
		}
		return 0;
	}
	
	@EventHandler
	public void onEntityDie(EntityResurrectEvent event) {
		if(!event.isCancelled()) {			
			if(event.getEntityType() == EntityType.PLAYER) {
				Player player = (Player)event.getEntity();
				UUID uuid = player.getUniqueId();
				TotemCooldown totemCooldown = getTotemcooldown(uuid);
				if(totemCooldown != null) {
					if(totemCooldown.getExpirationTime().isAfter(LocalDateTime.now())) {
						event.setCancelled(true);
						return;
					}
					totemCooldowns.remove(totemCooldown);
				}
				totemCooldowns.add(new TotemCooldown(uuid));
				Bukkit.broadcastMessage(player.getDisplayName() + " hat ein Totem verwendet. " + StringFormatter.Bold(TotemCooldown.cooldownLength + "min") + " Totem-Cooldown aktiviert.");
				player.sendMessage("Benutze " + StringFormatter.Bold("/totemcooldown") + " um die verbleibende Zeit anzuzeigen");
			}
		}
	}

	private static TotemCooldown getTotemcooldown(UUID uuid) {
		for (TotemCooldown totemCooldown : totemCooldowns) {
			if(totemCooldown.getPlayerId() == uuid) {
				return totemCooldown;
			}
		}
		return null;
	}
}
