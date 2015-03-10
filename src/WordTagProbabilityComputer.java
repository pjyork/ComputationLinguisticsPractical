import java.io.File;
import java.util.List;

import parsing.FileParser;
import parsing.Word;
import statisticsTables.StatisticsCompiler;


public class WordTagProbabilityComputer {
	
	public static void main(String[] args){
		StatisticsCompiler stats = new StatisticsCompiler();
		
		FileParser fileParser = new FileParser();
		List<List<Word>> allSentences = fileParser.parseFiles(new File("C:\\Users\\Peter\\workspace\\WSJ-2-12\\WSJ-2-12"), stats);
		CrossValidator validator = new CrossValidator(stats, allSentences);
		validator.applyViterbiAlgorithm();
	}
}
