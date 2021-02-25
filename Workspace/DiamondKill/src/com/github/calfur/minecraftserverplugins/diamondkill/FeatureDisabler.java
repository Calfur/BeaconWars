package com.github.calfur.minecraftserverplugins.diamondkill;

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
		plugin.getServer().getPluginManager().registerEvents(new ItemDropsDisabler(), plugin);
	}
}
