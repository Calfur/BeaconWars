package com.github.calfur.beaconWars.helperClasses;

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
		String actualResult = StringFormatter.firstLetterToUpper(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void FirstLetterToUpper_WhenFirstLetterIsSpetialCharacter_ResultIsNoError() {
		//Arrange
		String input = "__XXX__";
		String expectedResult = "__XXX__";
		//Act
		String actualResult = StringFormatter.firstLetterToUpper(input);
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
		String actualResult = StringFormatter.repeatString(input, repetitions);
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
		String actualResult = StringFormatter.repeatString(input, repetitions);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void DisplaySecondsAsTime_WhenInputIsZero_ResultIsAsExpected() {
		//Arrange
		long input = 0;
		String expectedResult = StringFormatter.bold("0") + "Sekunden";
		//Act
		String actualResult = StringFormatter.displaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void DisplaySecondsAsTime_WhenInputIsBelowOneMinute_ResultIsAsExpected() {
		//Arrange
		long input = 37;
		String expectedResult = StringFormatter.bold("37") + "Sekunden";
		//Act
		String actualResult = StringFormatter.displaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void DisplaySecondsAsTime_WhenInputIsBelowOneHour_ResultIsAsExpected() {
		//Arrange
		long input = 3001;
		String expectedResult = StringFormatter.bold("50") + "Minuten und " + StringFormatter.bold("1") + "Sekunde";
		//Act
		String actualResult = StringFormatter.displaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void DisplaySecondsAsTime_WhenInputIsOverOneHour_ResultIsAsExpected() {
		//Arrange
		long input = 3680;
		String expectedResult = StringFormatter.bold("1") + "Stunde " + StringFormatter.bold("1") + "Minute und " + StringFormatter.bold("20") + "Sekunden";
		//Act
		String actualResult = StringFormatter.displaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void DisplaySecondsAsTime_WhenInputIsExactlyOneMinute_ResultIsAsExpected() {
		//Arrange
		long input = 60;
		String expectedResult = StringFormatter.bold("1") + "Minute und " + StringFormatter.bold("0") + "Sekunden";
		//Act
		String actualResult = StringFormatter.displaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void Bold_WhenUnformattedStringIsUsed_ResultIsBold() {
		//Arrange
		String input = "unformatted string";
		String expectedResult = ChatColor.BOLD + "unformatted string" + ChatColor.RESET;
		//Act
		String actualResult = StringFormatter.bold(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void uncoleredDiamondWord_WhenAmountIsOne_WordIsSingular() {
		//Arrange
		int input = 1;
		String expectedResult = "1 Diamant";
		//Act
		String actualResult = StringFormatter.uncoloredDiamondWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void uncoleredDiamondWord_WhenAmountIsThree_WordIsPlural() {
		//Arrange
		int input = 3;
		String expectedResult = "3 Diamanten";
		//Act
		String actualResult = StringFormatter.uncoloredDiamondWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void diamondWord_WhenAmountIsOne_WordIsSingular() {
		//Arrange
		int input = 1;
		String expectedResult = ChatColor.AQUA + "1 Diamant" + ChatColor.RESET;
		//Act
		String actualResult = StringFormatter.diamondWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void diamondWord_WhenAmountIsThree_WordIsPlural() {
		//Arrange
		int input = 3;
		String expectedResult = ChatColor.AQUA + "3 Diamanten" + ChatColor.RESET;
		//Act
		String actualResult = StringFormatter.diamondWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void diamondWord_WhenAmountIsMinusOne_WordIsSingular() {
		//Arrange
		int input = -1;
		String expectedResult = ChatColor.AQUA + "-1 Diamant" + ChatColor.RESET;
		//Act
		String actualResult = StringFormatter.diamondWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void diamondWord_WhenAmountIsMinusThree_WordIsSingular() {
		//Arrange
		int input = -3;
		String expectedResult = ChatColor.AQUA + "-3 Diamanten" + ChatColor.RESET;
		//Act
		String actualResult = StringFormatter.diamondWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void singularOrPlural_WhenAmountIsOne_WordIsSingular() {
		//Arrange
		int input = 1;
		String singular = "Singular";
		String plural = "Plural";
		String expectedResult = "Singular";
		//Act
		String actualResult = StringFormatter.singularOrPlural(input, singular, plural);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void singularOrPlural_WhenAmountIsThree_WordIsPlural() {
		//Arrange
		int input = 3;
		String singular = "Singular";
		String plural = "Plural";
		String expectedResult = "Plural";
		//Act
		String actualResult = StringFormatter.singularOrPlural(input, singular, plural);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void uncoleredShortDiamondWord_WhenAmountIsOne_WordIsSingular() {
		//Arrange
		int input = 1;
		String expectedResult = "1 Dia";
		//Act
		String actualResult = StringFormatter.uncoloredDiaWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void uncoleredShortDiamondWord_WhenAmountIsThree_WordIsPlural() {
		//Arrange
		int input = 3;
		String expectedResult = "3 Dias";
		//Act
		String actualResult = StringFormatter.uncoloredDiaWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void uncoleredShortDiamondWord_WhenAmountIsZero_WordIsPlural() {
		//Arrange
		int input = 0;
		String expectedResult = "0 Dias";
		//Act
		String actualResult = StringFormatter.uncoloredDiaWord(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
}
