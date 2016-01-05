import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


// Loads Data from XML File, Generates Features and Makes the SVM DataFile

class EQML2 {
	
	private static final String DATA_PATH = "data/";
	private static final String DATA_FILENAME = "wrf."+Config.getWordRankFeatureListSize()+
												"."+Config.getWordRankBucketSize()+
												".brf."+Config.getBigramFeatureListSize()+
												"."+Config.getBigramBucketSize()+
												".wcf."+Config.getWordCoverageFeatureListSize()+
												"."+Config.getWordCoverageBucketSize()+".data";

	public static void main(String[] args) throws Exception { 

		// Read data.xml
		Data data = DataFile.loadFromXml();

		//Generate Features
		for ( Topic topic : data.getTopicList()) {

			for ( Query query : topic.getQueryList()){
				FrequencyListGenerator frequencyListGenerator = new FrequencyListGenerator();

				frequencyListGenerator.generateWordFrequencyList(query);
				frequencyListGenerator.generateBigramFrequencyList(query);

				for( Section s : query.getSectionList()) {
					InterrogativeFeatureGenerator interrogativeFeatureGenerator = new InterrogativeFeatureGenerator();
					s.setInterrogativeFeature(interrogativeFeatureGenerator.generateFeature(s));

					WordAbsenceFeatureGenerator wordAbsenceFeatureGenerator = new WordAbsenceFeatureGenerator();
					s.setWordAbsenceFeature(wordAbsenceFeatureGenerator.generateFeature(s));

					WordRankFeatureGenerator wordRankFeatureGenerator = new WordRankFeatureGenerator();
					s.setWordRankFeatureList(wordRankFeatureGenerator.generateWordRankFeatureList(s));

					BigramRankFeatureGenerator bigramRankFeatureGenerator = new BigramRankFeatureGenerator();
					s.setBigramRankFeatureList(bigramRankFeatureGenerator.generateBigramRankFeatureList(s));

					WordCoverageFeatureGenerator wordCoverageFeatureGenerator = new WordCoverageFeatureGenerator();
					s.setWordCoverageFeatureList(wordCoverageFeatureGenerator.generateWordCoverageFeatureList(s));
				}	
			}
		}
		
		// Delete Existing Files
		File f = new File(DATA_PATH+DATA_FILENAME);
		f.delete();

		// Generate SVM Data
		FileWriter fstream1 = new FileWriter(DATA_PATH+DATA_FILENAME,true); 
		try {	
			BufferedWriter out1 = new BufferedWriter(fstream1);
			for ( Topic topic : data.getTopicList()) {
				for ( Query q : topic.getQueryList() ){ 
					for( Section s : q.getSectionList()) {
						// Print Label
						out1.write((s.getManualLabel() == 1? "+1" : "-1")+" ");

						// Print Sparse Feature Vector
						int k=1;
						for( Double val : s.getFeatureVector() ) {
							if (val != 0) out1.write(k+":"+Double.toString(val)+" ");
							
							k++;
						}
						out1.newLine();
					}
				}
			}
			out1.close();
		} catch (IOException e) {					
			e.printStackTrace();
		} finally {
			try {
				fstream1.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}

