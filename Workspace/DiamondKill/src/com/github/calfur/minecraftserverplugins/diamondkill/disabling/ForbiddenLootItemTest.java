package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.github.calfur.minecraftserverplugins.diamondkill.disabling.ForbiddenLootItem.EnchantmentLevel;

public class ForbiddenLootItemTest {

	@Test
	void nerfEnchantments_WhenNoEnchantmentShouldBeNerfed_ThenTheSameEnchantmentsAreReturned() {
		//Arrange
		ForbiddenLootItem forbiddenLootItem = new ForbiddenLootItem(
				Material.DIAMOND_CHESTPLATE, 
				new ItemStack(Material.LEATHER_CHESTPLATE), 
				EnchantmentLevel.noEnchantments
		);
		
		Map<Enchantment, Integer> inputEnchantments = new HashMap<Enchantment, Integer>();
		inputEnchantments.put(Enchantment.PROTECTION_FALL, 4);
		//inputEnchantments.put(Enchantment.PROTECTION_EXPLOSIONS, 4);
		//inputEnchantments.put(Enchantment.LUCK, 1);
		//inputEnchantments.put(Enchantment.QUICK_CHARGE, 3);
		
		Material expectedMaterial = Material.LEATHER_CHESTPLATE;
		Map<Enchantment, Integer> expectedEnchantments = new HashMap<Enchantment, Integer>();
		expectedEnchantments.put(Enchantment.PROTECTION_FALL, 4);
		//expectedEnchantments.put(Enchantment.PROTECTION_EXPLOSIONS, 4);
		//expectedEnchantments.put(Enchantment.LUCK, 1);
		//expectedEnchantments.put(Enchantment.QUICK_CHARGE, 3);
		//Act
		ItemStack actualResult = forbiddenLootItem.getSubstituteWithAdjustedEnchantments(inputEnchantments);
		//Assert
		Assert.assertEquals(expectedMaterial, actualResult.getType());
		Map<Enchantment, Integer> actualEnchantments = actualResult.getEnchantments();
		for (Map.Entry<Enchantment, Integer> enchantment : expectedEnchantments.entrySet()) {	
			Assert.assertEquals(enchantment.getValue(), actualEnchantments.get(enchantment.getKey()));
		}
	}
	/*
	@Test
	void nerfEnchantments_WhenAllEnchantmentShouldBeNerfed_ThenTheNerfedEnchantmentsAreReturned() {
		//Arrange
		Map<Enchantment, Integer> input = new HashMap<Enchantment, Integer>();
		input.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		input.put(Enchantment.PROTECTION_PROJECTILE, 5);
		input.put(Enchantment.DAMAGE_ALL, 4);
		Map<Enchantment, Integer> expectedResult = new HashMap<Enchantment, Integer>();
		expectedResult.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		expectedResult.put(Enchantment.PROTECTION_PROJECTILE, 2);
		expectedResult.put(Enchantment.DAMAGE_ALL, 3);
		//Act
		Map<Enchantment, Integer> actualResult = ForbiddenLootItem.nerfEnchantments(input);
		//Assert
		for (Map.Entry<Enchantment, Integer> enchantment : expectedResult.entrySet()) {			
			Assert.assertEquals(enchantment.getValue(), actualResult.get(enchantment.getKey()));
		}
	}
	
	@Test
	void nerfEnchantments_WhenPartOfTheEnchantmentsShouldBeNerfed_ThenPartOfThemGetsNerfedAreReturned() {
		//Arrange
		Map<Enchantment, Integer> input = new HashMap<Enchantment, Integer>();
		input.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		input.put(Enchantment.PROTECTION_PROJECTILE, 5);
		input.put(Enchantment.CHANNELING, 1);
		Map<Enchantment, Integer> expectedResult = new HashMap<Enchantment, Integer>();
		expectedResult.put(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		expectedResult.put(Enchantment.PROTECTION_PROJECTILE, 2);
		expectedResult.put(Enchantment.CHANNELING, 1);
		//Act
		Map<Enchantment, Integer> actualResult = ForbiddenLootItem.nerfEnchantments(input);
		//Assert
		for (Map.Entry<Enchantment, Integer> enchantment : expectedResult.entrySet()) {			
			Assert.assertEquals(enchantment.getValue(), actualResult.get(enchantment.getKey()));
		}
	}*/
}
