package statisticsTables;

import java.util.Hashtable;

import parsing.Tag;
import parsing.Word;
//the boolean features required for the naive bayes algorithm


public class NaiveBayesFeatures {
	//count of  words which contain a numeral and are of the given tag
	private Hashtable<Tag, Integer> numericTagInstances;
	
	//count of words which contain are capitalised (e.g. "Hello") and are of the given tag
	private Hashtable<Tag, Integer> capitalisedTagInstances;
	
	//count of words which are all caps (e.g. "HELLO") and are of the given tag
	private Hashtable<Tag, Integer> allCapsTagInstances;
	
	//count of words which end in the letter S and are of the given tag
	private Hashtable<Tag, Integer> endsInSTagInstances;

	//count of instances of the given tag, total
	private Hashtable<Tag, Integer> tagInstances;

	//count of instances of the given word
	private Hashtable<String, Integer> wordInstances;
	private Integer wordsTotal = 0;
	
	public NaiveBayesFeatures(){
		this.initialise();
	}
	
	public void initialise() {
		//reset the object
		 this.numericTagInstances = new Hashtable<Tag, Integer>();
		 this.capitalisedTagInstances = new Hashtable<Tag, Integer>();		
		 this.allCapsTagInstances = new Hashtable<Tag, Integer>();	
		 this.endsInSTagInstances = new Hashtable<Tag, Integer>();
		 this.tagInstances = new Hashtable<Tag, Integer>();	
		 this.wordInstances = new Hashtable<String, Integer>();	
		 this.wordsTotal = 0;
		 Tag[] tags = Tag.values();
		 for(int i = 0; i < tags.length; i++){
			 increment(tagInstances, tags[i]);
		 }
	}
	
	public void add(Word word){
		//take a word, check its features, and then add to the appropriate counts
		wordsTotal++;
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
		if(endsInS(string)){
			increment(endsInSTagInstances, tag);
		}
		Integer wordInstance = wordInstances.get(string);
		if(wordInstance == null) { 
			wordInstance = 0; 
		}
		wordInstances.put(string, ++wordInstance);
	}

	private boolean endsInS(String string) {
		int n = string.length() - 1;
		return string.charAt(n) == 's' || string.charAt(n) == 'S';
	}

	private boolean isAllCaps(String string) {
		boolean isAllCaps = true;
		int pos = 0;
		while(isAllCaps && pos < string.length()){
			isAllCaps = Character.isUpperCase(string.charAt(pos));
			pos++;
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
			pos++;
		}
		return false;
	}

	private void increment(Hashtable<Tag, Integer> hashTable, Tag tag) {
		//increment the value for the given tag of the given hashtable
		Integer instances = hashTable.get(tag);
		if(instances == null){
			instances = 0;
		}
		instances += 1;
		hashTable.put(tag, instances);		
	}

	private double numericTagProbability(Tag tag, boolean isNumeric){		
		Integer numericInst = numericTagInstances.get(tag);
		return getProbability(numericInst, isNumeric, tag);		
	}
	
	private double getProbability(Integer instances, boolean needToInvert, Tag tag) {
		if(instances == null){
			instances = 1;
		}
		int tagInstance = tagInstances.get(tag);
		double prob = (double) instances / (double) tagInstance;
		if(!needToInvert){
			prob = 1 - prob;
		}
		return prob;
	}

	private double capitalisedTagProbability(Tag tag, boolean isCapitalised){
		Integer capitalisedInst = capitalisedTagInstances.get(tag);
		return getProbability(capitalisedInst, isCapitalised, tag);
	}
	
	private double allCapsTagProbability(Tag tag, boolean isAllCaps){
		Integer allCapsInst = allCapsTagInstances.get(tag);
		return getProbability(allCapsInst, isAllCaps, tag);
	}
	
	private double endsInSTagProbability(Tag tag, boolean endsInS){
		Integer endsInSInst = endsInSTagInstances.get(tag);
		return getProbability(endsInSInst, endsInS, tag);
	}
	
	public double getTagProbability(String word, Tag tag){
		double numericProb, capitalisedProb, allCapsProb, endsInSProb;
		numericProb = numericTagProbability(tag, isNumeric(word));
		capitalisedProb = capitalisedTagProbability(tag, isCapitalised(word));
		allCapsProb = allCapsTagProbability(tag, isAllCaps(word));
		endsInSProb = endsInSTagProbability(tag, endsInS(word));
		double tagProb = (double) tagInstances.get(tag) / (double) wordsTotal;
		double wordProb = wordProbability(word);
		return tagProb * numericProb * capitalisedProb * allCapsProb * endsInSProb / wordProb;
	}

	private double wordProbability(String word) {
		Integer instances = wordInstances.get(word);
		if(instances == null) instances = 1;
		return (double) instances / (double) wordsTotal;
	}
}
