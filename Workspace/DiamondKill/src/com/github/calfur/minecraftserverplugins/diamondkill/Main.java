package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.calfur.minecraftserverplugins.diamondkill.database.KillDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.PlayerDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.database.TeamDbConnection;
import com.github.calfur.minecraftserverplugins.diamondkill.disabling.FeatureDisabler;

public class Main extends JavaPlugin {
	private static Main instance;
	private final PlayerDbConnection playerDbConnection = new PlayerDbConnection();
	private final TeamDbConnection teamDbConnection = new TeamDbConnection();
	private final KillDbConnection killDbConnection = new KillDbConnection();
	private static ScoreboardLoader scoreboardLoader;
	@Override
	public void onEnable() {
		instance = this;
		scoreboardLoader = new ScoreboardLoader();
		
		new FeatureDisabler(); 
		new CommandRegistrator();
		new EventRegistrator();

		playerDbConnection.loadConfig();
		teamDbConnection.loadConfig();
		killDbConnection.loadConfig();
		
		scoreboardLoader.setTopKiller(TopKiller.getCurrentTopKiller());
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
	
	public ScoreboardLoader getScoreboardLoader() {
		return scoreboardLoader;
	}
}
