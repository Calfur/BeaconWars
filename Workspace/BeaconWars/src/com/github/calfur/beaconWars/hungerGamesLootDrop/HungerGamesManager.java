package com.github.calfur.beaconWars.hungerGamesLootDrop;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.customTasks.TaskScheduler;

public class HungerGamesManager {
	public void startItemSpawner() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime fullHour = LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0);
		
		long secondsUntilFullHour = ChronoUnit.SECONDS.between(now, fullHour);
		if(secondsUntilFullHour < 300) {
			fullHour = fullHour.plusHours(1);
			secondsUntilFullHour = ChronoUnit.SECONDS.between(now, fullHour);
		}
		
		TaskScheduler.getInstance().scheduleRepeatingTask(Main.getInstance(), 
				new ItemSpawner(), 
				fullHour, 
				3600);
		
		TaskScheduler.getInstance().scheduleRepeatingTask(Main.getInstance(), 
				new ItemSpawnAnnouncer(), 
				fullHour.minusMinutes(5), 
				3600);
	}
}
