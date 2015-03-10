

import java.util.ArrayList;
import java.util.List;

import parsing.Tag;
import parsing.Word;
import statisticsTables.StatisticsCompiler;
import viterbi.Viterbi;

public class CrossValidator {
	private Viterbi viterbi;
	private List<List<Word>> allSentences;
	private StatisticsCompiler stats;
	
	public CrossValidator(StatisticsCompiler stats, List<List<Word>> allSentences){
		this.stats = stats;
		this.viterbi = new Viterbi(stats);
		this.allSentences = allSentences;
	}
	
	public float crossValidation() {
		System.out.println("begin validation");
		int n = allSentences.size();
		int part = n/10;
		int k = 0; 
		float[] accuracyArray = new float[10];
		for (int i=0; i<10; i++) {
			stats.initialise();
			List<List<Word>> testing = allSentences.subList(k, k + part);			
			List<List<Word>> training = new ArrayList<List<Word>>();
			training.addAll(allSentences);
			training.removeAll(testing);
			stats.updateStats(training);
			//System.out.println(counter.tagCounter.values());
			int correct = 0;
			int total = 0;
			for (List<Word> sentence : testing) {
				
				Tag[] result = viterbi.tagSentence(sentence);				
				total += sentence.size();
				for (int j = 0; j < sentence.size(); j++) {
					if (result[j] == sentence.get(j).getTag()){
						correct+=1;					
					}
				}				
			}
			
			float accuracy = correct/total;

			System.out.println("testing - " + i + " complete" + " - accuracy = " + accuracy);
	
			accuracyArray[i] = accuracy;	
			k = k + part;
		}
		float sum = 0;
		for (int p=0; p<10; p++){
			sum+=accuracyArray[p];			
		}
		return sum/10;		
	}
	
	public float applyViterbiAlgorithm(){
		float averageAccuracy = crossValidation();
		System.out.println(averageAccuracy);
		return averageAccuracy;
		
	}
}
