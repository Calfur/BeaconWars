package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerMode {
	private Player player;
	private boolean buildModeActive = false;
	private LocalDateTime deactivatedAt;
	
	public PlayerMode(Player player) {
		this.player = player;
	}	

	public void deactivateBuildMode() {
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);	
		deactivatedAt = LocalDateTime.now();
		buildModeActive = false;
	}
	
	public void activateBuildMode() {
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 2));
		buildModeActive = true;
	}
	
	public boolean isBuildModeActive() {
		return buildModeActive;
	}
	
	public LocalDateTime getDeactivatedAt() {
		return deactivatedAt;
	}
}
