package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.BeaconFightManager;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

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
						executor.sendMessage(StringFormatter.Error(subCommand + " ist kein vorhandener Subcommand"));
						return false;
					}
				}else {
					executor.sendMessage(StringFormatter.Error("Mindestens ein Parameter benötigt"));
					return false;
				}			
			}else {
				executor.sendMessage(StringFormatter.Error("Fehlende Berechtigung für diesen Command"));
				return true;
			}
		}
		return false;
	}
	
	private boolean tryAddBeaconFight(Player executor, String[] args) {
		switch(args.length) {
			case 2:
				args = ArrayUtils.add(args, "90"); // default length of 90min			
			case 3:
				args = ArrayUtils.add(args, "15"); // default length per attack of 15min	
				break;
			case 4:
				break;
			default:
				executor.sendMessage(StringFormatter.Error("Der Command enthält nicht die richtige anzahl Parameter"));
				return false;
		}
		LocalDateTime startTime;
		Long eventDurationInMinutes;
		int attackDurationInMinutes;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
			startTime = LocalDateTime.parse(args[1], formatter);
		}catch(DateTimeParseException e) {
			executor.sendMessage(StringFormatter.Error("Der DateTime Parameter muss dem Format yyyy-MM-dd_HH:mm entsprechen"));
			return false;
		}
		try {
			eventDurationInMinutes = Long.parseLong(args[2]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.Error("Der Eventdauer Parameter muss dem Typ Long entsprechen"));
			return false;
		}
		if(eventDurationInMinutes < 1) {
			executor.sendMessage(StringFormatter.Error("Die Eventdauer muss mindestens 1min sein"));
			return false;
		}
		try {
			attackDurationInMinutes = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.Error("Der Angriffsdauer Parameter muss dem Typ Long entsprechen"));
			return false;
		}
		if(attackDurationInMinutes < 1) {
			executor.sendMessage(StringFormatter.Error("Die Angriffsdauer muss mindestens 1min sein"));
			return false;
		}
		if(beaconFightManager.tryAddBeaconFight(startTime, eventDurationInMinutes, attackDurationInMinutes)) {
			executor.sendMessage(ChatColor.GREEN + "Beaconevent erfolgreich hinzugefügt. Startet in " + ChatColor.RESET + ChronoUnit.MINUTES.between(LocalDateTime.now(), startTime) + " Minuten");
			return true;
		}else {
			executor.sendMessage(StringFormatter.Error("Beaconfight Event wurde nicht hinzugefügt. Möglicherweise war die Startzeit vor Jetzt, oder ein bestehender Event überschneidet die Zeit."));
			return false;
		}
	}

	private boolean tryRemoveBeaconFight(Player executor, String[] args) {
		boolean removed = beaconFightManager.tryRemoveActiveBeaconFight();
		if(removed) {
			executor.sendMessage(ChatColor.GREEN + "Beaconevent entfernt");
			return true;
		}else {
			executor.sendMessage(StringFormatter.Error("Es konnte kein Beaconfight entfernt werden"));
			return false;
		}
	}

}
