package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.BeaconFightManager;

public class PlayerJoinEvents implements Listener{
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();
	private PlayerModeManager playerModeManager = Main.getInstance().getPlayerModeManager();
	private BeaconFightManager beaconFightManager = Main.getInstance().getBeaconFightManager();

	@EventHandler
	public void onPlayerJoins(PlayerJoinEvent event) {
		Player joiner = event.getPlayer();
		scoreboardLoader.reloadScoreboardFor(joiner);
		
		playerModeManager.reloadPlayerMode(joiner);
		
		if(beaconFightManager.isBeaconEventActive() && joiner.getLocation().getWorld().getEnvironment() != World.Environment.NORMAL) {
			BeaconManager.teleportPlayerToBeacon(joiner);						
		}
	}
	
}
