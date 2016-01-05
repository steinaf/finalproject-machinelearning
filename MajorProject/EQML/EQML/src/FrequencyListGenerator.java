

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class FrequencyListGenerator {

	private String text, token;
	private Version matchVersion = Version.LUCENE_41;
	private Reader reader;
	private TokenStream stream;
	private Map<String, Integer> uniqueTokens;
	private List<Entry<String,Integer>> orderedTokens;;
	
	private Map<String, Integer> tokenList, mergedTokenList;
	
	private ShingleFilter filter; // used to filter out pairs of tokens - generates the bigrams
	
	// Function to sort Tokens in their descending order of frequency
	static List<Entry<String,Integer>> getTokensInDescendingFreqOrder(Map<String, Integer> wordCount) {

		// Convert map to list of <String,Integer> entries
		List<Map.Entry<String, Integer>> list = 
				new ArrayList<Map.Entry<String, Integer>>(wordCount.entrySet());

		// Sort list by integer values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				// compare o2 to o1, instead of o1 to o2, to get descending freq. order
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		return list;
	}

	// Function to generate word frequency list of a section
	public Map<String, Integer> generateWordFrequencyMap(Section s) throws Exception {
		
		text = s.getText();
		reader =  new StringReader(text);
		stream = new StandardTokenizer(matchVersion, reader);
		stream.reset(); // Throws IOException
		uniqueTokens = new HashMap<String, Integer>();
        int l = 0;
		
		while (stream.incrementToken()) {
			token = stream.getAttribute(CharTermAttribute.class).toString();

			if (! uniqueTokens.containsKey (token.toLowerCase())) {
				uniqueTokens.put (token.toLowerCase(), 1);
			}
			else {
				uniqueTokens.put (token.toLowerCase(), uniqueTokens.get (token.toLowerCase()) + 1);
			}
            l++;
		}
		
        s.setLength(l);
        s.setWordFrequencyHashmap(uniqueTokens);

		return uniqueTokens;
	}
	
	// Function to generate bigram frequency list of a section
	public Map<String, Integer> generateBigramFrequencyMap(Section s) throws Exception {
		
		text = s.getText();
		reader =  new StringReader(text);
		stream = new StandardTokenizer(matchVersion, reader);
		stream.reset();

		filter = new ShingleFilter(stream, 2, 2); // filtering out token pairs from the TokenStream
		filter.setOutputUnigrams(false);	// avoid outputting unigrams

		uniqueTokens = new HashMap<String, Integer>();

		while (filter.incrementToken()) {

			token=filter.getAttribute(CharTermAttribute.class).toString();

			if (! uniqueTokens.containsKey (token.toLowerCase()))
				uniqueTokens.put (token.toLowerCase(), 1);
			else
				uniqueTokens.put (token.toLowerCase(), uniqueTokens.get (token.toLowerCase()) + 1);
		}
		
		return uniqueTokens;		
	}
	
	public void generateWordFrequencyList(Query q) throws Exception {

		mergedTokenList = new HashMap<String, Integer>();
		
		for (Section s : q.getSectionList()) {

			tokenList = generateWordFrequencyMap(s);

			// merging hashmap of the current section with the one generated so far
			
			for (String x : tokenList.keySet()) {
				if ( ! mergedTokenList.containsKey(x)) {
					mergedTokenList.put(x, tokenList.get(x));
				} else {
					mergedTokenList.put(x, tokenList.get(x)+mergedTokenList.get(x));
				}
			}
			
		}
		
		// ordering the word frequency list in descending order
		orderedTokens = getTokensInDescendingFreqOrder(mergedTokenList);

//				usage
//				Iterator<Entry<String, Integer>> iterator=orderedTokens.iterator();
//				Entry<String, Integer> entry;
//				while(iterator.hasNext()) {
//					 entry = iterator.next();
//					 System.out.println(entry.getKey() + " " + entry.getValue());
//				}

		q.setWordFrequencyList(orderedTokens);

	}

	public void generateBigramFrequencyList(Query q) throws Exception {

		mergedTokenList = new HashMap<String, Integer>();
		tokenList = new HashMap<String, Integer>();
		
		for (Section s : q.getSectionList()) {
			tokenList = generateBigramFrequencyMap(s);

			for (String x : tokenList.keySet()) {
				if ( ! mergedTokenList.containsKey(x)) {
					mergedTokenList.put(x, tokenList.get(x));
				} else {
					mergedTokenList.put(x, tokenList.get(x)+mergedTokenList.get(x));
				}
			}
		}
		

		orderedTokens = getTokensInDescendingFreqOrder(mergedTokenList);

//		converting List<Entry<String,Integer>> into List<String>
//		List<String> result = new ArrayList<String>();
//		for (Map.Entry<String, Integer> entry : orderedTokens) {
//			result.add(entry.getKey());
//		}
//		return list;
		
		q.setBigramFrequencyList(orderedTokens);

	}
}
