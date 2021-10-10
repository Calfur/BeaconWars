package com.github.calfur.beaconWars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.CompassManager;
import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.CompassManager.CompassType;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandCompass implements CommandExecutor {

	CompassManager compassManager = Main.getInstance().getCompassManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;
			if(args.length == 1 && args[0].toLowerCase().equals("spawn")) {
				compassManager.addCompassToInventory(executor, CompassType.spawn);
				return true;
			}
			if(args.length == 2) {
				int teamNumber;
				try {
					teamNumber = Integer.parseInt(args[1]);
				}catch(NumberFormatException e) {
					executor.sendMessage(StringFormatter.error("Der Teamnummer Parameter muss dem Typ Int entsprechen"));
					return false;
				}				
				switch (args[0].toLowerCase()) {
				case "base":
					compassManager.addCompassToInventory(executor, CompassType.base, teamNumber);
					return true;
				case "beacon":
					compassManager.addCompassToInventory(executor, CompassType.beacon, teamNumber);
					return true;
				case "spawn":
					executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
					return false;
				default:
					executor.sendMessage(StringFormatter.error(args[0] + " ist kein gültiger subcommand"));
					return false;
				}
			}
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
			return false;
		}
		return false;
	}

}
