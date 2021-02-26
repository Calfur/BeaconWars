package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

public class StringEditor {
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
}
