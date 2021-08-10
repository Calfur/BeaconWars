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

public abstract class DbConnection<Data extends IData, Json extends ConfigurationSerializable> {
	protected final File folder = new File("plugins//DiamondKillDatabase");
	protected final File file;
		
	protected HashMap<String, Json> jsons = new HashMap<>();
	
	private Data data;
	private Class<Data> dataClass;
	
	protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private FileWriter writer;
	private FileReader reader;
	
	public DbConnection(String fileName, Data data, Class<Data> dataClass) {
		this.data = data;
		file = new File(folder + "//" + fileName);
		this.dataClass = dataClass;
	}
		
	public void loadDatabase() {
		if(folder.exists()) {
			if(file.exists()) {
				data = read();
				if(data != null) {
					jsons = mapDataToHashMap();
				}
			}else {
				createFile().saveFile().loadDatabase();
			}
		}else {
			createFolder().loadDatabase();
		}
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
	
	private DbConnection<Data, Json> createFolder() {
		folder.mkdirs();
		return this;
	}
	
	private DbConnection<Data, Json> createFile() {
		try {
			file.createNewFile();
			gson.toJson(data, writer());
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	private DbConnection<Data, Json> saveFile() {
		try {
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	public void addJson(String key, Json value) {
		data.getData().put(key, value.serialize());
		jsons.put(key, value);
		write(data);
	}	
	
	public void removeJson(String key) {
		data.getData().remove(key);
		jsons.remove(key);
		write(data);
	}
	
	private Data read() {
		return gson.fromJson(reader(), dataClass);
	}
	
	private void write(Data data) {
		gson.toJson(data, writer());
		saveFile();
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

}
