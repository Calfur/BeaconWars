package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class BeaconFightManager {
	private List<BeaconFight> beaconFights = new ArrayList<BeaconFight>();
	private boolean isBeaconEventActive = false;
	
	public boolean tryAddBeaconFight(LocalDateTime startTime) {
		// TODO: validation
		beaconFights.add(new BeaconFight(startTime, this));
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
		return true;
	}
	
	public boolean tryRemoveActiveBeaconFight() {
		BeaconFight beaconFight = getOngoingBeaconFight();
		if(beaconFight != null) {
			beaconFight.cancelBeaconFightEvent();
			RemoveBeaconFight(beaconFight);
			return true;
		}else {
			beaconFight = getNextWaitingBeaconFight();
			if(beaconFight != null) {
				RemoveBeaconFight(beaconFight);
				return true;
			}else {
				return false;
			}
		}
	}
	
	private void RemoveBeaconFight(BeaconFight beaconFight) {
		beaconFights.remove(beaconFight);
		Main.getInstance().getScoreboardLoader().reloadScoreboardForAllOnlinePlayers();
	}

	public BeaconFight getOngoingBeaconFight() {
		for (BeaconFight beaconFight : beaconFights) {
			if(beaconFight.getStartTime().isBefore(LocalDateTime.now())) {
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
			}else if(beaconFight.getStartTime().isAfter(nextWaitingBeaconFight.getStartTime())) {
				nextWaitingBeaconFight = beaconFight;
			}
		}
		return nextWaitingBeaconFight;
	}

	public void activateBeaconFightEvent() {
		isBeaconEventActive = true;
	}
	
	public void deactivateBeaconFightEvent() {
		isBeaconEventActive = false;
	}
	
	public boolean isBeaconEventActive() {
		return isBeaconEventActive;
	}
}
