package com.github.calfur.minecraftserverplugins.diamondkill.customTasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;

/***
 * Custom version of BukkitScheduler, doesn't get affected by server lags
 */
public class TaskScheduler {
	private static final TaskScheduler instance = new TaskScheduler();
	private HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
	private int lastTaskNumber = 0;
	
	private TaskScheduler(){
		startTaskWatcher();
	}
	
	public static TaskScheduler getInstance() {
		return instance;
	}
	
	/***
	 * Custom version of BukkitScheduler.scheduleSyncRepeatingTask
	 * @param plugin
	 * @param runnable
	 * @param firstExecution
	 * @param period in seconds
	 */	
	public int scheduleRepeatingTask(Plugin plugin, Runnable runnable, LocalDateTime firstExecution, long period) {
		return addTask(new RepeatingTask(plugin, runnable, firstExecution, period));
	}
	
	/***
	 * Custom version of BukkitScheduler.scheduleSyncDelayedTask
	 * @param plugin
	 * @param runnable
	 * @param executionTime
	 */	
	public int scheduleDelayedTask(Plugin plugin, Runnable runnable, LocalDateTime executionTime) {
		return addTask(new Task(plugin, runnable, executionTime));
	}	
	
	public void cancelTask(int taskId) {
		tasks.remove(taskId);
	}
	
	private int addTask(Task task) {
		lastTaskNumber++;
		tasks.put(lastTaskNumber, task);	
		return lastTaskNumber;
	}

	private void startTaskWatcher() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				tryRunTasks();				
			}

			
		}, 200, 200); //runs every 10 seconds
	}

	private void tryRunTasks() {
		ArrayList<Integer> taskKeysToDestroy = new ArrayList<Integer>();
		
		for (Entry<Integer, Task> task : tasks.entrySet()) {
			boolean destroyTask = task.getValue().tryRun();
			if(destroyTask) {
				taskKeysToDestroy.add(task.getKey());
			}
		}
		
		for (Integer taskKeyToDestroy : taskKeysToDestroy) {					
			tasks.remove(taskKeyToDestroy);
		}
	}
}
