package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.BeaconManager;
import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.Team;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;

public class BeaconRaid {
	private Team attacker;
	private Team defender;
	private LocalDateTime deadline;
	private String bossBarName;
	private String destructorName;
	private Location beaconLocation;
	private BeaconFight beaconFight;
	private int taskId;
	private int reward = 5;
	private int maxMinutesToBringBack = 1;
	
	public Team getAttacker() {
		return attacker;
	}
	public Team getDefender() {
		return defender;
	}
	public LocalDateTime getDeadline() {
		return deadline;
	}
	public String getBossBarName() {
		return bossBarName;
	}
	public String getDestructorName() {
		return destructorName;
	}
	
	public BeaconRaid(Team attacker, Team defender, Player destructor, Location beaconLocation, BeaconFight beaconFight) {
		this.attacker = attacker;
		this.defender = defender;
		this.destructorName = destructor.getName();
		this.beaconLocation = beaconLocation;
		this.beaconFight = beaconFight;
		this.deadline = LocalDateTime.now().plusMinutes(maxMinutesToBringBack);
		
		Bukkit.broadcastMessage(destructorName + " von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " hat den Beacon von " + defender.getColor() + "Team " + defender.getId() + ChatColor.RESET + " abgebaut");
		Bukkit.broadcastMessage("Der Beacon muss innerhalb von " + maxMinutesToBringBack + "min");
		Bukkit.broadcastMessage("zur�ck zur Basis von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " gebracht werden");
	
		bossBarName = attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " klaut den Beacon von " + defender.getColor() + "Team " + defender.getId();
		
		Main.getInstance().getScoreboardLoader().addBossBar(bossBarName, attacker.getColor(), deadline);
	
		taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				timeOverActions();				
			}
			
		}, maxMinutesToBringBack*60*20);
	}
	
	public void addBeaconPlacement(PlayerJson placer, Player player) {
		Bukkit.broadcastMessage(player.getName() + " von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " hat den Beacon von " + defender.getColor() + "Team " + defender.getId() + ChatColor.RESET + " erfolgreich zur�ckgebracht");
		Bukkit.broadcastMessage(player.getName() + " erh�lt daf�r " + ChatColor.AQUA + reward + " Dias");
		
		placer.addCollectableDiamonds(reward);
		Main.getInstance().getPlayerDbConnection().addPlayer(player.getName(), placer);
		BeaconManager.removeOneBeaconFromInventory(player);
		Main.getInstance().getScoreboardLoader().reloadScoreboardFor(player);
		
		destroy();
	}
	
	private void destroy() {
		Bukkit.getScheduler().cancelTask(taskId);
		Main.getInstance().getScoreboardLoader().removeBossBar(bossBarName);
		placeBeaconBack();
		beaconFight.removeBeaconRaid(this);
	}
	
	private void placeBeaconBack() {
		beaconLocation.getBlock().setType(Material.BEACON);
	}
	
	private void timeOverActions() {
		Bukkit.broadcastMessage("Die Zeit von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " ist abgelaufen. Angriff fehlgeschlagen.");
		Player player = Bukkit.getPlayerExact(destructorName);
		if(player != null) {
			BeaconManager.removeOneBeaconFromInventory(player);
		}
		destroy();
	}
}
