package statisticsTables;
import java.io.Serializable;
import java.util.Hashtable;

import parsing.Tag;
import parsing.Word;


public class WordTagStatsTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//this class stores the stats for each word
	private Hashtable<String,WordStats> table;
	
	public WordTagStatsTable(){
		table = new Hashtable<String, WordStats>();
	}
	
	public void addWord(Word word){
		Tag tag = word.getTag();
		String wordString = word.getWord();
		WordStats wordStats = table.get(wordString);
		if(wordStats == null){
			wordStats = new WordStats();
			table.put(wordString, wordStats);
		}
		wordStats.addInstanceOfTag(tag);
	}
	
	public double getTagProbability(Word word, Tag tag){
		return table.get(word).tagProbability(tag);
	}
}
