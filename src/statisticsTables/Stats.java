package statisticsTables;

import java.util.Hashtable;
import java.util.List;

import parsing.Tag;
import parsing.Word;

public class Stats implements Statistics {
	private Hashtable <String, Hashtable<Tag,Integer>> wordTags;
	private Hashtable <Tag, Hashtable<Tag,Integer>> tagSequences;
	private Hashtable <Tag, Integer> tagInstances;
	private NaiveBayesFeatures naiveBayesFeatures;
	
	public Stats(){
		this.initialise();	
	}
	
	public void initialise() {
		//reset this object 
		this.wordTags = new Hashtable <String, Hashtable<Tag,Integer>>();
		this.tagSequences = new Hashtable <Tag, Hashtable<Tag,Integer>>();	
		this.tagInstances = new Hashtable <Tag, Integer>();
		this.naiveBayesFeatures = new NaiveBayesFeatures();
	}
	
	public void updateStats(List<List<Word>> sentences){
		Word prevWord = null; 
		Word currentWord = null;
		for(List<Word> sentence : sentences){
			for(Word word : sentence){
				incrementEntry(tagInstances, word.getTag());
				Word tempWord = currentWord;
				currentWord = word;
				if(prevWord != null){
					addTagSequence(prevWord.getTag(), word.getTag());
				}
				prevWord = tempWord;
				addWord(word);
				
				naiveBayesFeatures.add(word);
			}
		}		
	}
	
	private void addWord(Word word) {
		Hashtable<Tag, Integer> wordTable = wordTags.get(word.getWord());
		
		if(wordTable == null){
			wordTable = new Hashtable<Tag, Integer>();
		}
		incrementEntry(wordTable, word.getTag());
	}

	private void addTagSequence(Tag tag1, Tag tag2) {
		Hashtable<Tag, Integer> tag1Table = tagSequences.get(tag1);
		if(tag1Table == null){
			tag1Table = new Hashtable<Tag, Integer>();
		}
		incrementEntry(tag1Table, tag2);
	}

	private void incrementEntry(Hashtable<Tag,Integer> table, Tag tag){
		Integer res = table.get(tag);
		if(res == null){
			res = 0;
		}
		res += 1;
		table.put(tag, res);
	}
	
	public double getSequenceProbability(Tag tag1, Tag tag2){
		Hashtable <Tag, Integer> table = tagSequences.get(tag1);
		if(table != null){
			Integer t2Instances = table.get(tag2);
			Integer t1Instances = tagInstances.get(tag1);
			if(t1Instances != null && t2Instances !=null){
				double prob = (double) t1Instances / (double) t2Instances;
				return Math.log10(prob);
			}
			else{
				double prob = (double) 1/ (double) Tag.values().length;
				return Math.log10(prob);
			}
		}
		else{
			double prob =(double) 1/ (double) Tag.values().length;
			return Math.log10(prob);
		}
	}
	
	public double getTagProbability(Word word, Tag tag){
		Hashtable <Tag, Integer> wordStats = wordTags.get(word.getWord());
		Integer count = 0;
		Integer numTag = 0;
		double prob = 0.0;
		if(wordStats != null){
			numTag = tagInstances.get(tag);
			if(numTag!=null){
				prob = (double) wordStats.get(tag) / (double) numTag;
				//System.out.println("word - " + word.getWord() + ", tag - " + tag + ", prob - " + prob);
				return Math.log10(prob);
			}
			numTag = 1;
			System.out.println("nulltag -  " + tag);
			
		}
		prob = (double) (count + 1) / (double) (numTag + wordTags.keySet().size());
		return Math.log10(prob);
	}
	
	public double getNaiveBayesTagProbability(String word, Tag tag){
		//get the naive bayes probability, given the string of the word
		return Math.log10(naiveBayesFeatures.getTagProbability(word, tag)) + getTagProbability(new Word(word, Tag.CC),tag);
	}
}
