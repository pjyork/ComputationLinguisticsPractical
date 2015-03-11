package statisticsTables;
import java.util.List;




import parsing.Tag;
import parsing.Word;


public class StatisticsCompiler implements Statistics{
	//keeps track of all the statistics required for both viterbi and naive bayes

	private TagSequenceStatsTable tagSequenceStatsTable;
	private WordTagStatsTable wordTagStatsTable;
	private NaiveBayesFeatures naiveBayesFeatures;
	
	public StatisticsCompiler(){
		this.initialise();	
	}
	
	public void initialise() {
		//reset this object
		this.tagSequenceStatsTable = new TagSequenceStatsTable();
		this.wordTagStatsTable = new WordTagStatsTable();	
		this.naiveBayesFeatures = new NaiveBayesFeatures();
	}
	
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
				naiveBayesFeatures.add(word);
			}
		}		
	}

	public double getSequenceProbability(Tag tag1, Tag tag2) {
		//get the probability of a word being tag2, given that it's preceded by tag1
		double prob = tagSequenceStatsTable.getSequenceProbability(tag1, tag2);
		if(prob == 0.0) System.out.println("zer0 tag seq");
		return Math.log10(prob);
	}

	public double getTagProbability(Word word, Tag tag) {
		//get the probability of a word being a tag, given what the string for the word is
		double prob = wordTagStatsTable.getTagProbability(word, tag);
		return Math.log10(prob);
	}
	
	public double getNaiveBayesTagProbability(String word, Tag tag){
		//get the naive bayes probability, given the string of the word
		return Math.log10(naiveBayesFeatures.getTagProbability(word, tag)) + getTagProbability(new Word(word, Tag.CC),tag);
	}
	
}
