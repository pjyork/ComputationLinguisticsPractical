package naiveBayes;


import java.util.List;

import main.Tagger;
import parsing.Tag;
import parsing.Word;
import statisticsTables.Statistics;

public class NaiveBayes implements Tagger {
	
	private double tagProbability(Tag tag, String word, Statistics stats){		
		return stats.getNaiveBayesTagProbability(word, tag);
	}

	@Override
	public Tag[] tagSentence(List<Word> sentence, Statistics stats) {
		Tag[] tags = Tag.values();
		Tag[] result = new Tag[sentence.size()];
		for(int i = 0; i < sentence.size(); i++){
			double maxProb = Double.NEGATIVE_INFINITY;
			String word = sentence.get(i).getWord();
			for(int j = 0; j < tags.length; j++){
				double tagProb = tagProbability(tags[j], word, stats);
				if(tagProb > maxProb){
					maxProb = tagProb;
					result[i] = tags[j];
				}
				
			}
		}
		return result;
	}
	
}
