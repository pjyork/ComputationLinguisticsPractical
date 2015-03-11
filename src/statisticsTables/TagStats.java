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
		if(instances == 0){
		//	return 0.01;
			System.out.println("wah!");
		}
		Integer followingInstances = table.get(tag);
		if(followingInstances == null){	
			followingInstances = 0;
		}
		
		
		return (double) (followingInstances+1) / (double) (instances + Tag.values().length);
	}
}
