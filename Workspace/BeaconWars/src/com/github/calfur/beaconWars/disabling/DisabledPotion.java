package com.github.calfur.beaconWars.disabling;

import org.bukkit.Material;
import org.bukkit.potion.PotionType;

public class DisabledPotion {
	private Material ingerdient;
	private PotionType potionType;

	public PotionType getPotionType() {
		return potionType;
	}

	public Material getIngerdient() {
		return ingerdient;
	}

	public DisabledPotion(Material ingerdient, PotionType potionType) {
		this.ingerdient = ingerdient;
		this.potionType = potionType;
	}
}
