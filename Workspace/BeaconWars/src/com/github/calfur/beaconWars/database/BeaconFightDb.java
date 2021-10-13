package com.github.calfur.beaconWars.database;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BeaconFightDb extends DbConnection<BeaconFightJson>{

	public BeaconFightDb() {
		super("beaconFight.json");
	}

	@Override
	protected BeaconFightJson deserialize(Map<String, Object> serializedJson) {
		return BeaconFightJson.deserialize((Map<String, Object>) serializedJson);
	}
	
	/**
	 * @param BeaconFightJson
	 * @return BeaconFight id
	 */
	public String addBeaconFight(BeaconFightJson beaconFightJson) {
		String id = Integer.toString(getNextId());
		addJson(id, beaconFightJson);
		return id;
	}	
	
	public void removeBeaconFight(int beaconFightId) {
		String id = Integer.toString(beaconFightId);
		removeJson(id);
	}
	
	public void removeBeaconFight(LocalDateTime startTime) {
		for (Entry<String, BeaconFightJson> beaconFight : jsons.entrySet()) {
			if(beaconFight.getValue().getStartTime().isEqual(startTime)) {
				removeJson(beaconFight.getKey());
				return;
			}
		}
	}

	public BeaconFightJson getBeaconFight(int beaconFightId) {
		String id = Integer.toString(beaconFightId);
		return jsons.get(id.toLowerCase());
	}
	
	public HashMap<String, BeaconFightJson> getBeaconFights() {
		return jsons;
	}

	private int getNextId() {
		int heighestId = 0;
		for (Entry<String, BeaconFightJson> beaconFight : jsons.entrySet()) {
			int id = Integer.parseInt(beaconFight.getKey());
			if(heighestId < id) {
				heighestId = id;
			}
		}
		return heighestId + 1;
	}
}
