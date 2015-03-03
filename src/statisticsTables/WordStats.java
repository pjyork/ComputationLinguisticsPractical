package statisticsTables;
import java.util.Hashtable;

import parsing.Tag;


public class WordStats {
	private Hashtable<Tag, Integer> tagInstances;
	private Integer instances;
	
	public WordStats(){
		tagInstances = new Hashtable<Tag, Integer>();
		instances = 0;
	}
	
	public void addInstanceOfTag(Tag tag){
		instances += 1;
		Integer currentVal = tagInstances.get(tag);
		Integer targetVal = 1;
		if(currentVal != null){
			targetVal = currentVal + 1;
		}
		tagInstances.put(tag, targetVal);
	}
	
	public double tagProbability(Tag tag){
		return tagInstances.get(tag) / instances; 
	}
}
