package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

import net.md_5.bungee.api.ChatColor;

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
					executor.sendMessage(StringFormatter.Error("Du musst ein Nametag in deiner Main-Hand halten"));
					return false;
				}
				renameNameTag(itemInHand, name);
				executor.sendMessage(ChatColor.GREEN + "Nametag umbenannt");
				return true;
			}
		}
		return false;
	}

	private void renameNameTag(ItemStack itemInHand, String name) {
		ItemMeta meta = itemInHand.getItemMeta();
		meta.setDisplayName(name);
		itemInHand.setItemMeta(meta);
	}

}
