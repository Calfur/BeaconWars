package com.github.calfur.minecraftserverplugins.diamondkill.database;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class DbConnection<Data extends IData> {
	protected final File folder = new File("plugins//DiamondKillDatabase");
	protected final File file;
		
	protected Data data;
	private Class<Data> dataClass;
	
	protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private FileWriter writer;
	private FileReader reader;
	
	public DbConnection(String fileName, Data data, Class<Data> dataClass) {
		this.data = data;
		file = new File(folder + "//" + fileName);
		this.dataClass = dataClass;
	}
	
	protected DbConnection<Data> createFolder() {
		folder.mkdirs();
		return this;
	}
	
	protected DbConnection<Data> createFile() {
		try {
			file.createNewFile();
			gson.toJson(data, writer());
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	public DbConnection<Data> saveConfig() {
		try {
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return this;
	}
	
	public Data read() {
		return gson.fromJson(reader(), dataClass);
	}
	
	protected FileWriter writer() {
		try {
			writer = new FileWriter(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return writer;
	}	
	protected FileReader reader() {
		try {
			reader = new FileReader(file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return reader;
	}

	protected abstract void loadConfig();
}
