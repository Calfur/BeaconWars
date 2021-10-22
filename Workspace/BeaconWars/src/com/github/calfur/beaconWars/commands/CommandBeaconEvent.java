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

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.configuration.ConstantConfiguration;
import com.github.calfur.beaconWars.database.BeaconFightJson;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandBeaconEvent implements CommandExecutor {
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length >= 1) {
			String subCommand = args[0].toLowerCase();
			switch(subCommand) {
			case "add":
				return tryAddBeaconFight(sender, args);
			case "remove":
			case "delete":
				return tryRemoveBeaconFight(sender, args);
			default:
				sender.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
				return false;
			}
		}else {
			sender.sendMessage(StringFormatter.error("Mindestens ein Parameter benötigt"));
			return false;
		}
	}
	
	private boolean tryAddBeaconFight(CommandSender sender, String[] args) {
		switch(args.length) {
			case 2:
				args = ArrayUtils.add(args, Integer.toString(ConstantConfiguration.defaultBeaconFightLength));			
			case 3:
				args = ArrayUtils.add(args, Integer.toString(ConstantConfiguration.defaultBeaconRaidLength));	
				break;
			case 4:
				break;
			default:
				sender.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
				return false;
		}
		LocalDateTime startTime;
		int eventDurationInMinutes;
		int attackDurationInMinutes;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
			startTime = LocalDateTime.parse(args[1], formatter);
		}catch(DateTimeParseException e) {
			sender.sendMessage(StringFormatter.error("Der DateTime Parameter muss dem Format yyyy-MM-dd_HH:mm entsprechen"));
			return false;
		}
		try {
			eventDurationInMinutes = Integer.parseInt(args[2]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Eventdauer Parameter muss dem Typ Long entsprechen"));
			return false;
		}
		if(eventDurationInMinutes < 1) {
			sender.sendMessage(StringFormatter.error("Die Eventdauer muss mindestens 1min sein"));
			return false;
		}
		try {
			attackDurationInMinutes = Integer.parseInt(args[3]);
		}catch(NumberFormatException e) {
			sender.sendMessage(StringFormatter.error("Der Angriffsdauer Parameter muss dem Typ Long entsprechen"));
			return false;
		}
		if(attackDurationInMinutes < 1) {
			sender.sendMessage(StringFormatter.error("Die Angriffsdauer muss mindestens 1min sein"));
			return false;
		}
		BeaconFightJson beaconFightJson = new BeaconFightJson(startTime, eventDurationInMinutes, attackDurationInMinutes);
		if(beaconFightManager.tryAddBeaconFight(beaconFightJson)) {
			sender.sendMessage(ChatColor.GREEN + "Beaconevent erfolgreich hinzugefügt. Startet in " + ChatColor.RESET + ChronoUnit.MINUTES.between(LocalDateTime.now(), startTime) + " Minuten");
			return true;
		}else {
			sender.sendMessage(StringFormatter.error("Beaconfight Event wurde nicht hinzugefügt. Möglicherweise war die Startzeit vor Jetzt, oder ein bestehendes Event überschneidet die Zeit."));
			return false;
		}
	}

	private boolean tryRemoveBeaconFight(CommandSender sender, String[] args) {
		boolean removed = beaconFightManager.tryRemoveActiveBeaconFight();
		if(removed) {
			sender.sendMessage(ChatColor.GREEN + "Beaconevent entfernt");
			return true;
		}else {
			sender.sendMessage(StringFormatter.error("Es konnte kein Beaconfight entfernt werden"));
			return false;
		}
	}

}
