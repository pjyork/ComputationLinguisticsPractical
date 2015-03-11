package main;


import java.util.ArrayList;
import java.util.List;

import parsing.Tag;
import parsing.Word;
import statisticsTables.StatisticsCompiler;
//takes a given tagger, and then runs cross-validation on it, by compiling the stats required
// for different training sets and runs the tagger on the remaining sentences, giving the tagger
// the stats compiled


public class CrossValidator {
	private List<List<Word>> allSentences;
	private StatisticsCompiler stats;
	
	public CrossValidator(StatisticsCompiler stats, List<List<Word>> allSentences){
		this.stats = stats;
		this.allSentences = allSentences;
	}
	
	public double crossValidation(Tagger tagger) {
		System.out.println("begin validation");
		int n = allSentences.size();
		int part = n/10;
		int k = 0; 
		double[] accuracyArray = new double[10];
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
				Tag[] result = tagger.tagSentence(sentence, stats);
				total += sentence.size();
				for (int j = 1; j < sentence.size(); j++) {
					if(!true){
						System.out.println(sentence.get(j).getTag() + " - " + result[j]);
						System.out.println(sentence.size() + " -  j " + j);
						System.out.println(sentence.get(j).getWord());
					}
					
					if (result[j] == sentence.get(j).getTag()){
						correct+=1;					
					}
				}
				System.out.print("");
			}
			
			double accuracy = (double) correct/ (double) total;

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
	
	public double applyAlgorithm(Tagger tagger){
		double averageAccuracy = crossValidation(tagger);
		System.out.println(averageAccuracy);
		return averageAccuracy;
		
	}
}
