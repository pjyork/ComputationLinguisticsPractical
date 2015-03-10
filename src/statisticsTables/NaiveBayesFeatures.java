package statisticsTables;

import java.util.Hashtable;

import parsing.Tag;
import parsing.Word;

public class NaiveBayesFeatures {
	private Hashtable<Tag, Integer> numericTagInstances;
	private Hashtable<Tag, Integer> capitalisedTagInstances;
	private Hashtable<Tag, Integer> allCapsTagInstances;
	private Hashtable<Tag, Integer> tagInstances;
	
	public NaiveBayesFeatures(){
		this.initialise();
	}
	
	public void initialise() {
		 this.numericTagInstances = new Hashtable<Tag, Integer>();
		 this.capitalisedTagInstances = new Hashtable<Tag, Integer>();		
		 this.allCapsTagInstances = new Hashtable<Tag, Integer>();	
		 this.tagInstances = new Hashtable<Tag, Integer>();	
	}
	
	public void add(Word word){
		Tag tag = word.getTag();
		increment(tagInstances, tag);
		String string = word.getWord();
		if(isNumeric(string)){
			increment(numericTagInstances, tag);
		}
		if(isCapitalised(string)){
			increment(capitalisedTagInstances, tag);
		}
		if(isAllCaps(string)){
			increment(allCapsTagInstances, tag);
		}
		
	}

	private boolean isAllCaps(String string) {
		boolean isAllCaps = true;
		int pos = 0;
		while(isAllCaps && pos < string.length()){
			isAllCaps = Character.isUpperCase(string.charAt(pos));
		}
		return isAllCaps;
	}

	private boolean isCapitalised(String string) {
		return Character.isUpperCase(string.charAt(0)) && !isAllCaps(string); 
	}

	private boolean isNumeric(String string) {
		int pos = 0;
		while(pos < string.length()){
			if(Character.isDigit(string.charAt(pos))){
				return true;
			}
		}
		return false;
	}

	private void increment(Hashtable<Tag, Integer> hashTable, Tag tag) {
		Integer instances = hashTable.get(tag);
		if(instances == null){
			instances = 0;
		}
		instances += 1;
		hashTable.put(tag, instances);		
	}

	public double numericTagProbability(Tag tag, boolean isNumeric){		
		Integer numericInst = numericTagInstances.get(tag);
		return getProbability(numericInst, isNumeric, tag);		
	}
	
	private double getProbability(Integer instances, boolean needToInvert, Tag tag) {
		if(instances == null){
			instances = 1;
		}
		int tagInstance = tagInstances.get(tag);
		double prob = instances / tagInstance;
		if(!needToInvert){
			prob = 1 - prob;
		}
		return prob;
	}

	public double capitalisedTagProbability(Tag tag, boolean isCapitalised){
		Integer capitalisedInst = capitalisedTagInstances.get(tag);
		return getProbability(capitalisedInst, isCapitalised, tag);
	}
	
	public double allCapsTagProbability(Tag tag, boolean isAllCaps){
		Integer allCapsInst = allCapsTagInstances.get(tag);
		return getProbability(allCapsInst, isAllCaps, tag);
	}
}
