package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerJson;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.helperClasses.StringEditor;

public class KillEvent implements Listener {
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection(); 
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection(); 
	private KillDbConnection killDbConnection = Main.getInstance().getKillDbConnection(); 
	
	private ArrayList<LatestHitByPlayer> latestHitByPlayers = new ArrayList<LatestHitByPlayer>();
	
	private void AddPlayerHit(Player defender, Player attacker) {
		String defenderName = defender.getName().toLowerCase();
		String attackerName = attacker.getName().toLowerCase();
		if(!playerDbConnection.existsPlayer(defenderName)) {
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(defender));			
			return;
		}
		if(!playerDbConnection.existsPlayer(attackerName)) {
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(attacker));	
			return;
		}
		int defenderTeam = playerDbConnection.getPlayer(defenderName).getTeamId();
		int attackerTeam = playerDbConnection.getPlayer(attackerName).getTeamId();
		if(defenderTeam != attackerTeam) {			
			latestHitByPlayers.remove(GetLatestHitByPlayer(defenderName));
			latestHitByPlayers.add(new LatestHitByPlayer(defenderName, attackerName));
		}
	}
	
	private LatestHitByPlayer GetLatestHitByPlayer(String player) {
		player = player.toLowerCase();
		for (LatestHitByPlayer latestHitByPlayer : latestHitByPlayers) {
			if(latestHitByPlayer.getDefender().equals(player)) {
				return latestHitByPlayer;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onPlayerGetsDamageFromPlayer(EntityDamageByEntityEvent event) {
		Entity defendingEntity = event.getEntity();
		if(defendingEntity.getType() == EntityType.PLAYER) {	
			Player defender = (Player)defendingEntity;
			Entity attacker = event.getDamager();
			switch(attacker.getType()) {
				case PLAYER:
					defender.sendMessage("Hit by Player");
					AddPlayerHit(defender, (Player)attacker);		
					break;
				case ARROW:
					Arrow arrow = (Arrow)attacker;
					ProjectileSource arrowShooter = arrow.getShooter();
					if(arrowShooter instanceof Player) {
						Player shooter = (Player)arrowShooter;
						AddPlayerHit(defender, shooter);					
					}
					break;
				case SPECTRAL_ARROW:
					SpectralArrow spectralArrow = (SpectralArrow)attacker;
					ProjectileSource spectralArrowShooter = spectralArrow.getShooter();
					if(spectralArrowShooter instanceof Player) {
						Player shooter = (Player)spectralArrowShooter;
						AddPlayerHit(defender, shooter);								
					}
					break;
				case TRIDENT:
					Trident trident = (Trident)attacker;
					ProjectileSource tridentShooter = trident.getShooter();
					if(tridentShooter instanceof Player) {
						Player shooter = (Player)tridentShooter;		
						AddPlayerHit(defender, shooter);						
					}
					break;
				default:
					break;
			}
		}
	}
	@EventHandler
	public void onPlayerDies(PlayerDeathEvent event) {
		Player player = (Player)event.getEntity();
		LatestHitByPlayer latestHitByPlayer = GetLatestHitByPlayer(player.getName());
		if(latestHitByPlayer != null) {
			long secondsBetween = ChronoUnit.SECONDS.between(latestHitByPlayer.getDateTime(), LocalDateTime.now());
			if(secondsBetween < 60) {
				String killer = latestHitByPlayer.getAttacker();
				String victim = latestHitByPlayer.getDefender();
				PlayerJson killerJson = playerDbConnection.getPlayer(killer);
				
				int bounty = killDbConnection.getBounty(victim);
				killerJson.addCollectableDiamonds(bounty);
				playerDbConnection.addPlayer(killer, killerJson);
				
				killDbConnection.addKill(killDbConnection.getNextId(), new KillJson(killer, victim, LocalDateTime.now()));
				
				sendDeathMessage(killer, victim, bounty);
			}
		}
	}
	private void sendDeathMessage(String killer, String victim, int bounty) {
		ChatColor teamColorKiller = teamDbConnection.getTeam(playerDbConnection.getPlayer(killer).getTeamId()).getColor();
		ChatColor teamColorVictim = teamDbConnection.getTeam(playerDbConnection.getPlayer(victim).getTeamId()).getColor();
		killer = StringEditor.FirstLetterToUpper(killer);
		victim = StringEditor.FirstLetterToUpper(victim);
		Bukkit.broadcastMessage((teamColorKiller + killer) + (ChatColor.GOLD + " bekommt ") + (ChatColor.AQUA + "" + bounty + " Diamanten") + (ChatColor.GOLD + " für den Kill an ") + (teamColorVictim + victim));
	}
}