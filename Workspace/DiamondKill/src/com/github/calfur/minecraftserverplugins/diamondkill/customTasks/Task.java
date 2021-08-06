package com.github.calfur.minecraftserverplugins.diamondkill.customTasks;

import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Task {
	protected Plugin plugin;
	protected Runnable runnable;
	protected LocalDateTime nextExecution;
	
		public Task(Plugin plugin, Runnable runnable, LocalDateTime firstExecution) {
		this.plugin = plugin;
		this.runnable = runnable;
		this.nextExecution = firstExecution;
	}
	
	public boolean tryRun() {
		if(nextExecution.isBefore(LocalDateTime.now())) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable);
			return true;
		}
		return false;
	}
}
