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
import com.github.calfur.minecraftserverplugins.diamondkill.PlayerKicker;
import com.github.calfur.minecraftserverplugins.diamondkill.ScoreboardLoader;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TransactionJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.InventoryManagement;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;

public class CommandCollect implements CommandExecutor {

	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection();
	private TransactionDbConnection transactionDbConnection = Main.getInstance().getTransactionDbConnection();
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
					executor.sendMessage(StringFormatter.error("Der Parameter Anzahl muss dem Typ Int entsprechen"));
					return false;
				}
				switch(type) {
					case "diamond":
					case "diamonds":
					case "diamant":
					case "dia":
					case "dias":
						return tryToWithdrawDiamonds(executor, amount);
					default:
						executor.sendMessage(StringFormatter.error(type + " ist kein verf�gbares Item"));
						return false;
				}
			}else {
				executor.sendMessage(StringFormatter.error("Der Command enth�lt nicht die richtige anzahl Parameter"));
				return false;
			}
		}
		return false;
	}

	private boolean tryToWithdrawDiamonds(Player executor, int amount) {
		PlayerInventory inventory = executor.getInventory();
		if(InventoryManagement.isInventoryFull(inventory)) {
			executor.sendMessage(StringFormatter.error("Keinen freien Inventar slot gefunden"));
			return false;
		}
		
		if(!playerDbConnection.existsPlayer(executor.getName())) {
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(executor));	
			return false;
		}
		
		PlayerJson playerJson = playerDbConnection.getPlayer(executor.getName());
		int availableDiamonds = playerJson.getCollectableDiamonds();
		if(availableDiamonds < amount) {
			executor.sendMessage(StringFormatter.error("Du kannst momentan nicht mehr als ") + StringFormatter.uncoloredDiamondWord(availableDiamonds) + StringFormatter.error(" einsammeln. Kille Leute um mehr zu erhalten."));
			return false;
		}
		
		addDiamondsToInventory(executor, playerJson, amount);
		return true;
	}

	private void addDiamondsToInventory(Player player, PlayerJson playerJson, int amount) {
		playerJson.removeCollectableDiamonds(amount);
		transactionDbConnection.addTransaction(new TransactionJson(player.getName(), playerJson.getTeamId(), -amount, 0, StringFormatter.uncoloredDiamondWord(amount) + " mit /collect ausgezahlt"));
		playerDbConnection.addPlayer(player.getName(), playerJson);
		scoreboardLoader.reloadScoreboardFor(player);
		
		ItemStack itemStack = new ItemStack(Material.DIAMOND, amount);
		player.getInventory().addItem(itemStack);
		player.sendMessage(StringFormatter.diamondWord(amount) + ChatColor.GREEN + StringFormatter.singularOrPlural(amount, " wurde", " wurden") + " zu deinem Inventar hinzugef�gt");
	}

}
