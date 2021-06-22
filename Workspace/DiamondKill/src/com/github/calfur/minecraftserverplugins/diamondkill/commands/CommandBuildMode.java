package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.PlayerModeManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;

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
				executor.sendMessage(ChatColor.RED + "Keine Parameter verfügbar");
				return false;
			}			
		}
		return false;
	}

}
