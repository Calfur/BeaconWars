package com.github.calfur.minecraftserverplugins.diamondkill.disabling;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ForbiddenLootItemTest {

	@Test
	void nerfEnchantments_WhenNoEnchantmentShouldBeNerfed_ThenTheSameEnchantmentsAreReturned() {
		//Arrange
		Map<Enchantment, Integer> input = new HashMap<Enchantment, Integer>();
		input.put(Enchantment.PROTECTION_FALL, 4);
		input.put(Enchantment.PROTECTION_EXPLOSIONS, 4);
		input.put(Enchantment.LUCK, 1);
		input.put(Enchantment.QUICK_CHARGE, 3);
		Map<Enchantment, Integer> expectedResult = new HashMap<Enchantment, Integer>();
		expectedResult.put(Enchantment.PROTECTION_FALL, 4);
		expectedResult.put(Enchantment.PROTECTION_FALL, 4);
		expectedResult.put(Enchantment.LUCK, 1);
		expectedResult.put(Enchantment.QUICK_CHARGE, 3);
		//Act
		Map<Enchantment, Integer> actualResult = ForbiddenLootItem.nerfEnchantments(input);
		//Assert
		for (Map.Entry<Enchantment, Integer> enchantment : expectedResult.entrySet()) {			
			Assert.assertEquals(enchantment.getValue(), actualResult.get(enchantment.getKey()));
		}
	}
	
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
	}
}
