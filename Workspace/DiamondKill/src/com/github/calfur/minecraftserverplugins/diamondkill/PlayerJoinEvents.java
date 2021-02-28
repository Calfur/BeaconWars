package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEvents implements Listener{
	private ScoreboardLoader scoreboardLoader = Main.getInstance().getScoreboardLoader();

	@EventHandler
	public void onPlayerJoins(PlayerJoinEvent event) {
		Player joiner = event.getPlayer();
		scoreboardLoader.ReloadScoreboardFor(joiner);
	}
	
}
