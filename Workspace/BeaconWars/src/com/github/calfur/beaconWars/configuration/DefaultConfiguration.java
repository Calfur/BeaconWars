package com.github.calfur.beaconWars.configuration;

public class DefaultConfiguration implements IConfiguration{
	private static final String scoreboardTitle = "Beacon Wars";

	@Override
	public String getScoreboardTitle() {
		return scoreboardTitle;
	}
	
}
