package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarProgresser extends BukkitRunnable{
	private BossBarProgresser instance = this;
	
	private CustomBossBar customBossBar;
	
	public BossBarProgresser(CustomBossBar customBossBar) {
		this.customBossBar = customBossBar;
	}
	
	@Override
	public void run() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			
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
					instance.cancel();
					customBossBar.getManager().removeBossBar(customBossBar);
				}
			}
			
		});
	}
	
}
