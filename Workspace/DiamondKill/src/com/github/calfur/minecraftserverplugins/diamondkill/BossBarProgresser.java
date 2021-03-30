package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.scheduler.BukkitRunnable;

public class BossBarProgresser extends BukkitRunnable{
	
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
			this.cancel();
			customBossBar.getManager().removeBossBar(customBossBar);
		}
	}
	
}
