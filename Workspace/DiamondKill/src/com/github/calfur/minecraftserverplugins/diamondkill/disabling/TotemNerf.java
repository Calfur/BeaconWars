package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class TotemNerf implements Listener {
	private List<TotemCooldown> totemCooldowns = new ArrayList<TotemCooldown>();
	
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
				Bukkit.broadcastMessage(player.getName() + " hat ein Totem verwendet. " + ChatColor.BOLD + TotemCooldown.cooldownLength + "min" + ChatColor.RESET + " Totem-Cooldown aktiviert.");
				player.sendMessage("Benutze " + ChatColor.BOLD + "/totemcooldown" + ChatColor.RESET + " um die verbleibende Zeit anzuzeigen");
			}
		}
	}

	private TotemCooldown getTotemcooldown(UUID uuid) {
		for (TotemCooldown totemCooldown : totemCooldowns) {
			if(totemCooldown.getPlayerId() == uuid) {
				return totemCooldown;
			}
		}
		return null;
	}
}
