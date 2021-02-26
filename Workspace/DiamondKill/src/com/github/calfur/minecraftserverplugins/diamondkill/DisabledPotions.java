package com.github.calfur.minecraftserverplugins.diamondkill;

import org.bukkit.Material;
import org.bukkit.potion.PotionType;

public class DisabledPotions {
	private Material ingerdient;
	private PotionType potionType;

	public PotionType getPotionType() {
		return potionType;
	}

	public Material getIngerdient() {
		return ingerdient;
	}

	public DisabledPotions(Material ingerdient, PotionType potionType) {
		this.ingerdient = ingerdient;
		this.potionType = potionType;
	}

}
