package com.github.calfur.beaconWars.commands;

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

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.configuration.ConstantConfiguration;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

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
						executor.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
						return false;
					}
				}else {
					executor.sendMessage(StringFormatter.error("Mindestens ein Parameter ben�tigt"));
					return false;
				}			
			}else {
				executor.sendMessage(StringFormatter.error("Fehlende Berechtigung f�r diesen Command"));
				return true;
			}
		}
		return false;
	}
	
	private boolean tryAddBeaconFight(Player executor, String[] args) {
		switch(args.length) {
			case 2:
				args = ArrayUtils.add(args, Integer.toString(ConstantConfiguration.defaultBeaconFightLength));			
			case 3:
				args = ArrayUtils.add(args, Integer.toString(ConstantConfiguration.defaultBeaconRaidLength));	
				break;
			case 4:
				break;
			default:
				executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige Anzahl Parameter"));
				return false;
		}
		LocalDateTime startTime;
		Long eventDurationInMinutes;
		int attackDurationInMinutes;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
			startTime = LocalDateTime.parse(args[1], formatter);
		}catch(DateTimeParseException e) {
			executor.sendMessage(StringFormatter.error("Der DateTime Parameter muss dem Format yyyy-MM-dd_HH:mm entsprechen"));
			return false;
		}
		try {
			eventDurationInMinutes = Long.parseLong(args[2]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Eventdauer Parameter muss dem Typ Long entsprechen"));
			return false;
		}
		if(eventDurationInMinutes < 1) {
			executor.sendMessage(StringFormatter.error("Die Eventdauer muss mindestens 1min sein"));
			return false;
		}
		try {
			attackDurationInMinutes = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Angriffsdauer Parameter muss dem Typ Long entsprechen"));
			return false;
		}
		if(attackDurationInMinutes < 1) {
			executor.sendMessage(StringFormatter.error("Die Angriffsdauer muss mindestens 1min sein"));
			return false;
		}
		if(beaconFightManager.tryAddBeaconFight(startTime, eventDurationInMinutes, attackDurationInMinutes)) {
			executor.sendMessage(ChatColor.GREEN + "Beaconevent erfolgreich hinzugef�gt. Startet in " + ChatColor.RESET + ChronoUnit.MINUTES.between(LocalDateTime.now(), startTime) + " Minuten");
			return true;
		}else {
			executor.sendMessage(StringFormatter.error("Beaconfight Event wurde nicht hinzugef�gt. M�glicherweise war die Startzeit vor Jetzt, oder ein bestehendes Event �berschneidet die Zeit."));
			return false;
		}
	}

	private boolean tryRemoveBeaconFight(Player executor, String[] args) {
		boolean removed = beaconFightManager.tryRemoveActiveBeaconFight();
		if(removed) {
			executor.sendMessage(ChatColor.GREEN + "Beaconevent entfernt");
			return true;
		}else {
			executor.sendMessage(StringFormatter.error("Es konnte kein Beaconfight entfernt werden"));
			return false;
		}
	}

}
