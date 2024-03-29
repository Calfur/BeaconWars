package com.github.calfur.beaconWars.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.disabling.TotemNerf;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandTotemCooldown implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length == 0) {
				sendTotemCooldownMessage(executor);
				return true;
			}else if(args.length == 1){
				String playerWhoseCooldownIsRequestedName = args[0];
				Player playerWhoseCooldownIsRequested = Bukkit.getPlayer(playerWhoseCooldownIsRequestedName);
				if(playerWhoseCooldownIsRequested == null) {				
					executor.sendMessage(StringFormatter.error("Der Spieler " + playerWhoseCooldownIsRequestedName + " ist nicht online"));
					return false;
				}
				sendTotemCooldownMessage(executor, playerWhoseCooldownIsRequested);
				return true;
			}else {
				executor.sendMessage(StringFormatter.error("Zu viele Parameter angegeben"));
				return false;				
			}
		}
		return false;
	}

	private void sendTotemCooldownMessage(Player executor) {
		long cooldownSeconds = TotemNerf.getRemainingCooldownSeconds(executor.getUniqueId());
		if(cooldownSeconds == 0) {
			executor.sendMessage("Du hast keinen aktiven Totem-Cooldown");					
		}else {
			executor.sendMessage("Dein Totem-Cooldown dauert noch " + StringFormatter.displaySecondsAsTime(cooldownSeconds));	
		}
	}
	
	private void sendTotemCooldownMessage(Player messageReceiver, Player playerWhoseCooldownIsRequested) {
		long cooldownSeconds = TotemNerf.getRemainingCooldownSeconds(playerWhoseCooldownIsRequested.getUniqueId());
		if(cooldownSeconds == 0) {
			messageReceiver.sendMessage(playerWhoseCooldownIsRequested.getDisplayName() + " hat keinen aktiven Totem-Cooldown");					
		}else {
			messageReceiver.sendMessage("Der Totem-Cooldown von " + playerWhoseCooldownIsRequested.getDisplayName() + " dauert noch " + StringFormatter.displaySecondsAsTime(cooldownSeconds));	
		}
	}
}
