package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import org.bukkit.ChatColor;

public class StringFormatter {
	public static String firstLetterToUpper(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
	}
	public static String repeatString(String string, int repetitions) {
		String result = "";
		for (int i = 0; i < repetitions; i++) {
			result += string;
		}
		return result;
	}
	public static String displaySecondsAsTime(long allSeconds) {
		long seconds = allSeconds % 60;
		long minutes = Math.floorDiv(allSeconds, 60);
		long hours = Math.floorDiv(minutes, 60);
		minutes = minutes % 60;
		String result = "";
		if(hours > 0) {
			result += bold("" + hours);
			if(hours == 1) {
				 result += "Stunde ";
			}else {
				 result += "Stunden ";
			}
		}
		if(minutes > 0) {
			result += bold("" + minutes);
			if(minutes == 1) {
				 result += "Minute und ";
			}else {
				 result += "Minuten und ";
			}
		}
		result += bold("" + seconds);
		if(seconds == 1) {
			 result += "Sekunde";
		}else {
			 result += "Sekunden";
		}
		return result;
	}
	
	public static String uncoloredDiamondWord(int amount) {
		return amount + singularOrPlural(amount, " Diamant", " Diamanten");
	}
	
	public static String singularOrPlural(int amount, String singular, String plural) {
		if(amount == 1 || amount == -1) {
			return singular;
		}else {
			return plural;
		}
	}
	
	public static String diamondWord(int amount) {		
		return ChatColor.AQUA + uncoloredDiamondWord(amount) + ChatColor.RESET;
	}
	
	public static String bold(String text) {
		return ChatColor.BOLD + text + ChatColor.RESET;
	}
	
	public static String error(String text) {
		return ChatColor.DARK_RED + text + ChatColor.RESET;
	}
}
