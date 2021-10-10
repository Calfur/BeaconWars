package com.github.calfur.beaconWars;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.calfur.beaconWars.beaconFight.BeaconDropEvent;
import com.github.calfur.beaconWars.beaconFight.BeaconItemEvents;
import com.github.calfur.beaconWars.beaconFight.CompassRightClickEvent;
import com.github.calfur.beaconWars.beaconFight.EnderPerlTeleportEvent;
import com.github.calfur.beaconWars.beaconFight.MoveBeaconIntoChestEvent;
import com.github.calfur.beaconWars.beaconFight.TeleportEvents;
import com.github.calfur.beaconWars.pvp.KillEvents;

public class EventRegistrator {
	private List<Listener> events = Arrays.asList(
		new KillEvents(),
		new PlayerJoinEvents(),
		new PlayerLeaveEvents(),
		new PlayerRespawnEvents(),
		new BlockBreakEvents(),
		new PlayerModeRefreshEvents(),
		new TeleportEvents(),
		new BeaconItemEvents(),
		new EnderPerlTeleportEvent(),
		new BeaconDropEvent(),
		new MoveBeaconIntoChestEvent(),
		new CompassRightClickEvent()		
	); 
	
	public EventRegistrator() {
		registrateEvents();
	}
	
	private void registrateEvents() {
		for (Listener event : events) {
			registrateEvent(event);
		}
	}
	
	private void registrateEvent(Listener event) {
		Plugin plugin = Main.getInstance();
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		pluginManager.registerEvents(event, plugin);
	}
}
