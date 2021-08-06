package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import org.bukkit.ChatColor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class StringFormatterTest {

	@Test
	void FirstLetterToUpper_WhenFirstLetterIsLowerCaseLetter_ResultIsUpperCase() {
		//Arrange
		String input = "calfur";
		String expectedResult = "Calfur";
		//Act
		String actualResult = StringFormatter.FirstLetterToUpper(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void FirstLetterToUpper_WhenFirstLetterIsSpetialCharacter_ResultIsNoError() {
		//Arrange
		String input = "__XXX__";
		String expectedResult = "__XXX__";
		//Act
		String actualResult = StringFormatter.FirstLetterToUpper(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void RepeatString_WhenInputIsOneLetter_ResultIsAsExpected() {
		//Arrange
		String input = " ";
		int repetitions = 5;
		String expectedResult = "     ";
		//Act
		String actualResult = StringFormatter.RepeatString(input, repetitions);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void RepeatString_WhenInputIsZeroRepetitions_ResultIsEmptyString() {
		//Arrange
		String input = "ab";
		int repetitions = 0;
		String expectedResult = "";
		//Act
		String actualResult = StringFormatter.RepeatString(input, repetitions);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void DisplaySecondsAsTime_WhenInputIsZero_ResultIsAsExpected() {
		//Arrange
		long input = 0;
		String expectedResult = StringFormatter.Bold("0") + "Sekunden";
		//Act
		String actualResult = StringFormatter.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void DisplaySecondsAsTime_WhenInputIsBelowOneMinute_ResultIsAsExpected() {
		//Arrange
		long input = 37;
		String expectedResult = StringFormatter.Bold("37") + "Sekunden";
		//Act
		String actualResult = StringFormatter.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void DisplaySecondsAsTime_WhenInputIsBelowOneHour_ResultIsAsExpected() {
		//Arrange
		long input = 3001;
		String expectedResult = StringFormatter.Bold("50") + "Minuten und " + StringFormatter.Bold("1") + "Sekunde";
		//Act
		String actualResult = StringFormatter.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void DisplaySecondsAsTime_WhenInputIsOverOneHour_ResultIsAsExpected() {
		//Arrange
		long input = 3680;
		String expectedResult = StringFormatter.Bold("1") + "Stunde " + StringFormatter.Bold("1") + "Minute und " + StringFormatter.Bold("20") + "Sekunden";
		//Act
		String actualResult = StringFormatter.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void DisplaySecondsAsTime_WhenInputIsExactlyOneMinute_ResultIsAsExpected() {
		//Arrange
		long input = 60;
		String expectedResult = StringFormatter.Bold("1") + "Minute und " + StringFormatter.Bold("0") + "Sekunden";
		//Act
		String actualResult = StringFormatter.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void Bold_WhenUnformattedStringIsUsed_ResultIsBold() {
		//Arrange
		String input = "unformatted string";
		String expectedResult = ChatColor.BOLD + "unformatted string" + ChatColor.RESET;
		//Act
		String actualResult = StringFormatter.Bold(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
}
