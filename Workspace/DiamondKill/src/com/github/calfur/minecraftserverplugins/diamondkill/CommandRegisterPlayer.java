package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRegisterPlayer implements CommandExecutor {
	
	private PlayerConfig playerConfig = Main.getInstance().getPlayerConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(player.hasPermission("admin")) {				
				if(args.length == 2) {
					String name;
					int team;
					try {
						name = args[0];
						team = Integer.parseInt(args[1]);
					}catch(NumberFormatException e) {
						player.sendMessage("Der Team Parameter muss dem Typ Int entsprechen");
						return false;
					}
					playerConfig.addPlayer(name, new JsonPlayer(team));
					playerConfig.saveConfig().loadConfig();
					return true;
				}
			}
		}
		return false;
	}
}
