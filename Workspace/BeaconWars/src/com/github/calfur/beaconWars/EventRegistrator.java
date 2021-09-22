package com.github.calfur.beaconWars;

import com.github.calfur.beaconWars.beaconFight.BeaconDropEvent;
import com.github.calfur.beaconWars.beaconFight.BeaconItemEvents;
import com.github.calfur.beaconWars.beaconFight.CompassRightClickEvent;
import com.github.calfur.beaconWars.beaconFight.EnderPerlTeleportEvent;
import com.github.calfur.beaconWars.beaconFight.MoveBeaconIntoChestEvent;
import com.github.calfur.beaconWars.beaconFight.TeleportEvents;
import com.github.calfur.beaconWars.pvp.KillEvents;

public class EventRegistrator {
	public EventRegistrator() {
		registrateEvents();
	}
	private void registrateEvents() {
		Main plugin = Main.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new KillEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerJoinEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerLeaveEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerRespawnEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new BlockBreakEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PlayerModeRefreshEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new TeleportEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new BeaconItemEvents(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new EnderPerlTeleportEvent(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new BeaconDropEvent(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new MoveBeaconIntoChestEvent(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new CompassRightClickEvent(), plugin);
	}
}
