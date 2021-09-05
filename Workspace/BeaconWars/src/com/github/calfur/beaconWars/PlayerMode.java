package com.github.calfur.beaconWars;

import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerMode {
	private String player;
	private boolean highlightedActive = false;
	private boolean buildModeActive = false;
	private LocalDateTime buildModeDeactivatedAt = LocalDateTime.now().minusMinutes(PlayerModeManager.buildModeCooldownInMinutes);
	private final static int secondsUntilBuildModeGetsDeactivatedWhenNotInBaseRange = 15;
	private int secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange = secondsUntilBuildModeGetsDeactivatedWhenNotInBaseRange;
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayerExact(player);
	}
	
	public int getSecondsUntilBuildModeGetsDeactivated() {
		return secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange;
	}
	
	public void reduceSecondsLeftUntilBuildModeGetsDeactivated(int seconds) {
		this.secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange -= seconds;
	}
	
	public void resetSecondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange() {
		secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange = secondsUntilBuildModeGetsDeactivatedWhenNotInBaseRange;		
	}
	
	public PlayerMode(String player) {
		this.player = player;
	}	
	
	public void activateBuildMode() {
		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 2));
		buildModeActive = true;
	}
	
	public void deactivateBuildMode() {
		getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		resetSecondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange();
		buildModeDeactivatedAt = LocalDateTime.now();
		buildModeActive = false;
	}

	public void activateHighlighted() {
		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 9999999, 0));
		highlightedActive = true;
	}
	
	public void deactivateHighlighted() {
		getPlayer().removePotionEffect(PotionEffectType.GLOWING);
		highlightedActive = false;
	}
	
	public boolean isBuildModeActive() {
		return buildModeActive;
	}
	
	public LocalDateTime getBuildModeDeactivatedAt() {
		return buildModeDeactivatedAt;
	}
	
	public void reloadEffects() {
		if(buildModeActive) {
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 2));
		}else {
			getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		}
		if(highlightedActive) {
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 9999999, 0));
		}else {
			getPlayer().removePotionEffect(PotionEffectType.GLOWING);
		}
	}
	
	public static void removeModeEffects(Player player) {
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		player.removePotionEffect(PotionEffectType.GLOWING);
	}
}
