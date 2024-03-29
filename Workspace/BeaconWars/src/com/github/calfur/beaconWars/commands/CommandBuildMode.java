package com.github.calfur.beaconWars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.PlayerModeManager;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandBuildMode implements CommandExecutor {
	private PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length == 0) {
				playerModeManager.toggleBuildMode(executor);
				return true;
			}else {
				executor.sendMessage(StringFormatter.error("Keine Parameter verf�gbar"));
				return false;
			}			
		}
		return false;
	}

}
