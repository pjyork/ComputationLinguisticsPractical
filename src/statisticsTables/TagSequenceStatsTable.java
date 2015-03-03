package statisticsTables;
import java.io.Serializable;
import java.util.Hashtable;

import parsing.Tag;


public class TagSequenceStatsTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//a table from tag to a class which, for the given tag, has the probability of each POS following it
	Hashtable<Tag, TagStats> table = new Hashtable<Tag, TagStats>();

	public void addSeq(Tag tag1, Tag tag2) {
		TagStats tagStats = table.get(tag1);
		if(tagStats == null){
			tagStats = new TagStats();
		}
		tagStats.addInstanceOfFollowingTag(tag2);		
	}
	
}