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

}
