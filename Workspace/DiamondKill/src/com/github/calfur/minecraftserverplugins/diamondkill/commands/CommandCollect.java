package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.ScoreboardLoader;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;

public class CommandCollect implements CommandExecutor {

	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;						
			if(args.length == 2) {
				String type = args[0].toLowerCase();
				int amount;
				try {
					amount = Integer.parseInt(args[1]);
				}catch(NumberFormatException e) {
					executor.sendMessage(ChatColor.RED + "Der Team Parameter muss dem Typ Int entsprechen");
					return false;
				}
				switch(type) {
					case "diamond":
					case "diamonds":
					case "diamant":
					case "dia":
					case "dias":
						Material material = Material.DIAMOND;
						return addItemsToInventory(executor, material, amount);
					default:
						executor.sendMessage(ChatColor.RED + type + " ist kein verfügbares Item");
						return false;
				}
			}else {
				executor.sendMessage(ChatColor.RED + "Der Command enthält nicht die richtige anzahl Parameter");
				return false;
			}
		}
		return false;
	}

	private boolean addItemsToInventory(Player executor, Material material, int amount) {
		PlayerInventory inventory = executor.getInventory();
		if(inventory.firstEmpty() == -1) {	//firstEmpty returns -1 if inventory is full
			executor.sendMessage(ChatColor.RED + "Keinen freien Inventar slot gefunden");
			return false;
		}
		PlayerJson playerJson = playerDbConnection.getPlayer(executor.getName());
		int availableDiamonds = playerJson.getCollectableDiamonds();
		if(availableDiamonds < amount) {
			executor.sendMessage(ChatColor.RED + "Du kannst nur maximal " + availableDiamonds + " Diamanten einsammeln. Kille Leute um mehr zu erhalten.");
			return false;
		}
		playerJson.removeCollectableDiamonds(amount);
		playerDbConnection.addPlayer(executor.getName(), playerJson);
		scoreboardLoader.reloadScoreboardFor(executor);
		ItemStack item = new ItemStack(material, amount);
		inventory.addItem(item);
		return true;
	}

}
