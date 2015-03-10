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
	private Hashtable<Tag, Integer> tagInstances;
	
	public WordTagStatsTable(){
		table = new Hashtable<String, WordStats>();
		tagInstances = new Hashtable<Tag,Integer>();
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
		incrementTag(tag);
	}
	
	private void incrementTag(Tag tag) {
		Integer tagInstance = tagInstances.get(tag);
		if(tagInstance == null){
			tagInstance = 1;
			tagInstances.put(tag, tagInstance);
		}
		else{
			tagInstances.put(tag, tagInstance + 1);
		}
	}

	public double getTagProbability(Word word, Tag tag){
		WordStats wordStats = table.get(word.getWord());
		if(wordStats != null){
			Integer numTag = tagInstances.get(tag);
			if(numTag!=null){
				double prob = Math.log10(wordStats.tag(tag) / tagInstances.get(tag));
				
				//System.out.println("word - " + word.getWord() + ", tag - " + tag + ", prob - " + prob);
				
				return prob;
			}
		}
		return 1;
	}
}
