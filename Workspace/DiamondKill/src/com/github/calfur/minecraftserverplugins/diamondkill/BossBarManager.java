package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BossBarManager {
	private List<CustomBossBar> customBossBars = new ArrayList<CustomBossBar>();
	
	public CustomBossBar addBossBar(String title, ChatColor color, LocalDateTime countdownEnd) {
		CustomBossBar customBossBar = new CustomBossBar(this, title, color, countdownEnd);
		customBossBars.add(customBossBar);
		return customBossBar;
	}
	
	public CustomBossBar getBossBar(String title) {
		for (CustomBossBar customBossBar : customBossBars) {
			if(customBossBar.getTitle().equalsIgnoreCase(title)) {
				return customBossBar;
			}
		}
		return null;
	}
	
	public void displayAllBossBarsTo(Player player) {
		for (CustomBossBar customBossBar : customBossBars) {
			customBossBar.addPlayer(player);
		}
	}
	
	public void removeBossBar(CustomBossBar customBossBar) {
		customBossBars.remove(customBossBar);
		customBossBar.destroy();
	}
	
	public void removeBossBar(String name) {
		CustomBossBar customBossBar = getBossBar(name);
		if(customBossBar != null) {			
			removeBossBar(customBossBar);
		}
	}
}
