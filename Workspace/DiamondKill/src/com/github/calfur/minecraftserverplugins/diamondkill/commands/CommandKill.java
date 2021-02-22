package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandKill implements CommandExecutor {

	private KillDbConnection killDbConnection = Main.getInstance().killDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().playerDbConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;						
			if(args.length >= 1) {
				String subCommand = args[0].toLowerCase();
				switch(subCommand) {
					case "info":
						return sendKillInfo(player, args);
					case "list":
						return sendKillList(player, args);
					case "delete":
						if(player.hasPermission("admin")) {	
							return deleteKill(player, args);
						}else {
							player.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					case "add":
						if(player.hasPermission("admin")) {	
							return addKill(player, args);
						}else {
							player.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					case "edit":
						if(player.hasPermission("admin")) {	
							return editKill(player, args);
						}else {
							player.sendMessage(ChatColor.RED + "Fehlende Berechtigung für diesen Command");
							return true;
						}
					default:
						player.sendMessage(ChatColor.RED + subCommand + " ist kein vorhandener Command");
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendKillList(Player executor, String[] args) {
		if(args.length != 1) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		Map<String, KillJson> kills = killDbConnection.getKills();
		executor.sendMessage(ChatColor.AQUA + "" + kills.size() + " Kills gefunden:");
		for (Entry<String, KillJson> kill : kills.entrySet()) {
			executor.sendMessage(ChatColor.AQUA + "Id: " + kill.getKey() + " Killer: " + kill.getValue().getKiller() + " Opfer: " + kill.getValue().getVictim());
		}
		return true;
	}


	private boolean sendKillInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Id Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			executor.sendMessage(ChatColor.RED + "Dieser Kill ist nicht registriert");
			return false;
		}
		KillJson killJson = killDbConnection.getKill(killId);
		executor.sendMessage(ChatColor.AQUA + "Id: " + killId); 
		executor.sendMessage(ChatColor.AQUA + "Killer: " + killJson.getKiller()); 
		executor.sendMessage(ChatColor.AQUA + "Opfer: " + killJson.getVictim());
		executor.sendMessage(ChatColor.AQUA + "Zeitpunkt: " + killJson.getDateTime());
		return true;
	}
	
	private boolean deleteKill(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Kill Id Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			executor.sendMessage(ChatColor.RED + "Dieser Kill ist nicht registriert");
			return false;
		}
		killDbConnection.removeKill(killId);
		executor.sendMessage(ChatColor.GREEN + "Kill " + killId + " gelöscht.");
		return true;
	}
	
	private boolean editKill(Player executor, String[] args) {
		if(args.length != 4) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		int killId;
		String killer;
		String victim;
		LocalDateTime dateTime = LocalDateTime.now();
		try {
			killId = Integer.parseInt(args[1]);
			killer = args[2];
			victim = args[3];
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Kill Id Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!playerDbConnection.existsPlayer(killer)) {
			executor.sendMessage(ChatColor.RED + "Der Killer " + killer + " ist nicht registriert");
			return false;
		}
		if(!playerDbConnection.existsPlayer(victim)) {
			executor.sendMessage(ChatColor.RED + "Das Opfer " + victim + " ist nicht registriert");
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			executor.sendMessage(ChatColor.RED + "Dieser Kill ist nicht registriert");
			return false;
		}
		killDbConnection.addKill(killId, new KillJson(killer, victim, dateTime));
		executor.sendMessage(ChatColor.GREEN + "Kill " + killId + " editiert.");
		return true;
	}
	
	private boolean addKill(Player executor, String[] args) {
		if(args.length != 3) {
			executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
			return false;
		}
		String killer;
		String victim;
		LocalDateTime dateTime = LocalDateTime.now();
		try {
			killer = args[1];
			victim = args[2];
		}catch(NumberFormatException e) {
			executor.sendMessage(ChatColor.RED + "Der Kill Id Parameter muss dem Typ Int entsprechen");
			return false;
		}
		if(!playerDbConnection.existsPlayer(killer)) {
			executor.sendMessage(ChatColor.RED + "Der Killer " + killer + " ist nicht registriert");
			return false;
		}
		if(!playerDbConnection.existsPlayer(victim)) {
			executor.sendMessage(ChatColor.RED + "Das Opfer " + victim + " ist nicht registriert");
			return false;
		}
		int killId = killDbConnection.getNextId();
		
		killDbConnection.addKill(killId, new KillJson(killer, victim, dateTime));
		executor.sendMessage(ChatColor.GREEN + "Kill " + killId + " registriert.");
		return true;
	}
}
