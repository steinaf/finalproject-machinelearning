

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class WordRankFeatureGenerator {
	
	private int featureListSize = Config.getWordRankFeatureListSize() ; 	// TO BE LOADED FROM XML
	private int bucketSize = Config.getWordRankBucketSize();			// TO BE LOADED FROM XML
	
	private int i, j, position;
    private double featureValue; 
	
	private Query q;
	private Map<String, Integer> sectionWordFrequencyHashmap;
	
	private List<Entry<String,Integer>> queryWordFrequencyList;	// Word frequency list of the query
	private List<Double> wordRankFeatureList;	// word rank feature list of the section
	
	public List<Double> generateWordRankFeatureList(Section s) throws Exception {
	
		q = s.getQuery();
		
		// Initialising featureListSize number of elements to initial value 0.0
		wordRankFeatureList = new ArrayList<Double>(Collections.nCopies(featureListSize, 0.0));

		queryWordFrequencyList = q.getWordFrequencyList();
		
		sectionWordFrequencyHashmap = s.getWordFrequencyHashmap();
	
		// generating the feature list
		for(i=0; i < featureListSize; i++) {
			
			featureValue=0;
			
			for(j=0; j < bucketSize; j++) {
				
				position = i*bucketSize + j;
				
				if(position>= queryWordFrequencyList.size())
					break;
				
				if(sectionWordFrequencyHashmap.containsKey(queryWordFrequencyList.get(position).getKey())) {				
					featureValue = featureValue + (sectionWordFrequencyHashmap.get(queryWordFrequencyList.get(position).getKey()));
				}
			}
			
			featureValue = featureValue/s.getLength();
			wordRankFeatureList.set(i, featureValue);
		}
		
		return wordRankFeatureList;
	}
}
