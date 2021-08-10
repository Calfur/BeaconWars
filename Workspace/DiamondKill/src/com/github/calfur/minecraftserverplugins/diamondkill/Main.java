package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.calfur.minecraftserverplugins.diamondkill.beaconFight.BeaconFightManager;
import com.github.calfur.minecraftserverplugins.diamondkill.commands.CommandProjectStart;
import com.github.calfur.minecraftserverplugins.diamondkill.commands.CommandRegistrator;
import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.disabling.FeatureDisabler;
import com.github.calfur.minecraftserverplugins.diamondkill.hungerGamesLootDrop.HungerGamesManager;

public class Main extends JavaPlugin {
	private static Main instance;
	private final PlayerDbConnection playerDbConnection = new PlayerDbConnection();
	private final TeamDbConnection teamDbConnection = new TeamDbConnection();
	private final KillDbConnection killDbConnection = new KillDbConnection();
	private TeamAttackManager teamAttackManager;
	private PlayerModeManager playerModeManager;
	private BeaconFightManager beaconFightManager;
	private static ScoreboardLoader scoreboardLoader;
	private CommandProjectStart commandProjectStart;
	private HungerGamesManager hungergamesManager;
	private CompassManager compassManager;
		
	@Override
	public void onEnable() {
		instance = this;
		
		playerDbConnection.loadDatabase();
		teamDbConnection.loadDatabase();
		killDbConnection.loadDatabase();

		teamAttackManager = new TeamAttackManager();
		beaconFightManager = new BeaconFightManager();
		playerModeManager = new PlayerModeManager();
		scoreboardLoader = new ScoreboardLoader();
		hungergamesManager = new HungerGamesManager();
		compassManager = new CompassManager();
		
		new FeatureDisabler(); 
		new CommandRegistrator();
		new EventRegistrator();

		DeathBanPluginInteraction.tryChangeBanDuration(10);
		
		hungergamesManager.startItemSpawner();
		
		playerModeManager.reloadPlayerModeForAllOnlinePlayers();
	}
	
	@Override
	public void onDisable() {
	}
	
	public static Main getInstance() {
		return instance;
	}

	public PlayerDbConnection getPlayerDbConnection() {
		return playerDbConnection;
	}

	public TeamDbConnection getTeamDbConnection() {
		return teamDbConnection;
	}
	
	public KillDbConnection getKillDbConnection() {
		return killDbConnection;
	}
	
	public TeamAttackManager getTeamAttackManager() {
		return teamAttackManager;
	}

	public ScoreboardLoader getScoreboardLoader() {
		return scoreboardLoader;
	}	
	
	public PlayerModeManager getPlayerModeManager() {
		return playerModeManager;
	}

	public BeaconFightManager getBeaconFightManager() {
		return beaconFightManager;
	}

	public void setCommandProjectStart(CommandProjectStart commandProjectStart) {
		this.commandProjectStart = commandProjectStart;		
	}
	
	public CommandProjectStart getCommandProjectStart() {
		return commandProjectStart;		
	}

	public CompassManager getCompassManager() {
		return compassManager;
	}
}
