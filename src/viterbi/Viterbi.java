package viterbi;

import java.util.LinkedList;
import java.util.List;

import parsing.Word;
import parsing.Tag;
import statisticsTables.TagSequenceStatsTable;
import statisticsTables.WordTagStatsTable;

public class Viterbi {
	private TagSequenceStatsTable tssTable;
	private WordTagStatsTable wtsTable;
	
	public List<Word> tagSentence(List<Word> sentence){
		List<Word> result = new LinkedList<Word>();
		Tag[] tags = Tag.values();
		int numTags = tags.length;
		double[][] score = new double[numTags][sentence.size()];
		int[][] backpointer = new int[numTags][sentence.size()];
		for(int i = 0; i < numTags; i++){
			double startTagProb = tssTable.getSequenceProbability(Tag.START, tags[i]);
			double wordTagProb = wtsTable.getTagProbability(sentence.get(0), tags[i]);
			score[i][0] = startTagProb * wordTagProb;
		}		
		for(int j = 1; j < sentence.size(); j++){
			Word word = sentence.get(j);
			for(int i = 0; i < numTags; i++){
				for(int k = 0; k < numTags; k++){
					
				}
			}
		}
		return result;
	}
}
