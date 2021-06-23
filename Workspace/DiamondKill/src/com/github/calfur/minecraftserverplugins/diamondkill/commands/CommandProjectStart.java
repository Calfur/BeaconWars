package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandProjectStart implements CommandExecutor {
	private boolean isProjectActive = true;
	private Integer countdownTaskId;
	private int countdownLengthInSeconds = 10;
	
	public boolean isProjectActive() {
		return isProjectActive;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(executor.hasPermission("admin")) {			
				return toggleProject(executor);
			}else {
				executor.sendMessage(StringFormatter.Error("Fehlende Berechtigung für diesen command"));
				return false;
			}
		}
		return false;
	}

	private boolean toggleProject(Player executor) {
		if(isProjectActive) {
			return deactivateProject(executor);
		}else {
			return activateProject(executor);
		}
	}

	private boolean activateProject(Player executor) {
		if(countdownTaskId == null) {
			countdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
				private int secondsSinceCountdownStarted = 0;
				
				@Override
				public void run() {
					Bukkit.broadcastMessage(ChatColor.GOLD + "Projektstart in " + ChatColor.RESET + (countdownLengthInSeconds - secondsSinceCountdownStarted));
					if(secondsSinceCountdownStarted < countdownLengthInSeconds) {
						secondsSinceCountdownStarted += 1;
					}else {
						stopCountdown();
						startProject();
					}				
				}
				
			}, 0, 20);
		}else {			
			stopCountdown();
		}
		return true;
	}

	private void stopCountdown() {
		Bukkit.getScheduler().cancelTask(countdownTaskId);
		countdownTaskId = null;
	}

	private void startProject() {
		isProjectActive = true;		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty normal");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule announceAdvancements false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle true");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning true");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doWeatherCycle true");
		
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.GOLD + (ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxx"));
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.AQUA + "Lets go!");
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.GOLD + (ChatColor.MAGIC + "xxxxxxxxxxxxxxxxxxxxxxxx"));
		Bukkit.broadcastMessage(" ");
		CommandStartProjectForPlayers.startProjectForAllOnlinePlayers();
	}

	private boolean deactivateProject(Player executor) {
		executor.sendMessage(ChatColor.GOLD + "Das Projekt ist nun im Startmodus. Führe den Command erneut aus, um den Countdown zu starten");
		isProjectActive = false;
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty peaceful");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doWeatherCycle false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
		return true;
	}

}
