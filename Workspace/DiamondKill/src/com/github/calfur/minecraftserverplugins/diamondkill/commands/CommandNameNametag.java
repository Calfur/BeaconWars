package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandNameNametag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length == 1) {
				String name = args[0];
				ItemStack itemInHand;
				itemInHand = executor.getInventory().getItemInMainHand();
			}
		}
		return false;
	}

}
