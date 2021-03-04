package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerMode {
	private String player;
	private boolean buildModeActive = false;
	private LocalDateTime deactivatedAt;
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

	public void deactivateBuildMode() {
		getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		resetSecondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange();
		deactivatedAt = LocalDateTime.now();
		buildModeActive = false;
	}
	
	public void activateBuildMode() {
		getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 2));
		buildModeActive = true;
	}
	
	public boolean isBuildModeActive() {
		return buildModeActive;
	}
	
	public LocalDateTime getDeactivatedAt() {
		return deactivatedAt;
	}
	
	public void reloadPotionEffects() {
		if(buildModeActive) {
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 2));
		}else {
			getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		}
	}
	
	public static void reloadPotionEffects(Player player) {
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
	}
}
