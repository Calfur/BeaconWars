package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.BeaconFightManager;

import net.md_5.bungee.api.ChatColor;

public class CommandBeaconFight implements CommandExecutor {
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(executor.hasPermission("admin")) {				
				if(args.length >= 1) {
					String subCommand = args[0].toLowerCase();
					switch(subCommand) {
					case "add":
						return tryAddBeaconFight(executor, args);
					case "remove":
					case "delete":
						return tryRemoveBeaconFight(executor, args);
					default:
						executor.sendMessage(ChatColor.RED + subCommand + " ist kein vorhandener Subcommand");
						return false;
					}
				}else {
					executor.sendMessage(ChatColor.RED + "Mindestens ein Parameter benötigt");
					return false;
				}			
			}else {
				executor.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
				return true;
			}
		}
		return false;
	}
	
	private boolean tryAddBeaconFight(Player executor, String[] args) {
		if(args.length == 2) {
			args = ArrayUtils.add(args, "90"); // default length of 90min
		}else if(args.length != 3) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		LocalDateTime startTime;
		Long durationInMinutes;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
			startTime = LocalDateTime.parse(args[1], formatter);
		}catch(DateTimeParseException e) {
			executor.sendMessage(ChatColor.RED + "Der DateTime Parameter muss dem Format yyyy-MM-dd_HH:mm entsprechen");
			return false;
		}
		try {
			durationInMinutes = Long.parseLong(args[2]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Teamnummer Parameter muss dem Typ Long entsprechen");
			return false;
		}
		if(durationInMinutes <= 0) {
			executor.sendMessage(ChatColor.RED + "Die Eventdauer muss mindestens 1min sein");
			return false;
		}
		if(beaconFightManager.tryAddBeaconFight(startTime, durationInMinutes)) {
			executor.sendMessage(ChatColor.GREEN + "Beaconevent erfolgreich hinzugefügt. Startet um " + args[1]);
			return true;
		}else {
			executor.sendMessage(ChatColor.RED + "Beaconfight Event wurde nicht hinzugefügt. Möglicherweise war die Startzeit vor Jetzt, oder ein bestehender Event überschneidet die Zeit.");
			return false;
		}
	}

	private boolean tryRemoveBeaconFight(Player executor, String[] args) {
		boolean removed = beaconFightManager.tryRemoveActiveBeaconFight();
		if(removed) {
			return true;
		}else {
			executor.sendMessage("Es konnte kein Beaconfight entfernt werden.");
			return false;
		}
	}

}
