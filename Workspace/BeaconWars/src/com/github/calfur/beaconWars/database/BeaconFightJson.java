package com.github.calfur.beaconWars.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class BeaconFightJson implements ConfigurationSerializable {
	private LocalDateTime startTime;
	private int eventDurationInMinutes;
	private int raidDurationInMinutes;

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public int getEventDurationInMinutes() {
		return eventDurationInMinutes;
	}

	public void setEventDurationInMinutes(int minutes) {
		this.eventDurationInMinutes = minutes;
	}

	public int getRaidDurationInMinutes() {
		return raidDurationInMinutes;
	}

	public void setRaidDurationInMinutes(int minutes) {
		this.raidDurationInMinutes = minutes;
	}
	
	public BeaconFightJson(LocalDateTime startTime, int eventDurationInMinutes, int raidDurationInMinutes) {
		this.startTime = startTime;
		this.eventDurationInMinutes = eventDurationInMinutes;
		this.raidDurationInMinutes = raidDurationInMinutes;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("startTime", this.startTime.toString());
		data.put("eventDurationInMinutes", this.eventDurationInMinutes);
		data.put("raidDurationInMinutes", this.raidDurationInMinutes);
		return data;
	}
	
	public static BeaconFightJson deserialize(Map<String, Object> args) {
		return new BeaconFightJson(
			LocalDateTime.parse((CharSequence) args.get("startTime")),
			NumberConversions.toInt(args.get("eventDurationInMinutes")), 
			NumberConversions.toInt(args.get("raidDurationInMinutes"))
		);
	}
}
