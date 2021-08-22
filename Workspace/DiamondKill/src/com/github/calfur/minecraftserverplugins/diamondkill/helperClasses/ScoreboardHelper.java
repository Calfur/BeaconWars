package com.github.calfur.minecraftserverplugins.diamondkill.helperClasses;

import java.util.List;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class ScoreboardHelper {

	/***
	 * 
	 * @param objective
	 * @param scoreTexts
	 * 
	 * Use empty strings like this "" to add empty rows. Do not name two scoreTexts same except from this. 
	 */
	public static void addScoreRows(Objective objective, List<String> scoreTexts) {
		int amountOfScores = scoreTexts.size();
		for (int i = 0; i < amountOfScores; i++) {
			int scoreNumber = amountOfScores - i;
			String scoreText = scoreTexts.get(i);
			
			if(scoreText.equals("")) {
				addEmptyScoreRow(objective, scoreNumber);
			}else {
				addScoreRow(objective, scoreNumber, scoreText);
			}
		}
	}
	
	private static void addEmptyScoreRow(Objective objective, int scoreNumber) {
		addScoreRow(objective, scoreNumber, getEmptyScoreName(scoreNumber));
	}
	
	private static void addScoreRow(Objective objective, int scoreNumber, String scoreText) {
		Score score = objective.getScore(scoreText);
		score.setScore(scoreNumber);
	}
	
	private static String getEmptyScoreName(int score) {
		return StringFormatter.repeatString(" ", score);
	}
}
