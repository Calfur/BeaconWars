package com.github.calfur.beaconWars.pvp;

import java.time.LocalDateTime;

public class LatestHitByPlayer {
	private String defender;
	private String attacker;
	private LocalDateTime dateTime;
	
	public String getDefender() {
		return defender;
	}
	public String getAttacker() {
		return attacker;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public LatestHitByPlayer(String defender, String attacker) {
		this.defender = defender;
		this.attacker = attacker;
		this.dateTime = LocalDateTime.now();
	}
	
}
