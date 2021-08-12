package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.CompassManager;
import com.github.calfur.minecraftserverplugins.diamondkill.CompassManager.CompassType;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandCompass implements CommandExecutor {

	CompassManager compassManager = Main.getInstance().getCompassManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;
			if(args.length == 1 && args[0].equals("Spawn")) {
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
				switch (args[0]) {
				case "Base":
					compassManager.addCompassToInventory(executor, CompassType.base, teamNumber);
					return true;
				case "Beacon":
					compassManager.addCompassToInventory(executor, CompassType.beacon, teamNumber);
					return true;
				case "Spawn":
					executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige anzahl Parameter"));
					return false;
				default:
					executor.sendMessage(StringFormatter.error(args[0] + " ist kein g�ltiger subcommand"));
					return false;
				}
			}
			executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige anzahl Parameter"));
			return false;
		}
		return false;
	}

}