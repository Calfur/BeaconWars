package com.github.calfur.beaconWars.disabling;

import java.time.LocalDateTime;
import java.util.UUID;

public class TotemCooldown {
	public static final int cooldownLength = 10; // Length in Minutes
	
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
		this.expirationTime = LocalDateTime.now().plusMinutes(cooldownLength);
	}
	
}
