package com.github.calfur.beaconWars.disabling;

import java.time.LocalDateTime;
import java.util.UUID;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.configuration.IConfiguration;

public class TotemCooldown {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	
	private UUID playerId;
	private LocalDateTime expirationTime;
	
	public UUID getPlayerId() {
		return playerId;
	}
	
	public LocalDateTime getExpirationTime() {
		return expirationTime;
	}
	
	public TotemCooldown(UUID playerId) {
		this.playerId = playerId;
		this.expirationTime = LocalDateTime.now().plusMinutes(configuration.getTotemCooldownLengthInMinutes());
	}
	
}
