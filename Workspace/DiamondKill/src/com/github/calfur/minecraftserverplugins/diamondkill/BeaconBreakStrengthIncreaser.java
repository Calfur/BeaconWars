package com.github.calfur.minecraftserverplugins.diamondkill;

import net.minecraft.server.v1_16_R3.BlockBase;
import net.minecraft.server.v1_16_R3.Blocks;

public class BeaconBreakStrengthIncreaser {

	public static void increaseBreakStrength() {
		try {
			java.lang.reflect.Field field = BlockBase.BlockData.class.getDeclaredField("strength");
			field.setAccessible(true);
			field.setFloat(Blocks.BEACON.getBlockData(), 15.0F); // 5 times stronger
		 } catch (Exception ex) {
			 ex.printStackTrace();
		 }
	}

}
