package com.github.calfur.minecraftserverplugins.diamondkill.beaconFight;

import java.time.LocalDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;
import com.github.calfur.minecraftserverplugins.diamondkill.Team;

import net.md_5.bungee.api.ChatColor;

public class BeaconRaid {
	private Team attacker;
	private Team defender;
	private LocalDateTime deadline;
	private String bossBarName;
	
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
	
	public BeaconRaid(Team attacker, Team defender, Player causer) {
		this.attacker = attacker;
		this.defender = defender;
		this.deadline = LocalDateTime.now().plusMinutes(15);
		
		Bukkit.broadcastMessage(causer.getName() + " von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " hat den Beacon von " + defender.getColor() + "Team " + defender.getId() + ChatColor.RESET + " abgebaut");
		Bukkit.broadcastMessage("Der Beacon muss innerhalb von 15min");
		Bukkit.broadcastMessage("zurück zur Basis von " + attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " gebracht werden");
	
		bossBarName = attacker.getColor() + "Team " + attacker.getId() + ChatColor.RESET + " klaut den Beacon von " + defender.getColor() + "Team " + defender.getId();
		
		Main.getInstance().getScoreboardLoader().addBossBar(bossBarName, attacker.getColor(), deadline);
	}
	
	
}
