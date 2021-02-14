package com.github.calfur.minecraftserverplugins.diamondkill;

public class TopKiller {
	private String name;
	private int diamondValue;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDiamondValue() {
		return diamondValue;
	}
	public void setDiamondValue(int diamondValue) {
		this.diamondValue = diamondValue;
	}
	
	public TopKiller(String name, int diamondValue) {
		this.name = name;
		this.diamondValue = diamondValue;
	}
}
