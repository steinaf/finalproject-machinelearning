

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class WordCoverageFeatureGenerator {

	private int featureListSize = Config.getWordCoverageFeatureListSize() ; 	// TO BE LOADED FROM XML
	private int bucketSize = Config.getWordCoverageBucketSize();			// TO BE LOADED FROM XML

	private int i, j, position;
    private double featureValue;
	
	private Map<String, Integer> sectionWordFrequencyHashmap;
	private Query q;
	
	private List<Double> wordCoverageFeatureList;	// word coverage feature list of the section
	private List<Entry<String,Integer>> queryWordFrequencyList;	// unigram frequency list of the query
	
	public List<Double> generateWordCoverageFeatureList(Section s) throws Exception {

		q = s.getQuery();
		
		// Initialising featureListSize number of elements to initial value 0.0
		wordCoverageFeatureList = new ArrayList<Double>(Collections.nCopies(featureListSize, 0.0));
		
		queryWordFrequencyList = q.getWordFrequencyList();
		
		sectionWordFrequencyHashmap = s.getWordFrequencyHashmap();

		// generating the feature list
		for(i=0;i<featureListSize;i++) {
			
			featureValue=0;
			
			for(j=0;j<bucketSize;j++) {
				
				position = i*bucketSize + j;
				
				if(position >= queryWordFrequencyList.size())
					break;
				
				if( sectionWordFrequencyHashmap.containsKey(queryWordFrequencyList.get(position).getKey())) {
					featureValue = featureValue + 1;
				}
			}
			
			wordCoverageFeatureList.set(i, featureValue);
		}
		
		return wordCoverageFeatureList;

	}
}
