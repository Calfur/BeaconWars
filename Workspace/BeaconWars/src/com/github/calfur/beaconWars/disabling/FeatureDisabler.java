package com.github.calfur.beaconWars.disabling;

import com.github.calfur.beaconWars.Main;

public class FeatureDisabler {
	public FeatureDisabler() {
		CraftingRecipeDisabler.removeDisabledCraftingRecipes();
		registrateEvents();
	}
	private void registrateEvents() {
		Main plugin = Main.getInstance();
		plugin.getServer().getPluginManager().registerEvents(new EnchantingDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new DiamondOreDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new ChestLootNerf(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new AnvilDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new VillagerTradesDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new PotionDisabler(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new TotemNerf(), plugin);
		plugin.getServer().getPluginManager().registerEvents(new FishingNerf(), plugin);
		}
}
