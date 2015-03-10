package parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



import statisticsTables.StatisticsCompiler;

public class FileParser {
	
	//sort out relationships between sentence parsing and counter hashtables;
	

	
	
	public List<List<Word>> parseFiles(File dir, StatisticsCompiler stats) {
		File directories[] = dir.listFiles();
		List<List<Word>> allSentences = new ArrayList<List<Word>>();
		SentenceParser parser = new SentenceParser();
		for (File directory : directories) {
			File files[] = directory.listFiles();
			for (File file:files) {				
				String fileName = file.getAbsolutePath();;
				List<List<Word>> sentences = parser.parseSentences(fileName); 
				allSentences.addAll(sentences);
			}
		}
		return allSentences;
		
	}
	
	
	
	
}
