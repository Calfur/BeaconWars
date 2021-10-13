package com.github.calfur.beaconWars.helperClasses;

import org.bukkit.ChatColor;

import com.github.calfur.beaconWars.Reward;

public class StringFormatter {
	public final static ChatColor errorColor = ChatColor.DARK_RED;
	
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
		return displaySecondsAsTime(allSeconds, ChatColor.RESET);
	}
	
	public static String displaySecondsAsTime(long allSeconds, ChatColor color) {
		long seconds = allSeconds % 60;
		long minutes = Math.floorDiv(allSeconds, 60);
		long hours = Math.floorDiv(minutes, 60);
		minutes = minutes % 60;
		String result = "";
		if(hours > 0) {
			result += bold("" + hours);
			if(hours == 1) {
				 result += color + " Stunde ";
			}else {
				 result += color + " Stunden ";
			}
		}
		if(minutes > 0) {
			result += bold("" + minutes);
			if(minutes == 1) {
				 result += color + " Minute und ";
			}else {
				 result += color + " Minuten und ";
			}
		}
		result += bold("" + seconds);
		if(seconds == 1) {
			 result += color + " Sekunde";
		}else {
			 result += color + " Sekunden";
		}
		return result;
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
	
	public static String pointWord(int amount) {		
		return amount + singularOrPlural(amount, " Punkt", " Punkte");
	}

	public static String diaWord(int amount) {
		return ChatColor.AQUA + uncoloredDiaWord(amount) + ChatColor.RESET;
	}

	public static String uncoloredDiamondWord(int amount) {
		return amount + singularOrPlural(amount, " Diamant", " Diamanten");
	}
	
	public static String uncoloredDiaWord(int amount) {
		return amount + singularOrPlural(amount, " Dia", " Dias");
	}
	
	public static String teamWord(int amount) {
		return amount + singularOrPlural(amount, " Team", " Teams");
	}
	
	public static String rewardText(Reward reward) {
		return diamondWord(reward.getDiamonds()) + " und " + pointWord(reward.getPoints());
	}
	
	public static String bold(String text) {
		return ChatColor.BOLD + text + ChatColor.RESET;
	}
	
	public static String error(String text) {
		return errorColor + text + ChatColor.RESET;
	}
}
