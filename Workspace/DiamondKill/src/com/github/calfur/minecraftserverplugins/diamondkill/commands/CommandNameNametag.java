package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.InventoryManagement;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandNameNametag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length >= 1) {
				String name = String.join(" ", args);
				int maxLength = (name.length() < 32)?name.length():32;
				name = name.substring(0, maxLength);
				ItemStack itemInHand;
				itemInHand = executor.getInventory().getItemInMainHand();
				if(itemInHand.getType() != Material.NAME_TAG) {
					executor.sendMessage(StringFormatter.error("Du musst ein Nametag in deiner Main-Hand halten"));
					return false;
				}
				InventoryManagement.renameItem(itemInHand, name);
				executor.sendMessage(ChatColor.GREEN + "Nametag umbenannt");
				return true;
			}
		}
		return false;
	}

}
