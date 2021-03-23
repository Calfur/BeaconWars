package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.Bukkit;

public class BossBarProgresser implements Runnable{
	
	private CustomBossBar customBossBar;
	
	public BossBarProgresser(CustomBossBar customBossBar) {
		this.customBossBar = customBossBar;
	}
	
	@Override
	public void run() {
		double newProgress = customBossBar.getBossBar().getProgress() - customBossBar.getBossBarStepPerSecond();
		if(newProgress >= 0) {					
			customBossBar.getBossBar().setProgress(newProgress);
		}else {
			Bukkit.getScheduler().cancelTask(customBossBar.getTaskId());
			customBossBar.getManager().removeBossBar(customBossBar);
		}
	}
	
}
