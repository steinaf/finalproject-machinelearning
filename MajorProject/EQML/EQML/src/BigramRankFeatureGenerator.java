


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class BigramRankFeatureGenerator {
	
	private int featureListSize = Config.getBigramFeatureListSize(); 	// TO BE LOADED FROM XML
	private int bucketSize = Config.getBigramBucketSize();			// TO BE LOADED FROM XML
	
	private int i, j, position; 
    private double featureValue;
    
	private Query q;
	private Map<String, Integer> sectionBigramFrequencyList;
	
	private List<Entry<String,Integer>> queryBigramFrequencyList; // Bigram frequency list of the Query

	private List<Double> bigramRankFeatureList;	// Bigram feature list of the section
	
	public List<Double> generateBigramRankFeatureList(Section s) throws Exception {
		
		q = s.getQuery();
		
		// Initialising featureListSize number of elements to initial value 0.0
		bigramRankFeatureList = new ArrayList<Double>(Collections.nCopies(featureListSize, 0.0));

		queryBigramFrequencyList = q.getBigramFrequencyList();

		FrequencyListGenerator frequencyListGenerator = new FrequencyListGenerator();
		sectionBigramFrequencyList = frequencyListGenerator.generateBigramFrequencyMap(s);
		
		// generating the feature list
		for(i=0; i<featureListSize;i++) {
			
			featureValue = 0;
			for(j=0; j<bucketSize; j++ ) {
				
				position = i*bucketSize + j;
				
				if(position >= queryBigramFrequencyList.size())
					break;
				
				if (sectionBigramFrequencyList.containsKey (queryBigramFrequencyList.get(position).getKey())) {
					
					featureValue = featureValue + sectionBigramFrequencyList.get(queryBigramFrequencyList.get(position).getKey());
				}
			}

			featureValue = featureValue/s.getLength();
			bigramRankFeatureList.set(i, featureValue);
		}
			
		return bigramRankFeatureList;
		
	}
}
