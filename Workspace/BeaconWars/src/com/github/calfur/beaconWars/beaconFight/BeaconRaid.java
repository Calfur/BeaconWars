package com.github.calfur.beaconWars.beaconFight;

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

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.PlayerModeManager;
import com.github.calfur.beaconWars.Reward;
import com.github.calfur.beaconWars.RewardManager;
import com.github.calfur.beaconWars.ScoreboardLoader;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.customTasks.TaskScheduler;
import com.github.calfur.beaconWars.pvp.Team;

public class BeaconRaid {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	private PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
	private RewardManager rewardManager = Main.getInstance().getRewardManager();
	
	private Team attackerTeam;
	private Team defenderTeam;
	private LocalDateTime deadline;
	private String bossBarName;
	private String destructorName;
	private Location beaconLocation;
	private BeaconFight beaconFight;
	private int overtimeTaskId;
	private int maxMinutesToBringBack;
	private Collection<PotionEffect> attackerEffects;
	
	public Team getAttackerTeam() {
		return attackerTeam;
	}
	
	public Team getDefenderTeam() {
		return defenderTeam;
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
	
	public BeaconRaid(Team attackerTeam, Team defenderTeam, Player destructor, Location beaconLocation, int attackDurationInMinutes, BeaconFight beaconFight) {
		this.attackerTeam = attackerTeam;
		this.defenderTeam = defenderTeam;
		this.destructorName = destructor.getName();
		this.beaconLocation = beaconLocation;
		this.beaconFight = beaconFight;
		this.maxMinutesToBringBack = attackDurationInMinutes;
		this.deadline = LocalDateTime.now().plusMinutes(maxMinutesToBringBack);
		this.attackerEffects = Arrays.asList(new PotionEffect[]{
			new PotionEffect(PotionEffectType.GLOWING, maxMinutesToBringBack*60*20, 0),
			new PotionEffect(PotionEffectType.WEAKNESS, maxMinutesToBringBack*60*20, 0)
		});
		
		destructor.addPotionEffects(attackerEffects);
		
		Bukkit.broadcastMessage(destructorName + " von " + attackerTeam.getColor() + "Team " + attackerTeam.getId() + ChatColor.RESET + " hat den Beacon von " + defenderTeam.getColor() + "Team " + defenderTeam.getId() + ChatColor.RESET + " abgebaut");
		Bukkit.broadcastMessage("Der Beacon muss innerhalb von " + maxMinutesToBringBack + "min zur Base gebracht werden");
	
		bossBarName = attackerTeam.getColor() + "Team " + attackerTeam.getId() + ChatColor.RESET + " klaut den Beacon von " + defenderTeam.getColor() + "Team " + defenderTeam.getId();
		
		scoreboardLoader.addBossBar(bossBarName, attackerTeam.getColor(), deadline);
	
		overtimeTaskId = TaskScheduler.getInstance().scheduleDelayedTask(Main.getInstance(), 
				new Runnable() {
			
					@Override
					public void run() {
						doTimeOverActions();				
					}
			
				}, 
				LocalDateTime.now().plusMinutes(maxMinutesToBringBack));
	}
	
	public void addBeaconPlacement(Player player) {
		Bukkit.broadcastMessage(player.getName() + " von " + attackerTeam.getColor() + "Team " + attackerTeam.getId() + ChatColor.RESET + " hat den Beacon von " + defenderTeam.getColor() + "Team " + defenderTeam.getId() + ChatColor.RESET + " erfolgreich zurückgebracht");
		Bukkit.broadcastMessage(player.getName() + " erhält dafür " + ChatColor.AQUA + configuration.getRewardBeaconRaidSuccessDiamonds() + " Dias");
		
		rewardManager.addReward(
			player.getName(), 
			new Reward(
				configuration.getRewardBeaconRaidSuccessDiamonds(), 
				configuration.getRewardBeaconRaidSuccessPoints()
			), 
			"Belohnung für das erfolgreiche stehlen und zurückbringen des Beacons von Team " + defenderTeam.getId()
		);
		
		BeaconManager.removeOneBeaconFromInventory(player);
		scoreboardLoader.reloadScoreboardFor(player);
		
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
			playerModeManager.reloadPlayerMode(player);
		}
	}
	
	private void destroy() {
		TaskScheduler.getInstance().cancelTask(overtimeTaskId);
		scoreboardLoader.removeBossBar(bossBarName);
		placeBeaconBack();
		beaconFight.removeBeaconRaid(this);
	}
	
	private void placeBeaconBack() {
		beaconLocation.getBlock().setType(Material.BEACON);
	}

	public void doTimeOverActions() {
		Bukkit.broadcastMessage("Die Zeit von " + attackerTeam.getColor() + "Team " + attackerTeam.getId() + ChatColor.RESET + " ist abgelaufen. Beaconraub gegen " + defenderTeam.getColor() + "Team " + defenderTeam.getId() + ChatColor.RESET + "  fehlgeschlagen.");
		Player player = Bukkit.getPlayerExact(destructorName);
		if(player != null) {
			BeaconManager.removeOneBeaconFromInventory(player);
		}
		destroy();
	}
	
	public void doAttackPreventedActions() {
		Bukkit.broadcastMessage("Der Angriff von " + attackerTeam.getColor() + "Team " + attackerTeam.getId() + ChatColor.RESET + " gegen " + defenderTeam.getColor() + "Team " + defenderTeam.getId() + ChatColor.RESET + " wurde verhindert. Beaconraub fehlgeschlagen.");
		destroy();
	}
}
