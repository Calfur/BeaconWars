package com.github.calfur.minecraftserverplugins.diamondkill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class NaturalDiamondDisabler implements Listener {
	
	private Color diamondColor = Color.fromRGB(124, 255, 243); //8191987 / #7cfff3
	private List<ForbiddenItem> forbiddenLoot = Arrays.asList(
			new ForbiddenItem(Material.DIAMOND, new ItemStack(Material.LIGHT_BLUE_DYE)), 
			new ForbiddenItem(Material.DIAMOND_BOOTS, colorizeLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), diamondColor)),
			new ForbiddenItem(Material.DIAMOND_LEGGINGS, colorizeLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), diamondColor)),
			new ForbiddenItem(Material.DIAMOND_CHESTPLATE, colorizeLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), diamondColor)),
			new ForbiddenItem(Material.DIAMOND_HELMET, colorizeLeatherArmor(new ItemStack(Material.LEATHER_HELMET), diamondColor)),
			new ForbiddenItem(Material.DIAMOND_SWORD, new ItemStack(Material.WOODEN_SWORD)),
			new ForbiddenItem(Material.IRON_SWORD, new ItemStack(Material.IRON_SWORD))
	);

	public NaturalDiamondDisabler() {	

		
	}
	
	private ItemStack colorizeLeatherArmor(ItemStack itemStack, Color color) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		if(itemMeta instanceof LeatherArmorMeta) {			
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
			leatherArmorMeta.setColor(color);
			itemStack.setItemMeta(leatherArmorMeta);
		}
		return itemStack;
	}
	
	@EventHandler
	public void onDiamondOreBreaks(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(block.getType() == Material.DIAMOND_ORE) {			
			//event.getBlock().getLocation().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.STONE, 1));
			event.setDropItems(false);
		}
	}	
	@EventHandler
	public void onDiamondOreExplodes(BlockExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(block.getType() == Material.DIAMOND_ORE) {
				Bukkit.broadcastMessage("Jemand versuchte Diamanten zu sprengen. Nice Try!");
				event.setCancelled(true);
				return;
			}
		}
	}	
	@EventHandler
	public void onDiamondOreExplodes(EntityExplodeEvent event) {
		List<Block> blocks = event.blockList();
		for(Block block : blocks){
			if(block.getType() == Material.DIAMOND_ORE) {
				Bukkit.broadcastMessage("Jemand versuchte Diamanten zu sprengen. Nice Try!");
				event.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler
	public void onDiamondLootGenerate(LootGenerateEvent event) {
		List<ItemStack> loot = event.getLoot();
		ArrayList<ItemStack> itemsToRemove = new ArrayList<>();
		ArrayList<ItemStack> itemsToAdd = new ArrayList<>();
		for(ItemStack itemStack : loot){
			for(ForbiddenItem forbiddenItem : forbiddenLoot) {
				if(itemStack.getType() == forbiddenItem.getItem()) {
					ItemStack substitute = forbiddenItem.getSubstitute();
					if(forbiddenItem.isCopyEnchantments()) {						
						Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
						substitute.addEnchantments(enchantments);
					}
					itemsToRemove.add(itemStack);
					itemsToAdd.add(substitute);
					break;
				}
			}
		}
		loot.removeAll(itemsToRemove);
		loot.addAll(itemsToAdd);
	}
}
