package statisticsTables;
import java.util.Hashtable;

import parsing.Tag;


public class TagStats {
	private Hashtable<Tag, Integer> table;
	private Integer instances;
	
	public TagStats(){
		table = new Hashtable<Tag, Integer>();
		instances = 0;
	}
	
	public void addInstanceOfFollowingTag(Tag followingTag){
		instances += 1;
		Integer currentVal = table.get(followingTag);
		Integer targetVal = 1;
		if(currentVal != null){
			targetVal = currentVal + 1;
		}
		table.put(followingTag, targetVal);
	}
	
	public double followingTagProbability(Tag tag){
		return table.get(tag) / instances;
	}
}
