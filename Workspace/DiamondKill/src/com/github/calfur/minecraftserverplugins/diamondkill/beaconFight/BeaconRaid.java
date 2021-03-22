package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
	private int maxMinutesToBringBack = 15;
	private Collection<PotionEffect> attackerEffects = Arrays.asList(new PotionEffect[]{
		new PotionEffect(PotionEffectType.GLOWING, maxMinutesToBringBack*60*20, 0),
		new PotionEffect(PotionEffectType.WEAKNESS, maxMinutesToBringBack*60*20, 0)
	});
	
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
	
	public BeaconRaid(Team attacker, Team defender, Player destructor, Location beaconLocation, int attackDurationInMinutes, BeaconFight beaconFight) {
		this.attacker = attacker;
		this.defender = defender;
		this.destructorName = destructor.getName();
		this.beaconLocation = beaconLocation;
		this.beaconFight = beaconFight;
		this.maxMinutesToBringBack = attackDurationInMinutes;
		this.deadline = LocalDateTime.now().plusMinutes(maxMinutesToBringBack);
				
		destructor.addPotionEffects(attackerEffects);
		
		Bukkit.broadcastMessage(destructorName + " von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " hat den Beacon von " + defender.getColor() + "Team " + defender.getId() + ChatColor.RESET + " abgebaut");
		Bukkit.broadcastMessage("Der Beacon muss innerhalb von " + maxMinutesToBringBack + "min zur Base gebracht werden");
	
		bossBarName = attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " klaut den Beacon von " + defender.getColor() + "Team " + defender.getId();
		
		Main.getInstance().getScoreboardLoader().addBossBar(bossBarName, attacker.getColor(), deadline);
	
		taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				doTimeOverActions();				
			}
			
		}, maxMinutesToBringBack*60*20);
	}
	
	public void addBeaconPlacement(PlayerJson placer, Player player) {
		Bukkit.broadcastMessage(player.getName() + " von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " hat den Beacon von " + defender.getColor() + "Team " + defender.getId() + ChatColor.RESET + " erfolgreich zurückgebracht");
		Bukkit.broadcastMessage(player.getName() + " erhält dafür " + ChatColor.AQUA + reward + " Dias");
		
		placer.addCollectableDiamonds(reward);
		Main.getInstance().getPlayerDbConnection().addPlayer(player.getName(), placer);
		BeaconManager.removeOneBeaconFromInventory(player);
		Main.getInstance().getScoreboardLoader().reloadScoreboardFor(player);
		
		removePotionEffects(player);
		
		playBeaconPlacementSound();
		
		destroy();
	}
	
	private void playBeaconPlacementSound() {
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1, 1);
		}
	}
	
	private void removePotionEffects(Player player) {
		for (PotionEffect potionEffect : attackerEffects) {			
			player.removePotionEffect(potionEffect.getType());
			Main.getInstance().getPlayerModeManager().reloadPlayerMode(player);
		}
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

	public void doTimeOverActions() {
		Bukkit.broadcastMessage("Die Zeit von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " ist abgelaufen. Beaconraub gegen " + defender.getColor() + "Team " + defender.getId() + ChatColor.RESET + "  fehlgeschlagen.");
		Player player = Bukkit.getPlayerExact(destructorName);
		if(player != null) {
			BeaconManager.removeOneBeaconFromInventory(player);
		}
		destroy();
	}
	
	public void doAttackPreventedActions() {
		Bukkit.broadcastMessage("Der Angriff von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " gegen " + defender.getColor() + "Team " + defender.getId() + ChatColor.RESET + " wurde verhindert. Beaconraub fehlgeschlagen.");
		destroy();
	}
}
