package com.github.calfur.minecraftserverplugins.diamondkill;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DeathBanPluginInteraction {
	private static File configFile = new File("plugins//DeathBan", "config.yml");
	private static FileConfiguration configFileConfiguration = YamlConfiguration.loadConfiguration(configFile);

	public static void tryChangeBanDuration(int minutes) {
		String time = minutes + "m";
		configFileConfiguration.set("default.time", time);
		
		try {
			configFileConfiguration.save(configFile);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "deathban reload");
	}
}
