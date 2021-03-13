package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class BeaconFightManager {
	private List<BeaconFight> beaconFights = new ArrayList<BeaconFight>();
	private boolean isBeaconEventActive = false;
	
	public boolean tryAddBeaconFight(LocalDateTime startTime, long durationInMinutes) {
		if(startTime.isBefore(LocalDateTime.now())){
			return false;
		}
		if(isTimeOverlappingWithAnotherEvent(startTime, startTime.plusMinutes(durationInMinutes))) {
			return false;
		}
		beaconFights.add(new BeaconFight(startTime, durationInMinutes, this));
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
		return true;
	}
	
	private boolean isTimeOverlappingWithAnotherEvent(LocalDateTime startTime, LocalDateTime endTime) {
		for (BeaconFight beaconFight : beaconFights) {
			LocalDateTime otherStartTime = beaconFight.getStartTime();
			LocalDateTime otherEndTime = beaconFight.getEndTime();
			if((startTime.isAfter(otherStartTime) && startTime.isBefore(otherEndTime)) || 
			   (endTime.isAfter(otherStartTime) && endTime.isBefore(otherEndTime)) || 
			   (startTime.isBefore(otherStartTime) && endTime.isAfter(otherEndTime)) || 
			   (startTime.isEqual(otherStartTime)) || 
			   (startTime.isEqual(otherEndTime)) || 
			   (endTime.isEqual(otherStartTime)) || 
			   (endTime.isEqual(otherEndTime))){
				return true;
			}
		}
		return false;
	}

	public boolean tryRemoveActiveBeaconFight() {
		BeaconFight beaconFight = getOngoingBeaconFight();
		if(beaconFight != null) {
			beaconFight.cancelBeaconFightEvent();
			return true;
		}else {
			beaconFight = getNextWaitingBeaconFight();
			if(beaconFight != null) {
				removeBeaconFight(beaconFight);
				return true;
			}else {
				return false;
			}
		}
	}
	
	private void removeBeaconFight(BeaconFight beaconFight) {
		beaconFights.remove(beaconFight);
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
	}

	public BeaconFight getOngoingBeaconFight() {
		for (BeaconFight beaconFight : beaconFights) {
			LocalDateTime startTime = beaconFight.getStartTime().minusSeconds(10);
			if(startTime.isBefore(LocalDateTime.now())) {
				return beaconFight;
			}
		}
		return null;
	}

	public BeaconFight getNextWaitingBeaconFight() {
		BeaconFight nextWaitingBeaconFight = null;
		for (BeaconFight beaconFight : beaconFights) {
			if(nextWaitingBeaconFight == null) {
				nextWaitingBeaconFight = beaconFight;
			}else if(beaconFight.getStartTime().isBefore(nextWaitingBeaconFight.getStartTime())) {
				nextWaitingBeaconFight = beaconFight;
			}
		}
		return nextWaitingBeaconFight;
	}

	public void activateBeaconFightEvent() {
		isBeaconEventActive = true;
	}
	
	public void deactivateBeaconFightEvent(BeaconFight beaconFight) {
		removeBeaconFight(beaconFight);
		isBeaconEventActive = false;
	}
	
	public boolean isBeaconEventActive() {
		return isBeaconEventActive;
	}
}
