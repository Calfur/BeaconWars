package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;

public class BeaconFight {
	private LocalDateTime startTime;
	private static final long durationInMinutes = 90;

	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public LocalDateTime getEndTime() {
		return startTime.plusMinutes(durationInMinutes);
	}
	
	public BeaconFight(LocalDateTime startTime) {
		this.startTime = startTime;
	}

}
