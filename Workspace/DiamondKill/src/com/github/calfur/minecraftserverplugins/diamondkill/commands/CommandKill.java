package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.TopKiller;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandKill implements CommandExecutor {

	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length >= 1) {
				String subCommand = args[0].toLowerCase();
				switch(subCommand) {
					case "info":
						return sendKillInfo(executor, args);
					case "list":
						return sendKillList(executor, args);
					case "delete":
					case "remove":
						if(executor.hasPermission("admin")) {	
							return deleteKill(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
							return true;
						}
					case "add":
						if(executor.hasPermission("admin")) {	
							return addKill(executor, args);
						}else {
							executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
							return true;
						}
					default:
						executor.sendMessage(StringFormatter.error(subCommand + " ist kein vorhandener Subcommand"));
						return false;
				}
			}			
		}
		return false;
	}

	private boolean sendKillList(Player executor, String[] args) {
		if(args.length != 1) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		Map<String, KillJson> kills = killDbConnection.getKills();
		executor.sendMessage(ChatColor.BOLD + "" + kills.size() + " Kills gefunden:");
		for (Entry<String, KillJson> kill : kills.entrySet()) {
			executor.sendMessage(ChatColor.RESET + "Id: " + kill.getKey() + " Killer: " + StringFormatter.firstLetterToUpper(kill.getValue().getKiller()) + " Opfer: " + StringFormatter.firstLetterToUpper(kill.getValue().getVictim()));
		}
		return true;
	}


	private boolean sendKillInfo(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			executor.sendMessage(StringFormatter.error("Dieser Kill ist nicht registriert"));
			return false;
		}
		KillJson killJson = killDbConnection.getKill(killId);
		executor.sendMessage(StringFormatter.bold("Id: " + killId)); 
		executor.sendMessage(ChatColor.RESET + "Killer: " + StringFormatter.firstLetterToUpper(killJson.getKiller())); 
		executor.sendMessage(ChatColor.RESET + "Opfer: " + StringFormatter.firstLetterToUpper(killJson.getVictim()));
		executor.sendMessage(ChatColor.RESET + "Zeit: " + killJson.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm")));
		return true;
	}
	
	private boolean deleteKill(Player executor, String[] args) {
		if(args.length != 2) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		int killId;
		try {
			killId = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Kill Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!killDbConnection.existsKill(killId)) {
			executor.sendMessage(StringFormatter.error("Dieser Kill ist nicht registriert"));
			return false;
		}
		killDbConnection.removeKill(killId);
		Main.getInstance().getScoreboardLoader().setTopKiller(TopKiller.getCurrentTopKiller());
		executor.sendMessage(ChatColor.GREEN + "Kill " + killId + " gelöscht.");
		return true;
	}
	
	private boolean addKill(Player executor, String[] args) {
		if(args.length != 3) {
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		String killer;
		String victim;
		LocalDateTime dateTime = LocalDateTime.now();
		try {
			killer = args[1];
			victim = args[2];
		}catch(NumberFormatException e) {
			executor.sendMessage(StringFormatter.error("Der Kill Id Parameter muss dem Typ Int entsprechen"));
			return false;
		}
		if(!playerDbConnection.existsPlayer(killer)) {
			executor.sendMessage(StringFormatter.error("Der Killer " + killer + " ist nicht registriert"));
			return false;
		}
		if(!playerDbConnection.existsPlayer(victim)) {
			executor.sendMessage(StringFormatter.error("Das Opfer " + victim + " ist nicht registriert"));
			return false;
		}
		int killId = killDbConnection.getNextId();
		

		PlayerJson killerJson = playerDbConnection.getPlayer(killer);
		
		int bounty = killDbConnection.getBounty(victim);
		killerJson.addCollectableDiamonds(bounty);
		playerDbConnection.addPlayer(killer, killerJson);

		killDbConnection.addKill(killId, new KillJson(killer, victim, dateTime));
		
		Main.getInstance().getScoreboardLoader().setTopKiller(TopKiller.getCurrentTopKiller());

		executor.sendMessage(ChatColor.GREEN + "Kill " + killId + " registriert. " + ChatColor.RESET + bounty + " Diamanten Kompfgeld an " + killer + " ausgezahlt;");
		return true;
	}
}
