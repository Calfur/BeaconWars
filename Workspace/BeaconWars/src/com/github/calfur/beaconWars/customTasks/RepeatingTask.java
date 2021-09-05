package com.github.calfur.beaconWars.customTasks;

import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class RepeatingTask extends Task{
	/***
	 * In seconds
	 */
	private long period;
	/***
	 * @param plugin
	 * @param runnable
	 * @param firstExecution
	 * @param period in seconds
	 */
	public RepeatingTask(Plugin plugin, Runnable runnable, LocalDateTime firstExecution, long period) {
		super(plugin, runnable, firstExecution);
		this.period = period;
	}
	
	@Override
	public boolean tryRun() {
		if(nextExecution.isBefore(LocalDateTime.now())) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable);
			nextExecution = nextExecution.plusSeconds(period);
		}
		return false;		
	}
}
