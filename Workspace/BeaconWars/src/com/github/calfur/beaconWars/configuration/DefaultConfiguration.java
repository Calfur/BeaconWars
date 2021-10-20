package com.github.calfur.beaconWars.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DefaultConfiguration implements IConfiguration{
	/***
	 * The title of the scoreboard
	 */
	private static final String scoreboardTitle = "Beacon Wars";
	/***
	 * The here set amount of points gets removed from the team whose beacon gets stolen and returned
	 */
	private static final int deductionBeaconRaidLostDefensePoints = -500;
	/***
	 * The reward given to the player who steals a beacon and brings it back
	 */
	private static final int rewardBeaconRaidSuccessDiamonds = 5;
	private static final int rewardBeaconRaidSuccessPoints = 1000;
	/***
	 * The multiplicator of the bounty reward.
	 * Example 1: When a player has a bounty of 2 and gets killed, the killer gets 2x the set reward.
	 * If the reward is set to 5, the player will get 10
	 * 
	 * Example 2: When a player has a bounty of 3 and gets killed, the killer gets 3x the set reward.
	 * If the reward is set to 0, the player will get 0
	 */
	private static final int rewardKillBountyMultiplicatorDiamonds = 1;
	private static final int rewardKillBountyMultiplicatorPoints = 50;
	/***
	 * If this is set to true every full hour spawns a diamond
	 */
	private static final boolean areHungerGamesEnabled = true;
	/***
	 * X coordinate where the hunger game diamond spawns
	 */
	private static final double hungerGamesLocationX = 0.5;
	/***
	 * Y coordinate where the hunger game diamond spawns
	 */
	private static final double hungerGamesLocationY = 80;
	/***
	 * Z coordinate where the hunger game diamond spawns
	 */
	private static final double hungerGamesLocationZ = 0.5;
	/***
	 * World name where the hunger game diamond spawns
	 */
	private static final String hungerGamesLocationWorld = "world";
	/***
	 * Minimum amout of teams of which at least one player must be online, that the hunger games diamond spawns. 
	 * If this is set to 0, the diamond will as well spawn if no one is online.
	 */
	private static final int hungerGamesMinimumAmountOfRequiredOnlineTeams = 2;
	/***
	 * If this is set to true the team points are displayed in the scoreboard
	 */
	private static final boolean areTeamPointsDisplayedInScoreboard = true;
	/***
	 * The duration after using a totem until the player is able to use another one
	 */
	private static final int totemCooldownLengthInMinutes = 15;
	/***
	 * The time until the attack disapears from the scoreboard, after a player gets damage by player from another team.
	 * In this time players from both teams can't switch to the buildmode
	 */
	private static final int attackDurationInSeconds = 180;
	/***
	 * The time until the death does not count as kill anymore.
	 * Example: This configuration is set to 60. 
	 * Player A hits Player B with his bow.
	 * After 24s Blayer B dies in lava.
	 * This death counts as kill for Player A
	 */
	private static final int timeUntilDeathCountsNotAsKillInSeconds = 60;
	/***
	 * The cooldown until a player can use the buildmode again after exiting it 
	 */
	private static final int buildModeCooldownInMinutes = 30;
	/***
	 * The radius around the team beacon in which team members are able to activate the buildmode
	 */
	private static final int buildModeBaseRangeRadiusInBlocks = 100;
	/***
	 * The duration of the ban after a player dies
	 */
	private static final int deathBanDurationInMinutes = 10;
	/***
	 * The duration of the ban after a player dies during a beacon event 
	 */
	private static final int deathBanDurationDuringBeaconFightInMinutes = 2;
	/***
	 * World name where the base beacons for the teams get created
	 */
	private static final String beaconEventWorldName = "world";

	@Override
	public String getScoreboardTitle() {
		return scoreboardTitle;
	}

	@Override
	public Integer getDeductionBeaconRaidLostDefensePoints() {
		return deductionBeaconRaidLostDefensePoints;
	}

	@Override
	public Integer getRewardBeaconRaidSuccessDiamonds() {
		return rewardBeaconRaidSuccessDiamonds;
	}

	@Override
	public Integer getRewardBeaconRaidSuccessPoints() {
		return rewardBeaconRaidSuccessPoints;
	}

	@Override
	public Integer getRewardKillBountyMultiplicatorDiamonds() {
		return rewardKillBountyMultiplicatorDiamonds;
	}

	@Override
	public Integer getRewardKillBountyMultiplicatorPoints() {
		return rewardKillBountyMultiplicatorPoints;
	}
	
	@Override
	public Boolean areHungerGamesEnabled() {
		return areHungerGamesEnabled;
	}
	
	@Override
	public Location getHungerGamesLocation() {
		return new Location(
			Bukkit.getWorld(hungerGamesLocationWorld),
			hungerGamesLocationX,
			hungerGamesLocationY,
			hungerGamesLocationZ
		);
	}
	
	@Override
	public Integer getHungerGamesMinimumAmountOfRequiredOnlineTeams() {
		return hungerGamesMinimumAmountOfRequiredOnlineTeams;
	}
	
	@Override
	public Boolean areTeamPointsDisplayedInScoreboard() {
		return areTeamPointsDisplayedInScoreboard;
	}
	
	@Override
	public Integer getTotemCooldownLengthInMinutes() {
		return totemCooldownLengthInMinutes;
	}

	@Override
	public Integer getAttackDurationInSeconds() {
		return attackDurationInSeconds;
	}

	@Override
	public Integer getTimeUntilDeathCountsNotAsKillInSeconds() {
		return timeUntilDeathCountsNotAsKillInSeconds;
	}

	@Override
	public Integer getBuildModeCooldownInMinutes() {
		return buildModeCooldownInMinutes;
	}

	@Override
	public Integer getBuildModeBaseRangeRadiusInBlocks() {
		return buildModeBaseRangeRadiusInBlocks;
	}

	@Override
	public Integer getDeathBanDurationInMinutes() {
		return deathBanDurationInMinutes;
	}

	@Override
	public Integer getDeathBanDurationDuringBeaconFightInMinutes() {
		return deathBanDurationDuringBeaconFightInMinutes;
	}

	@Override
	public String getBeaconEventWorldName() {
		return beaconEventWorldName;
	}
}
