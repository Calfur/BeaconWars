package com.github.calfur.beaconWars;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;

public class BossBarProgresser implements Runnable{
	
	private CustomBossBar customBossBar;
	
	public BossBarProgresser(CustomBossBar customBossBar) {
		this.customBossBar = customBossBar;
	}
	
	@Override
	public void run() {
		double timePassed = ChronoUnit.SECONDS.between(customBossBar.getCountdownStart(), LocalDateTime.now());
		double newProgress;
		if(timePassed != 0) {					
			newProgress = 1 - (1 / (customBossBar.getCountdownDuration() / timePassed));
		}else {
			newProgress = 1;
		}
		if(newProgress >= 0) {					
			customBossBar.getBossBar().setProgress(newProgress);
		}else {
			Bukkit.getScheduler().cancelTask(customBossBar.getTaskId());
			customBossBar.getManager().removeBossBar(customBossBar);
		}
	}
	
}
