package com.github.calfur.beaconWars.pvp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import com.github.calfur.beaconWars.Main;
import com.github.calfur.beaconWars.PlayerKicker;
import com.github.calfur.beaconWars.PlayerModeManager;
import com.github.calfur.beaconWars.Reward;
import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.commands.CommandProjectStart;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.helperClasses.KillHelper;
import com.github.calfur.beaconWars.helperClasses.StringFormatter;

public class KillEvents implements Listener {
	private IConfiguration configuration = Main.getInstance().getConfiguration();
	private PlayerDbConnection playerDbConnection = Main.getInstance().getPlayerDbConnection(); 
	private TeamDbConnection teamDbConnection = Main.getInstance().getTeamDbConnection(); 
	
	private PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
	private TeamAttackManager teamAttackManager = Main.getInstance().getTeamAttackManager();
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();
	private CommandProjectStart commandProjectStart = Main.getInstance().getCommandProjectStart();
	
	private ArrayList<LatestHitByPlayer> latestHitByPlayers = new ArrayList<LatestHitByPlayer>();
	
	private boolean tryAddPlayerHit(Player defender, Player attacker) {
		String defenderName = defender.getName().toLowerCase();
		String attackerName = attacker.getName().toLowerCase();
		if(!commandProjectStart.isProjectActive()) {
			return false;
		}
		if(!playerDbConnection.existsPlayer(attackerName)) {
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(attacker));	
			return false;
		}
		if(!playerDbConnection.existsPlayer(defenderName)) {
			Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new PlayerKicker(defender));			
			return false;
		}
		if(!playerModeManager.isPlayerAllowedToFight(defender)) {
			attacker.sendMessage(StringFormatter.error("Dieser Spieler kann nicht angegriffen werden, weil er im Baumodus ist"));
			return false;
		}
		if(!playerModeManager.isPlayerAllowedToFight(attacker)) {
			attacker.sendMessage(StringFormatter.error("Du kannst keine Spieler angreifen, w�hrend du im Baumodus bist"));
			return false;
		}
		if(playerDbConnection.isPlayerSpectator(defenderName)) {
			attacker.sendMessage(StringFormatter.error("Dieser Spieler kann nicht angegriffen werden, weil er Spectator ist"));
			return false;
		}
		if(playerDbConnection.isPlayerSpectator(attackerName)) {
			attacker.sendMessage(StringFormatter.error("Du kannst keine Spieler angreifen, weil du Spectator bist"));
			return false;
		}
		int defenderTeam = playerDbConnection.getPlayer(defenderName).getTeamId();
		int attackerTeam = playerDbConnection.getPlayer(attackerName).getTeamId();
		if(defenderTeam != attackerTeam) {			
			latestHitByPlayers.remove(GetLatestHitByPlayer(defenderName));
			latestHitByPlayers.add(new LatestHitByPlayer(defenderName, attackerName));
			teamAttackManager.registrateHit(attackerTeam, defenderTeam);
		}
		return true;
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
			boolean isHitAllowed = true;
			
			switch(attacker.getType()) {
				case PLAYER:
					isHitAllowed = tryAddPlayerHit(defender, (Player)attacker);		
					break;
				case ARROW:
					Arrow arrow = (Arrow)attacker;
					ProjectileSource arrowShooter = arrow.getShooter();
					if(arrowShooter instanceof Player) {
						Player shooter = (Player)arrowShooter;
						isHitAllowed = tryAddPlayerHit(defender, shooter);					
					}
					break;
				case SPECTRAL_ARROW:
					SpectralArrow spectralArrow = (SpectralArrow)attacker;
					ProjectileSource spectralArrowShooter = spectralArrow.getShooter();
					if(spectralArrowShooter instanceof Player) {
						Player shooter = (Player)spectralArrowShooter;
						isHitAllowed = tryAddPlayerHit(defender, shooter);								
					}
					break;
				case TRIDENT:
					Trident trident = (Trident)attacker;
					ProjectileSource tridentShooter = trident.getShooter();
					if(tridentShooter instanceof Player) {
						Player shooter = (Player)tridentShooter;		
						isHitAllowed = tryAddPlayerHit(defender, shooter);						
					}
					break;
				default:
					isHitAllowed = true;
					break;
			}
			if(!isHitAllowed) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDies(PlayerDeathEvent event) {
		Player player = (Player)event.getEntity();
		removeBeaconsFromLoot(event.getDrops(), player);
		LatestHitByPlayer latestHitByPlayer = GetLatestHitByPlayer(player.getName());
		if(latestHitByPlayer != null) {
			long secondsBetween = ChronoUnit.SECONDS.between(latestHitByPlayer.getDateTime(), LocalDateTime.now());
			if(secondsBetween < configuration.getTimeUntilDeathCountsNotAsKillInSeconds()) {
				removeUndroppableItems(event.getDrops());
				
				String killer = latestHitByPlayer.getAttacker();
				String victim = latestHitByPlayer.getDefender();
				
				Reward payedReward = KillHelper.addKill(
						killer, 
						victim, 
						"F�r das killen von " + victim,
						Main.getInstance());
				
				sendDeathMessage(killer, victim, payedReward);
			}
		}
	}
	
	private void sendDeathMessage(String killer, String victim, Reward payedReward) {
		ChatColor teamColorKiller = teamDbConnection.getTeam(playerDbConnection.getPlayer(killer).getTeamId()).getColor();
		ChatColor teamColorVictim = teamDbConnection.getTeam(playerDbConnection.getPlayer(victim).getTeamId()).getColor();
		killer = StringFormatter.firstLetterToUpper(killer);
		victim = StringFormatter.firstLetterToUpper(victim);
		Bukkit.broadcastMessage(teamColorKiller + killer + ChatColor.RESET +" bekommt " + StringFormatter.rewardText(payedReward) + " f�r den Kill an " + teamColorVictim + victim);
	}
	
	private void removeUndroppableItems(List<ItemStack> loot) {

		for (ItemStack itemStack : loot) {
			switch (itemStack.getType()) {
				case DIAMOND_BOOTS:
				case DIAMOND_CHESTPLATE:
				case DIAMOND_HELMET: 
				case DIAMOND_LEGGINGS:
					itemStack.setType(null);
					break;
				default:
					break;
			}
			
		}		
	}
	
	private void removeBeaconsFromLoot(List<ItemStack> loot, Player owner) {
		for (ItemStack itemStack: loot) {
			switch (itemStack.getType()) {
				case BEACON:
					if(beaconFightManager.isBeaconEventActive()) {	
						beaconFightManager.getOngoingBeaconFight().removeBeaconRaidsByDestructor(owner);
					}
					itemStack.setType(null);
					break;
				default:
					break;
			}
		}
	}
}