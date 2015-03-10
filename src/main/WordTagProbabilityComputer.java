package main;
import java.io.File;
import java.util.List;

import naiveBayes.NaiveBayes;
import parsing.FileParser;
import parsing.Word;
import statisticsTables.StatisticsCompiler;
import viterbi.Viterbi;


public class WordTagProbabilityComputer {
	
	public static void main(String[] args){
		StatisticsCompiler stats = new StatisticsCompiler();
		
		FileParser fileParser = new FileParser();
		List<List<Word>> allSentences = fileParser.parseFiles(new File("C:\\Users\\Peter\\workspace\\WSJ-2-12\\WSJ-2-12"), stats);
		CrossValidator validator = new CrossValidator(stats, allSentences);
		Viterbi viterbi = new Viterbi();
		NaiveBayes naiveBayes = new NaiveBayes();
		System.out.println("viterbi, commence - ");
		validator.applyAlgorithm(viterbi);
		

		System.out.println("naive bayes, commence - ");
		validator.applyAlgorithm(naiveBayes);
	}
}
