package statisticsTables;

import java.util.HashMap;
import java.util.List;

import parsing.Tag;
import parsing.Word;

public class StatisticsCounter implements Statistics{

	//each word w is a key to a list l, such that the list contains all tags that the word has and their count  
	public HashMap<String, HashMap<Tag,Integer>> wordTagCount; 
	//counts the number of instances of each tag
	public HashMap<Tag, Integer> tagCounter;
	//counts the number of instances of a sequence of tag1 followed by tag2
	public HashMap<Tag, HashMap<Tag, Integer>> tagRelationships;
	//stores the features for computing naive bayes classification of a word
	public NaiveBayesFeatures naiveBayesFeatures;
	
	
	public StatisticsCounter() {
		initialise();
	}
	
	@Override
	public void initialise() {
		wordTagCount = new HashMap<String,HashMap<Tag,Integer>>();
		tagCounter = new HashMap<Tag,Integer>();
		tagRelationships = new HashMap<Tag,HashMap<Tag,Integer>>();
		naiveBayesFeatures = new NaiveBayesFeatures();
	}
	
	@Override
	// goes through all the sentences and adds them in the hashtables
	public void updateStats(List<List<Word>> sentences) {
		for (List<Word> sentence:sentences) {
			addInCounter(sentence);
		}
	}
	
	//add to our stats the information given by this sentence
	public void addInCounter(List<Word> sentence) {
		List<Word> words = sentence;
		int size = words.size();
		for (int i=0; i<size; i++) {
			addWordTag(words.get(i));
			addTag(words.get(i).getTag());
			if (i!=size-1) addTagSequence(words.get(i).getTag(), words.get(i+1).getTag());
			naiveBayesFeatures.add(words.get(i));
		}
			
		
	}

	//add to our stats an instance of a word with a given tag
	public void addWordTag(Word word) {
		String sWord = word.getWord();
		Tag tag = word.getTag();
		if (wordTagCount.containsKey(sWord)) {
			HashMap<Tag, Integer> tagList = wordTagCount.get(sWord);
			
			if (tagList.containsKey(tag))
				tagList.replace(tag,tagList.get(tag)+1);
			else
				tagList.put(tag, 1);
		}
		else {
			HashMap<Tag, Integer> tagList = new HashMap<Tag,Integer>();
			tagList.put(tag, 1);
			wordTagCount.put(sWord, tagList);
		}
		
	}
	
	//add to your stats an instance of tag
	public void addTag(Tag tag) {
		if (tagCounter.containsKey(tag))
			 tagCounter.replace(tag, tagCounter.get(tag)+1);
		else
			tagCounter.put(tag, 1);
	}
	
	//add to our stats an instance of tag1 being followed by tag2
	public void addTagSequence(Tag tag1, Tag tag2) {
		if (tagRelationships.containsKey(tag1)) {
			HashMap<Tag, Integer> tagList = tagRelationships.get(tag1);
			if (tagList.containsKey(tag2))
				tagList.replace(tag2,tagList.get(tag2)+1);
			else
				tagList.put(tag2, 1);
		}
		else {
			HashMap<Tag, Integer> tagList = new HashMap<Tag,Integer>();
			tagList.put(tag2, 1);
			tagRelationships.put(tag1, tagList);
		}
			
	}
	
	//get probability that a word is of a specific tag
	@Override
	public double getTagProbability(String word, Tag tag) {
		double count = 0;
		double tagCount = 0;
		HashMap<Tag,Integer> tagList = wordTagCount.get(word);	
		double result = 0;
		if (tagList!=null) {
			if (tagList.containsKey(tag)){
				count = (double) tagList.get(tag);
				tagCount = (double) tagCounter.get(tag);					
			}
		}
		int m = wordTagCount.keySet().size();
		result = (double) (Math.log((count+0.001)/(tagCount+0.001*m))); //smoothing if the word is not in 
		return result;
	}
	
	//get probability that tag2 follows tag1
	@Override
	public double getSequenceProbability(Tag tag1, Tag tag2) {
		double trCount = 0;
		double tagCount = 0;
		HashMap<Tag,Integer> tagList = tagRelationships.get(tag1);
		if (tagList!=null) {
			if (tagList.containsKey(tag2)) {
				trCount = (double) tagList.get(tag2);
				tagCount = (double) tagCounter.get(tag1);	
			}
		}
		return (double) (Math.log((trCount+0.001)/(tagCount+0.001*Tag.values().length))); // smoothing if the word is not in 
	}
	
	//for Naive Bayes, probability of the tag and word 
	@Override
	public double getNaiveBayesTagProbability(String word, Tag tag) {		
		//naive bayes probability is given by the naive bayes probability * the probability for that word itself
		double prob = (double) Math.log(naiveBayesFeatures.getTagProbability(word, tag));
		//use log probabilities to avoid underflow
		prob += getTagProbability(word,tag);
		return prob;
	}
		
}
