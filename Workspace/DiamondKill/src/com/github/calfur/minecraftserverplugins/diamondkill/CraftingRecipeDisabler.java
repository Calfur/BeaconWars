package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

public class CraftingRecipeDisabler {
	private static Material[] disabledRecipes = { Material.ANVIL, Material.BEACON, Material.FIREWORK_ROCKET, Material.NETHERITE_BLOCK, Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_BOOTS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_HELMET};
	public static void removeDisabledCraftingRecipes() {
		for(Material disabledRecipe : disabledRecipes){
			removeCraftingRecipe(disabledRecipe);
		}
	}
	private static void removeCraftingRecipe(Material material) {
		Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext())
        {
            recipe = it.next();
            if (recipe != null && recipe.getResult().getType() == material)
            {
                it.remove();
            }
        }
	}
}