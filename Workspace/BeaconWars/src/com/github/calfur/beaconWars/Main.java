package com.github.calfur.beaconWars;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.calfur.beaconWars.beaconFight.BeaconFightManager;
import com.github.calfur.beaconWars.commands.CommandProjectStart;
import com.github.calfur.beaconWars.commands.CommandRegistrator;
import com.github.calfur.beaconWars.configuration.ConfigurationManager;
import com.github.calfur.beaconWars.configuration.IConfiguration;
import com.github.calfur.beaconWars.database.KillDbConnection;
import com.github.calfur.beaconWars.database.PlayerDbConnection;
import com.github.calfur.beaconWars.database.TeamDbConnection;
import com.github.calfur.beaconWars.database.TransactionDbConnection;
import com.github.calfur.beaconWars.disabling.FeatureDisabler;
import com.github.calfur.beaconWars.hungerGamesLootDrop.HungerGamesManager;
import com.github.calfur.beaconWars.pvp.DeathBanPluginInteraction;
import com.github.calfur.beaconWars.pvp.TeamAttackManager;

public class Main extends JavaPlugin {
	private static Main instance;
	
	private final ConfigurationManager configurationManager = new ConfigurationManager();
	private final IConfiguration configuration = configurationManager.getConfiguration();
	private final PlayerDbConnection playerDbConnection = new PlayerDbConnection();
	private final TeamDbConnection teamDbConnection = new TeamDbConnection();
	private final KillDbConnection killDbConnection = new KillDbConnection();
	private final TransactionDbConnection transactionDbConnection = new TransactionDbConnection();
	
	private TeamAttackManager teamAttackManager;
	private PlayerModeManager playerModeManager;
	private BeaconFightManager beaconFightManager;
	private static ScoreboardLoader scoreboardLoader;
	private CommandProjectStart commandProjectStart;
	private HungerGamesManager hungergamesManager;
	private CompassManager compassManager;
	private RewardManager rewardManager;
		
	@Override
	public void onEnable() {
		instance = this;
		
		playerDbConnection.loadDatabase();
		teamDbConnection.loadDatabase();
		killDbConnection.loadDatabase();
		transactionDbConnection.loadDatabase();

		teamAttackManager = new TeamAttackManager();
		beaconFightManager = new BeaconFightManager();
		playerModeManager = new PlayerModeManager();
		scoreboardLoader = new ScoreboardLoader();
		hungergamesManager = new HungerGamesManager();
		compassManager = new CompassManager();
		rewardManager = new RewardManager();
		
		new FeatureDisabler(); 
		new CommandRegistrator();
		new EventRegistrator();

		updateDeathBanDuration();
		
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

	public TransactionDbConnection getTransactionDbConnection() {		
		return transactionDbConnection;
	}

	public RewardManager getRewardManager() {
		return rewardManager;
	}
	
	public IConfiguration getConfiguration() {
		return configuration;
	}

	public boolean reloadConfiguration() {
		return configurationManager.reloadConfiguration();
	}
	
	public void updateDeathBanDuration() {
		int timeInMinutes;
		if(beaconFightManager.isBeaconEventActive()) {
			timeInMinutes = configuration.getDeathBanDurationDuringBeaconFightInMinutes();
		}else {
			timeInMinutes = configuration.getDeathBanDurationInMinutes();
		}
		DeathBanPluginInteraction.tryChangeBanDuration(timeInMinutes);	
	}
}
