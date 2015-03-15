package viterbi;

import java.util.List;

import main.Tagger;
import parsing.Word;
import parsing.Tag;
import statisticsTables.Statistics;

public class Viterbi implements Tagger {
	
	
	public Tag[] tagSentence(List<Word> sentence, Statistics stats){
		int n = sentence.size();
		Tag[] result;
		result = new Tag[n]; 
		Tag[] tags = Tag.values();
		int numTags = tags.length;
		result[0] = Tag.START;
		double[][] score = new double[numTags][n];
		int[][] backpointer = new int[numTags][n];
		for(int i = 1; i < sentence.size(); i++){
			//the chance of an actual word in the sentence having the tag START is 0
			score[0][i] = Double.NEGATIVE_INFINITY;			
		}
		for(int i = 1; i < numTags; i++){
			//what is the the probability, for a given tag and word, that it is preceded by the tag START
			// i.e. that it is at the beginning of the actual sentence
			double startTagProb = stats.getSequenceProbability(Tag.START, tags[i]);
			double wordTagProb = stats.getTagProbability(sentence.get(1).getWord(), tags[i]);
			score[i][1] = startTagProb + wordTagProb;
		}		
		
		for(int j = 2; j < n; j++){
			//for each word in thte sentence
			Word word = sentence.get(j);
			for(int i = 1; i < numTags; i++){
				//for each potential tag it could be
				score[i][j] = Double.NEGATIVE_INFINITY;
				double wordTagProb = stats.getTagProbability(word.getWord(), tags[i]);
				
				for(int k = 1; k < numTags; k++){
					//for each of the possiblities for preceding tags
					double seqTagProb = stats.getSequenceProbability(tags[k], tags[i]);
					double prob = score[k][j-1] + wordTagProb + seqTagProb;
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
			//find the most likely tag for the final word in the sentence
			double currentScore = score[i][n - 1];
			if(currentScore > bestScore){
				bestScore = currentScore;
				backtrackTag = i;
			}
		}
		int posInSentence = n - 1;
		while(posInSentence > 0){
			//backtrack through the most likely tags for each word
			result[posInSentence] = tags[backtrackTag];
			backtrackTag = backpointer[backtrackTag][posInSentence];
			posInSentence -= 1;
		}
		result[0] = tags[backtrackTag];
		return result;
	}
}
