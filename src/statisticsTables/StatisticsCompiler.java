package statisticsTables;
import java.util.Hashtable;
import java.util.List;




import parsing.Tag;
import parsing.Word;


public class StatisticsCompiler {
	//takes a list of tagged sentences and creates or updates existing tables appropriately
	private TagSequenceStatsTable tagSequenceStatsTable;
	private WordTagStatsTable wordTagStatsTable;
	private NaiveBayesFeatures naiveBayesFeatures;
	
	public StatisticsCompiler(){
		this.initialise();	
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
			}
		}		
	}

	public double getSequenceProbability(Tag tag1, Tag tag2) {
		return tagSequenceStatsTable.getSequenceProbability(tag1, tag2);
	}

	public double getTagProbability(Word word, Tag tag) {
		return wordTagStatsTable.getTagProbability(word, tag);
	}
	
	public void initialise() {
		this.tagSequenceStatsTable = new TagSequenceStatsTable();
		this.wordTagStatsTable = new WordTagStatsTable();	
		this.naiveBayesFeatures = new NaiveBayesFeatures();
	}
	
}
