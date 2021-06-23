package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import org.bukkit.ChatColor;

public class StringFormatter {
	public static String FirstLetterToUpper(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
	}
	public static String RepeatString(String string, int repetitions) {
		String result = "";
		for (int i = 0; i < repetitions; i++) {
			result += string;
		}
		return result;
	}
	public static String DisplaySecondsAsTime(long allSeconds) {
		long seconds = allSeconds % 60;
		long minutes = Math.floorDiv(allSeconds, 60);
		long hours = Math.floorDiv(minutes, 60);
		minutes = minutes % 60;
		String result = "";
		if(hours > 0) {
			result += Bold("" + hours);
			if(hours == 1) {
				 result += "Stunde ";
			}else {
				 result += "Stunden ";
			}
		}
		if(minutes > 0) {
			result += Bold("" + minutes);
			if(minutes == 1) {
				 result += "Minute und ";
			}else {
				 result += "Minuten und ";
			}
		}
		result += Bold("" + seconds);
		if(seconds == 1) {
			 result += "Sekunde";
		}else {
			 result += "Sekunden";
		}
		return result;
	}
	
	public static String Bold(String text) {
		return ChatColor.BOLD + text + ChatColor.RESET;
	}
}
