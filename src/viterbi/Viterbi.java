package viterbi;

import java.util.List;

import parsing.Word;
import parsing.Tag;
import statisticsTables.StatisticsCompiler;

public class Viterbi {
	private StatisticsCompiler stats;
	
	public Viterbi(StatisticsCompiler stats){
		this.stats = stats;
	}
	
	public Tag[] tagSentence(List<Word> sentence){
		int n = sentence.size();
		Tag[] result;
		result = new Tag[n];
		Tag[] tags = Tag.values();
		int numTags = tags.length;
		double[][] score = new double[numTags][n];
		int[][] backpointer = new int[numTags][n];
		
		for(int i = 0; i < numTags; i++){
			double startTagProb = stats.getSequenceProbability(Tag.START, tags[i]);
			double wordTagProb = stats.getTagProbability(sentence.get(0), tags[i]);
			score[i][0] = startTagProb + wordTagProb;
		}		
		
		for(int j = 1; j < n; j++){
			Word word = sentence.get(j);
			for(int i = 0; i < numTags; i++){
				double wordTagProb = stats.getTagProbability(word, tags[i]);
				for(int k = 0; k < numTags; k++){
					double seqTagProb = stats.getSequenceProbability(tags[k], tags[i]);
					double prob = score[k][j-1] + wordTagProb + seqTagProb;
					if(prob > score[i][j]){
						score[i][j] = prob;
						backpointer[i][j] = k;
					}
				}
			}
		}
		
		double bestScore = 0;
		int backtrackTag = 0;
		for(int i = 0; i < numTags; i++){
			double currentScore = score[i][n - 1];
			if(currentScore > bestScore){
				bestScore = currentScore;
				backtrackTag = i;
			}
		}
		int posInSentence = n - 1;
		while(posInSentence > 0){
			result[posInSentence] = tags[backtrackTag];
			backtrackTag = backpointer[backtrackTag][posInSentence];
			posInSentence -= 1;
		}
		result[0] = tags[backtrackTag];
		return result;
	}
}
