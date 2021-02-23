package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.entity.Player;

public class PlayerKicker implements Runnable {
	private Player playerToKick;
	
	public PlayerKicker(Player playerToKick) {
		this.playerToKick = playerToKick;
	}

	@Override
	public void run() {
		playerToKick.kickPlayer("Du bist nicht registriert und noch keinem Team zugewiesen. Melde dich bitte über Discord bei den Admins.");
	}
	
}
