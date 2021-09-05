package com.github.calfur.beaconWars.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;

import com.github.calfur.beaconWars.BeaconManager;
import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.PlayerKicker;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class CommandStartProjectForPlayers  implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) { // Spieler führt den Command aus
			Player executor = (Player)sender;						
			if(executor.hasPermission("admin")) {			
				if(args.length == 1) {
					Player player = Bukkit.getPlayerExact(args[0]);
					if(player != null) {
						startProjectForPlayer(player);
						sender.sendMessage(ChatColor.GREEN + "Spieler " + player.getName() + " teleportiert");
						return true;
					}else {
						executor.sendMessage(StringFormatter.error("Spieler " + args[0] + " konnte nicht gefunden werden (muss online sein)"));
						return false;
					}
				}else {
					executor.sendMessage(StringFormatter.error("Der Command enthält nicht die richtige Anzahl Parameter"));
					return false;
				}
			}else {
				executor.sendMessage(StringFormatter.error("Fehlende Berechtigung für diesen Command"));
				return false;
			}
		}else if(sender instanceof BlockCommandSender) { // Commandblock führt den Command aus
			BlockCommandSender commandBlock = (BlockCommandSender)sender;
			Location location = commandBlock.getBlock().getLocation();
			return startProjectForAllNearbyPlayers(location);
		}
		return false;
	}

	public static void startProjectForAllOnlinePlayers() {
		startProjectForPlayers(Bukkit.getOnlinePlayers());
	}

	private static boolean startProjectForAllNearbyPlayers(Location location) {
		List<Player> nearbyPlayers = getNearbyPlayers(location);
		if(nearbyPlayers.size() == 0) {			
			return false;
		}
		startProjectForPlayers(nearbyPlayers);
		return true;
	}

	private static List<Player> getNearbyPlayers(Location location) {
		List<Player> nearbyPlayers = new ArrayList<Player>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			Location playerLocation = player.getLocation();
			double distance = location.distance(playerLocation);
			if(distance < 5) {
				nearbyPlayers.add(player);
			}
		}
		return nearbyPlayers;
	}

	private static void startProjectForPlayers(Collection<? extends Player> collection) {
		for (Player player : collection) {
			startProjectForPlayer(player);
		}
	}
	
	private static void startProjectForPlayer(Player player) {
		if(!Main.getInstance().getPlayerDbConnection().existsPlayer(player.getName())) {
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(player));	
			return;
		}
		if(!Main.getInstance().getPlayerDbConnection().isPlayerSpectator(player.getName())) {
			BeaconManager.teleportPlayerToBeacon(player);
			BeaconManager.setBeaconAsRespawnLocation(player);			
			player.setGameMode(GameMode.SURVIVAL);
		}
	}
}
