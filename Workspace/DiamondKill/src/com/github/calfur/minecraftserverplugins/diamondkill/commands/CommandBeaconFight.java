package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
		}
		return false;
	}
	
	private boolean tryAddBeaconFight(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		LocalDateTime startTime;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
			startTime = LocalDateTime.parse(args[1], formatter);
		}catch(DateTimeParseException e) {
			executor.sendMessage(ChatColor.RED + "Der DateTime Parameter muss dem Format yyyy-MM-dd_HH:mm entsprechen");
			return false;
		}
		
		return beaconFightManager.tryAddBeaconFight(startTime);
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
