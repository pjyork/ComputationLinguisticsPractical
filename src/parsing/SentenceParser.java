package parsing;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SentenceParser {
	//given a file, split it into the constituent sentences, and parse those	
	
	public List<Word> parseSentence(String sentence){
		int i = 0;
		List<Word> result = new ArrayList<Word>();
		char currentChar = '~';
		String currentWord = "";
		String currentTag = "";
		boolean readingTag = false; // is true iff we are currently reading a tag for a word
		//this is true immediately after seeing a '/' character until we see a space
		result.add(new Word(" ", Tag.START));
		while(i < sentence.length()){
			currentChar = sentence.charAt(i++);
			if(!readingTag){
				//if we are reading a word 
				if(currentChar == '/'){
					readingTag = true;
				}
				else if(currentChar != '[' && currentChar != ' ' && currentChar != ']'){
					currentWord += currentChar;
				}
			}
			else{
				//if we are reading a tag
				if(currentChar == ' '){				
					try {
					Tag tag = Tag.valueOf(currentTag);					
					readingTag = false;
					result.add(new Word(currentWord, tag));
					currentWord = "";
					currentTag = "";
					}
					catch (IllegalArgumentException e) {
						readingTag = false;
						currentWord = "";
						currentTag = "";
						
					}
				}
				else{
					currentTag += currentChar;
				}
			}
		}
		return result;
	}
	
	

	public List<String> splitSentences(String fileName) throws IOException{
		//return a list of strings where each string represents one sentence
		//which is otherwise unchanged from the file
		LinkedList<String> results = new LinkedList<String>();
		BufferedReader istream = new BufferedReader(new FileReader(fileName));
		try {
			String current = "";
			String line = "";
            while( (line = istream.readLine()) != null) {
            	if(line.equalsIgnoreCase("./. ")){
            		results.add(current);
            		current = "";
            	}
            	else if(!line.equals("======================================")){
            		current += line;
            	}	
            }
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		}
		istream.close();
		return results;
	}
	
	public List<List<Word>> parseSentences(String fileName){
		List<List<Word>> results = new ArrayList<List<Word>>();
		List<String> sentenceStrings = new LinkedList<String>();
		try {
			sentenceStrings = splitSentences(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String sentenceString : sentenceStrings){
			results.add(parseSentence(sentenceString));
		}
		return results;		
	}
}
