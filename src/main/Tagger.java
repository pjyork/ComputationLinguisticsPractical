package main;
import java.util.List;

import parsing.Tag;
import parsing.Word;
import statisticsTables.StatisticsCompiler;


public interface Tagger {
	public Tag[] tagSentence(List<Word> sentence, StatisticsCompiler stats);
}
