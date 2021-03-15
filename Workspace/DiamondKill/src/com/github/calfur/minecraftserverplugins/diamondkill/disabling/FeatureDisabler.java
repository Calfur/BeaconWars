package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

import com.github.calfur.minecraftserverplugins.diamondkill.Main;

public class FeatureDisabler {
	public FeatureDisabler() {
		CraftingRecipeDisabler.removeDisabledCraftingRecipes();
		registrateEvents();
	}
	private void registrateEvents() {
		Main plugin = Main.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new EnchantingDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new NaturalDiamondDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new AnvilDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new VillagerTradesDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PotionDisabler(), plugin);
	}
}
