package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.ChatColor;

public class Team {
	private ChatColor color; 
	private int id;
	public Team(int id, ChatColor color) {
		this.color = color;
		this.id = id;
	}
	public ChatColor getColor() {
		return color;
	}
	public void setColor(ChatColor color) {
		this.color = color;
	}
	public int getId() {
		return id;
	}
	public void setNumber(int number) {
		this.id = number;
	}
}
