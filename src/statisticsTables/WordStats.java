package statisticsTables;
import java.util.Hashtable;

import parsing.Tag;


public class WordStats {
	private Hashtable<Tag, Integer> tagInstances;
	
	public WordStats(){
		tagInstances = new Hashtable<Tag, Integer>();
	}
	
	public void addInstanceOfTag(Tag tag){
		Integer currentVal = tagInstances.get(tag);
		Integer targetVal = 1;
		if(currentVal != null){
			targetVal = currentVal + 1;
		}
		tagInstances.put(tag, targetVal);
	}
	
	public int tag(Tag tag){
		Integer numTag = tagInstances.get(tag);
		if(numTag == null){
			return 1;
		}
		return numTag;
	}
}
