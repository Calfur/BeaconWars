package com.github.calfur.beaconWars.configuration;

import org.bukkit.Location;

public interface IConfiguration {
	public String getScoreboardTitle();
	public Integer getRewardBeaconDefenseTotalDiamonds();
	public Integer getRewardBeaconDefenseTotalPoints();
	public Integer getRewardBeaconRaidSuccessDiamonds();
	public Integer getRewardBeaconRaidSuccessPoints();
	public Integer getRewardKillBountyMultiplicatorDiamonds();
	public Integer getRewardKillBountyMultiplicatorPoints();
	public Boolean areHungerGamesEnabled();
	public Location getHungerGamesLocation();
	public Boolean areTeamPointsDisplayedInScoreboard();
	public Integer getTotemCooldownLengthInMinutes();
}
