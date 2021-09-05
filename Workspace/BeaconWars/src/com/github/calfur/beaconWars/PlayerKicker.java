package com.github.calfur.beaconWars;

import org.bukkit.entity.Player;

public class PlayerKicker implements Runnable {
	private Player playerToKick;
	
	public PlayerKicker(Player playerToKick) {
		this.playerToKick = playerToKick;
	}

	@Override
	public void run() {
		playerToKick.kickPlayer("Du bist nicht registriert und noch keinem Team zugewiesen. Melde dich bitte bei einem Admin.");
	}
	
}
