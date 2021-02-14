package com.github.calfur.minecraftserverplugins.diamondkill;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerConfig {
	private final File folder = new File("plugins//DiamondKillDatabase");
	private final File file = new File(folder + "//player.json");
	
	private HashMap<String, JsonPlayer> player = new HashMap<>();
	
	private PlayerData data = new PlayerData();
	
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private FileWriter writer;
	private FileReader reader;
	
	private PlayerConfig createFolder() {
		folder.mkdirs();
		return this;
	}
	
	private PlayerConfig createFile() {
		try {
			file.createNewFile();
			gson.toJson(data, writer());
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	public boolean existsPlayer(String key) {
		if(player != null && player.get(key) != null) {
			return true;
		}
		return false;
	}
	
	public PlayerConfig saveConfig() {
		try {
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	public PlayerConfig addPlayer(String key, JsonPlayer value) {
		data.getPlayer().put(key, value.serialize());
		gson.toJson(data, writer());
		return this;
	}	
	
	public PlayerConfig removePlayer(String key) {
		data.getPlayer().remove(key);
		player.remove(key);
		gson.toJson(data, writer());
		return this;
	}
	
	public void loadConfig() {
		if(folder.exists()) {
			if(file.exists()) {
				if(read() != null) {
					read().getPlayer().entrySet().forEach(entry -> {						
						player.put(entry.getKey(), JsonPlayer.deserialize((Map<String, Object>) entry.getValue()));
						data.getPlayer().put(entry.getKey(), entry.getValue());
					});
				}
			}else {
				createFile().saveConfig().loadConfig();
			}
		}else {
			createFolder().saveConfig().loadConfig();
		}
	}
	
	public PlayerData read() {
		return gson.fromJson(reader(), PlayerData.class);
	}
	
	private FileWriter writer() {
		try {
			writer = new FileWriter(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return writer;
	}	
	private FileReader reader() {
		try {
			reader = new FileReader(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return reader;
	}
		
	public JsonPlayer getPlayer(String key) {
		return player.get(key);
	}
}
