package com.github.calfur.minecraftserverplugins.diamondkill.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamJson;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.InventoryManagement;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringFormatter;
import com.github.calfur.minecraftserverplugins.diamondkill.hungerGamesLootDrop.HungerGamesManager;

public class CommandCompass implements CommandExecutor {

	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player executor = (Player)sender;
			if(args.length == 1 && args[0].equals("Spawn")) {
				addCompassToInventory(executor, CompassType.spawn);
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
					addCompassToInventory(executor, CompassType.base, teamNumber);
					return true;
				case "Beacon":
					addCompassToInventory(executor, CompassType.beacon, teamNumber);
					return true;
				case "Spawn":
					executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
					return false;
				default:
					executor.sendMessage(StringFormatter.error(args[0] + " ist kein gültiger subcommand"));
					return false;
				}
			}
			executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige anzahl Parameter"));
			return false;
		}
		return false;
	}
	
	private void addCompassToInventory(Player player, CompassType compassType) {
		switch (compassType) {
		case spawn:			
			addCompassToInventory(player, HungerGamesManager.spawnLocation, ChatColor.RESET + "Spawn");
			break;
		default:
			player.sendMessage(StringFormatter.error("Unerwarteter Fehler beim ausführen des Commands"));
			break;
		}	
		return;
	}

	private void addCompassToInventory(Player player, CompassType compassType, int teamNumber) {
		switch (compassType) {
		case base:
			if(!teamDbConnection.existsTeam(teamNumber) || TeamDbConnection.spectatorTeamNumber == teamNumber) {				
				player.sendMessage(StringFormatter.error("Ein Team mit der Nummer ") + teamNumber + StringFormatter.error(" existiert nicht"));
				return;
			}
			TeamJson teamJson = teamDbConnection.getTeam(teamNumber);
			Location targetLocation = teamJson.getBeaconPosition();
			addCompassToInventory(player, targetLocation, ChatColor.RESET + "Base " + teamJson.getColor() + "Team " + teamNumber);
			break;
		case beacon:
			// TODO spieler verfolgender Kompass
			break;
		default:
			player.sendMessage(StringFormatter.error("Unerwarteter Fehler beim ausführen des Commands"));
			break;
		}
	}
	
	private void addCompassToInventory(Player player, Location targetLocation, String itemName) {
		PlayerInventory inventory = player.getInventory();
		if(InventoryManagement.isInventoryFull(inventory)) {
			player.sendMessage(StringFormatter.error("Keinen freien Inventar slot gefunden"));
			return;
		}
		
		ItemStack itemStack = getLodstoneCompass(targetLocation);
		InventoryManagement.renameItem(itemStack, itemName);
		inventory.addItem(itemStack);
		player.sendMessage(ChatColor.GREEN + "Kompass zu deinem Inventar hinzugefügt");
	}
	
	private ItemStack getLodstoneCompass(Location targetLocation) {
		ItemStack itemStack = new ItemStack(Material.COMPASS, 1);
		CompassMeta meta = (CompassMeta) itemStack.getItemMeta();
		meta.setLodestoneTracked(false);
		meta.setLodestone(targetLocation);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	private enum CompassType{
		spawn,
		base,
		beacon
	}
}
