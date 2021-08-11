package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class DbConnection<Json extends ConfigurationSerializable> {
	protected HashMap<String, Json> jsons = new HashMap<>();
	
	private Data data = new Data();

	private final File folder = new File("plugins//DiamondKillDatabase");
	private final File file;
		
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
	public DbConnection(String fileName) {
		file = new File(folder + "//" + fileName);
	}
		
	public void loadDatabase() {
		if(folder.exists()) {
			if(file.exists()) {
				data = read();
				if(data != null) {
					jsons = mapDataToHashMap();
				}
			}else {
				createFile().loadDatabase();
			}
		}else {
			createFolder().loadDatabase();
		}
	}
	
	protected void addJson(String key, Json value) {
		data.getData().put(key, value.serialize());
		jsons.put(key, value);
		write(data);
	}	
	
	protected void removeJson(String key) {
		data.getData().remove(key);
		jsons.remove(key);
		write(data);
	}
	
	protected abstract Json deserialize(Map<String, Object> serializedJson);

	@SuppressWarnings("unchecked")
	private HashMap<String, Json> mapDataToHashMap() {
		HashMap<String, Json> result = new HashMap<>();
		data.getData().entrySet().forEach(entry -> {						
			result.put(entry.getKey(), deserialize((Map<String, Object>) entry.getValue()));
		});
		return result;
	}
	
	private DbConnection<Json> createFolder() {
		folder.mkdirs();
		return this;
	}
	
	private DbConnection<Json> createFile() {
		try {
			FileWriter writer = writer();
			file.createNewFile();
			gson.toJson(data, writer);
			saveFile(writer);
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	private DbConnection<Json> saveFile(FileWriter writer) {
		try {
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	private Data read() {
		return gson.fromJson(reader(), Data.class);
	}
	
	private void write(Data data) {
		FileWriter writer = writer();
		gson.toJson(data, writer);
		saveFile(writer);
	}
	
	private FileWriter writer() {
		try {
			return new FileWriter(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	private FileReader reader() {
		try {
			return new FileReader(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
