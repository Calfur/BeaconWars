package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.ChatColor;

public class Team {
	private ChatColor color; 
	private int number;
	public Team(ChatColor color, int number) {
		this.color = color;
		this.number = number;
	}
	public ChatColor getColor() {
		return color;
	}
	public void setColor(ChatColor color) {
		this.color = color;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
}
