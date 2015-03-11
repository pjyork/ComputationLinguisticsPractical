package statisticsTables;

import java.util.List;

import parsing.Tag;
import parsing.Word;

public interface Statistics {
	public void updateStats(List<List<Word>> sentences);
	public void initialise();
	public double getSequenceProbability(Tag tag1, Tag tag2);
	public double getTagProbability(Word word, Tag tag) ;
	public double getNaiveBayesTagProbability(String word, Tag tag);
}
