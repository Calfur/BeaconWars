package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class StringEditorTest {

	@Test
	void  FirstLetterToUpper_WhenFirstLetterIsLowerCaseLetter_ResultIsUpperCase() {
		//Arrange
		String input = "calfur";
		String expectedResult = "Calfur";
		//Act
		String actualResult = StringEditor.FirstLetterToUpper(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void  FirstLetterToUpper_WhenFirstLetterIsSpetialCharacter_ResultIsNoError() {
		//Arrange
		String input = "__XXX__";
		String expectedResult = "__XXX__";
		//Act
		String actualResult = StringEditor.FirstLetterToUpper(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void  RepeatString_WhenInputIsOneLetter_ResultIsAsExpected() {
		//Arrange
		String input = " ";
		int repetitions = 5;
		String expectedResult = "     ";
		//Act
		String actualResult = StringEditor.RepeatString(input, repetitions);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void  RepeatString_WhenInputIsZeroRepetitions_ResultIsEmptyString() {
		//Arrange
		String input = "ab";
		int repetitions = 0;
		String expectedResult = "";
		//Act
		String actualResult = StringEditor.RepeatString(input, repetitions);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void  DisplaySecondsAsTime_WhenInputIsZero_ResultIsAsExpected() {
		//Arrange
		long input = 0;
		String expectedResult = StringEditor.Bold("0") + "Sekunden";
		//Act
		String actualResult = StringEditor.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void  DisplaySecondsAsTime_WhenInputIsBelowOneMinute_ResultIsAsExpected() {
		//Arrange
		long input = 37;
		String expectedResult = StringEditor.Bold("37") + "Sekunden";
		//Act
		String actualResult = StringEditor.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void  DisplaySecondsAsTime_WhenInputIsBelowOneHour_ResultIsAsExpected() {
		//Arrange
		long input = 3001;
		String expectedResult = StringEditor.Bold("50") + "Minuten und " + StringEditor.Bold("1") + "Sekunde";
		//Act
		String actualResult = StringEditor.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}

	@Test
	void  DisplaySecondsAsTime_WhenInputIsOverOneHour_ResultIsAsExpected() {
		//Arrange
		long input = 3680;
		String expectedResult = StringEditor.Bold("1") + "Stunde " + StringEditor.Bold("1") + "Minute und " + StringEditor.Bold("20") + "Sekunden";
		//Act
		String actualResult = StringEditor.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	void  DisplaySecondsAsTime_WhenInputIsExactlyOneMinute_ResultIsAsExpected() {
		//Arrange
		long input = 60;
		String expectedResult = StringEditor.Bold("1") + "Minute und " + StringEditor.Bold("0") + "Sekunden";
		//Act
		String actualResult = StringEditor.DisplaySecondsAsTime(input);
		//Assert
		Assert.assertEquals(expectedResult, actualResult);
	}
}
