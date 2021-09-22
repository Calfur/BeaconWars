package com.github.calfur.beaconWars;

public class Reward {
	private int diamonds;
	private int points;

	public int getDiamonds() {
		return diamonds;
	}
	
	public int getPoints() {
		return points;
	}
	
	public Reward(int diamonds, int points) {
		this.diamonds = diamonds;
		this.points = points;
	}
}
