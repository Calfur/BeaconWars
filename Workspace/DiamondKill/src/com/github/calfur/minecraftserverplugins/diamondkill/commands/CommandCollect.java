package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandCollect implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack diamonds = new ItemStack(Material.DIAMOND);
			diamonds.setAmount(20);
			player.getInventory().addItem(diamonds);
			Bukkit.broadcastMessage("Command Parameters: Label: " + label + " args[0]: " + args[0] + " args[1]: " + args[1]);
			return true;
		}		
		return false;
	}

}
