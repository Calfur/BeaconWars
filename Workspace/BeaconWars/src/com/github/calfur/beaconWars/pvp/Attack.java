package com.github.calfur.beaconWars.pvp;

public class Attack {
	private Team attacker;
	private Team defender;
	
	public Attack(Team attacker, Team defender) {
		this.attacker = attacker;
		this.defender = defender;
	}
	public Team getAttacker() {
		return attacker;
	}
	public Team getDefender() {
		return defender;
	}
	
}
