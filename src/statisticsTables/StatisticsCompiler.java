package statisticsTables;
import java.util.List;


import parsing.Word;


public class StatisticsCompiler {
	//takes a list of tagged sentences and creates or updates existing tables appropriately
	private TagSequenceStatsTable tagSequenceStatsTable;
	private WordTagStatsTable wordTagStatsTable;
	
	public void updateStats(List<List<Word>> sentences){
		Word prevWord = null; 
		Word currentWord = null;
		for(List<Word> sentence : sentences){
			for(Word word : sentence){
				Word tempWord = currentWord;
				currentWord = word;
				if(prevWord != null){
					tagSequenceStatsTable.addSeq(prevWord.getTag(),word.getTag());
				}
				prevWord = tempWord;
				wordTagStatsTable.addWord(word);
			}
		}		
	}
}
