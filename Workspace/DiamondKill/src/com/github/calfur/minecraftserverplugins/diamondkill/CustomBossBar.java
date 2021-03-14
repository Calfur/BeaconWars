package com.github.calfur.minecraftserverplugins.diamondkill;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class CustomBossBar {
	private BossBarManager manager;
	private BossBar bossBar;
	private String title;
	private int taskId;
	private double bossBarStepPerSecond;

	public BossBarManager getManager() {
		return manager;
	}
	
	public BossBar getBossBar() {
		return bossBar;
	}

	public int getTaskId() {
		return taskId;
	}
	
	public String getTitle() {
		return title;
	}

	public double getBossBarStepPerSecond() {
		return bossBarStepPerSecond;
	}
	
	public CustomBossBar(BossBarManager manager, String title, ChatColor chatColor, LocalDateTime countdownEnd) {
		this.manager = manager;
		this.title = title;
		bossBar = Bukkit.createBossBar(title, getParsedChatColorOrWhite(chatColor), BarStyle.SOLID);
		activateCountdown(countdownEnd);
	}
	
	public void addPlayer(Player player) {
		bossBar.addPlayer(player);
	}
	
	private BarColor getParsedChatColorOrWhite(ChatColor chatColor) {
		BarColor result;
		try {			
			result = BarColor.valueOf(chatColor.toString());
		}catch(IllegalArgumentException e) {			
			result = BarColor.WHITE;
		}
		return result;
	}
	
	private void activateCountdown(LocalDateTime countdownEnd) {
		double countDownDurationInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), countdownEnd);
		bossBarStepPerSecond = 1 / countDownDurationInSeconds;
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new BossBarProgresser(this), 20, 20);
	}

	public void destroy() {
		bossBar.removeAll();		
	}
}
