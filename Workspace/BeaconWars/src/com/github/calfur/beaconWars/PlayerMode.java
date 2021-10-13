package com.github.calfur.beaconWars;

import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.calfur.beaconWars.configuration.ConstantConfiguration;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.PlayerJson;

public class PlayerMode {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	
	private final static PotionEffect buildModePotionEffect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, 2);
	private final static PotionEffect highlightPotionEffect = new PotionEffect(PotionEffectType.GLOWING, 9999999, 0);
	
	private String playerName;
	private boolean highlightedActive = false;
	private boolean isBuildModeActive = false;
	private LocalDateTime buildModeCooldownEnd = LocalDateTime.now().minusMinutes(configuration.getBuildModeCooldownInMinutes());
	private int secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange = ConstantConfiguration.secondsUntilBuildModeGetsDeactivatedWhenNotInBaseRange;

	public boolean isBuildModeActive() {
		return isBuildModeActive;
	}
	
	public LocalDateTime getBuildModeCooldownEnd() {
		return buildModeCooldownEnd;
	}

	public Player getPlayer() {
		return Bukkit.getServer().getPlayerExact(playerName);
	}
	
	public int getSecondsUntilBuildModeGetsDeactivated() {
		return secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange;
	}
	
	public void reduceSecondsLeftUntilBuildModeGetsDeactivated(int seconds) {
		this.secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange -= seconds;
	}
	
	public void resetSecondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange() {
		secondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange = ConstantConfiguration.secondsUntilBuildModeGetsDeactivatedWhenNotInBaseRange;		
	}
	
	public PlayerMode(String playerName) {
		this.playerName = playerName;
		
		PlayerJson playerJson = playerDbConnection.getPlayer(playerName);
		this.isBuildModeActive = playerJson.isBuildModeActive();
		this.buildModeCooldownEnd = playerJson.getBuildModeCooldownEnd();
		
		reloadEffects();
	}	
	
	public void activateBuildMode() {
		addPotionEffect(buildModePotionEffect);
		isBuildModeActive = true;

		PlayerJson playerJson = playerDbConnection.getPlayer(playerName);
		playerJson.setBuildModeActive(true);
		playerDbConnection.addPlayer(playerName, playerJson);
	}
	
	public void deactivateBuildMode() {
		buildModeCooldownEnd = LocalDateTime.now().plusMinutes(configuration.getBuildModeCooldownInMinutes());
		resetSecondsLeftUntilBuildModeGetsDeactivatedBecauseNotInBaseRange();
		isBuildModeActive = false;
		removePotionEffect(buildModePotionEffect.getType());		
		
		PlayerJson playerJson = playerDbConnection.getPlayer(playerName);
		playerJson.setBuildModeActive(false);
		playerJson.setBuildModeCooldownEnd(buildModeCooldownEnd);
		playerDbConnection.addPlayer(playerName, playerJson);
	}

	public void activateHighlighted() {
		addPotionEffect(highlightPotionEffect);
		highlightedActive = true;
	}
	
	public void deactivateHighlighted() {
		removePotionEffect(highlightPotionEffect.getType());
		highlightedActive = false;
	}
	
	public void reloadEffects() {
		if(isBuildModeActive) {
			addPotionEffect(buildModePotionEffect);
		}else {
			removePotionEffect(buildModePotionEffect.getType());
		}
		if(highlightedActive) {
			addPotionEffect(highlightPotionEffect);
		}else {
			removePotionEffect(highlightPotionEffect.getType());
		}
	}
	
	public static void removeModeEffects(Player player) {
		player.removePotionEffect(buildModePotionEffect.getType());
		player.removePotionEffect(highlightPotionEffect.getType());
	}
	
	private void addPotionEffect(PotionEffect potionEffect) {
		Player player = getPlayer();
		if(player != null) {
			player.addPotionEffect(potionEffect);			
		}
	}
	
	private void removePotionEffect(PotionEffectType type) {
		Player player = getPlayer();
		if(player != null) {
			player.removePotionEffect(type);			
		}
	}
}
