package viterbi;

import java.util.List;

import main.Tagger;
import parsing.Word;
import parsing.Tag;
import statisticsTables.Statistics;
import statisticsTables.StatisticsCompiler;

public class Viterbi implements Tagger {
	
	
	public Tag[] tagSentence(List<Word> sentence, Statistics stats){
		int n = sentence.size();
		Tag[] result;
		result = new Tag[n];
		Tag[] tags = Tag.values();
		int numTags = tags.length;
		double[][] score = new double[numTags][n];
		int[][] backpointer = new int[numTags][n];
		for(int i = 1; i < sentence.size(); i++){
			score[0][i] = Double.NEGATIVE_INFINITY;			
		}
		for(int i = 1; i < numTags; i++){
			double startTagProb = stats.getSequenceProbability(Tag.START, tags[i]);
			double wordTagProb = stats.getTagProbability(sentence.get(0), tags[i]);
			//]System.out.println(startTagProb + " " + wordTagProb);
			score[i][1] = startTagProb + wordTagProb;
			//System.out.println("start - " + score[i][1]);
		}		
		
		for(int j = 2; j < n; j++){
			Word word = sentence.get(j);
			for(int i = 1; i < numTags; i++){
				score[i][j] = Double.NEGATIVE_INFINITY;
				double wordTagProb = stats.getTagProbability(word, tags[i]);
				
				for(int k = 1; k < numTags; k++){
					double seqTagProb = stats.getSequenceProbability(tags[k], tags[i]);
					//System.out.println(" probs " + wordTagProb + " - " + seqTagProb);
					double prob = score[k][j-1] + wordTagProb + seqTagProb;
//					System.out.println("prob - " + prob + "  score  - " + score[i][j]);
					double prevScore = score[i][j];
					if(prob > prevScore){
						score[i][j] = prob;
						backpointer[i][j] = k;
					}
				}
			}
		}
		
		double bestScore = Double.NEGATIVE_INFINITY;
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
