package com.github.calfur.beaconWars.beaconFight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.database.BeaconFightDb;
import com.github.calfur.beaconWars.database.BeaconFightJson;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class BeaconFightManager {
	private BeaconFightDb beaconFightDb = new BeaconFightDb();
	private List<BeaconFight> beaconFights = new ArrayList<BeaconFight>();
	private boolean isBeaconEventActive = false;
	
	public BeaconFightManager() {
		beaconFightDb.loadDatabase();
		Collection<BeaconFightJson> beaconFightJsons = beaconFightDb.getBeaconFights().values();
		for (BeaconFightJson beaconFightJson : beaconFightJsons) {
			if(beaconFightJson.getStartTime().isBefore(LocalDateTime.now())){
				beaconFightDb.removeBeaconFight(beaconFightJson.getStartTime());
			}else {
				beaconFights.add(new BeaconFight(this, beaconFightJson));
			}
		}
	}
	
	public boolean tryAddBeaconFight(BeaconFightJson beaconFightJson) {
		if(beaconFightJson.getStartTime().isBefore(LocalDateTime.now())){
			return false;
		}
		
		if(isTimeOverlappingWithAnotherEvent(
				beaconFightJson.getStartTime(), 
				beaconFightJson.getStartTime().plusMinutes(beaconFightJson.getEventDurationInMinutes())
		)) {
			return false;
		}
		beaconFightDb.addBeaconFight(beaconFightJson);
		beaconFights.add(new BeaconFight(this, beaconFightJson));
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
			beaconFight.cancelOngoingBeaconFight();
			return true;
		}else {
			beaconFight = getNextWaitingBeaconFight();
			if(beaconFight != null) {
				beaconFight.cancelBeaconFightBeforeStarted();
				removeNotStartedBeaconFight(beaconFight);
				return true;
			}else {
				return false;
			}
		}
	}
	
	public void removeOngoingBeaconFight(BeaconFight beaconFight) {
		isBeaconEventActive = false;
		removeBeaconFight(beaconFight);
	}
	
	private void removeNotStartedBeaconFight(BeaconFight beaconFight) {
		removeBeaconFight(beaconFight);
	}
	
	private void removeBeaconFight(BeaconFight beaconFight) {
		beaconFights.remove(beaconFight);
		beaconFightDb.removeBeaconFight(beaconFight.getStartTime());
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
	
	public boolean isBeaconEventActive() {
		return isBeaconEventActive;
	}
	
	public boolean addBeaconBreak(Player destructor, Location beaconLocation) {
		BeaconFight ongoingBeaconFight = getOngoingBeaconFight();
		if(ongoingBeaconFight == null) {
			Bukkit.broadcastMessage(StringFormatter.error("ERROR: Beacon abgebaut obwohl kein Beaconevent aktiv ist!"));
			return false;
		}
		return addBeaconBreak(destructor, beaconLocation, ongoingBeaconFight);
	}
	
	private boolean addBeaconBreak(Player destructor, Location beaconLocation, BeaconFight beaconFight) {
		HashMap<Integer, ItemStack> notAddedItems = destructor.getInventory().addItem(new ItemStack(Material.BEACON, 1));
		if(notAddedItems.size() != 0) {
			destructor.sendMessage(StringFormatter.error("Keinen freien Platz im Inventar gefunden, Beacon konnte nicht abgebaut werden"));
			return false;
		}
		beaconFight.addBeaconDestruction(destructor, beaconLocation);
		return true;
	}
}
