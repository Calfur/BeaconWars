package com.github.calfur.beaconWars.configuration;

public class DynamicConfiguration implements IConfiguration {
	private String scoreboardTitle;
	
	@Override
	public String getScoreboardTitle() {
		return scoreboardTitle;
	}
	
	public DynamicConfiguration(
			String scoreboardTitle
	){
		this.scoreboardTitle = scoreboardTitle;
	}
}
