import java.io.*;

// ASSUMPTIONS : 
// data/svmData.model and data/svmData.range EXISTS [ Model is generated ]

public class EQML_RealRun {

	public static void main (String args[]) throws Exception {
		EQML_RealRun obj = new EQML_RealRun();
		obj.generateDataFile();
		obj.outputPredictions();
	}

	private void generateDataFile() throws Exception {
		Topic topic = new Topic();
		String pageData = "";

		// Take Topic as Input 
		try{
			System.out.print("Enter the topic : ");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			topic = new Topic(bufferRead.readLine());
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Entered topic is : "+topic.getText());

		// Generate Queries
		QueryGenerator queryGenerator = new QueryGenerator();       
		topic.setQueryList(queryGenerator.generateQueryList(topic.getText()));

		for (Query q : topic.getQueryList()) {
			q.setTopic(topic);
		}

		try {
			for (Query q : topic.getQueryList()) {	

				HTMLParser htmlParser = new HTMLParser();
				SearchAPI searchAPI = new SearchAPI();

				// Generate Links from Search API
				q.setLinkList(searchAPI.generateLinkList(q.getText().replace(' ','+')));

				for (String link : q.getLinkList()) {
					pageData = pageData + htmlParser.parse(link);
				}
				// All pages loaded into pageData

				// Generate Sections
				SectionGenerator sectionGenerator = new SectionGenerator(); 

				q.setSectionList(sectionGenerator.generateSections(pageData)); 

				for( Section s : q.getSectionList()) {
					s.setQuery(q);
				}

				pageData = "";
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

		Data d = new Data();
		d.addTopic(topic);
		DataFile.saveToXml(d);
	}

	private void outputPredictions() throws Exception {
		Data data = DataFile.loadFromXml();
		Topic topic = data.getTopicList().get(0);

		// Generate Features
		
		System.out.println("Generating Features");

		for ( Query query : topic.getQueryList()){
			FrequencyListGenerator frequencyListGenerator = new FrequencyListGenerator();

			if (query.getSectionList() == null) continue;
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
		
		System.out.println("Generating SVM Data");

		// Generate SVM Data
		File f = new File("data/realRunData");
		f.delete();
		
		FileWriter fstream1 = new FileWriter("data/realRunData"); 
		try {	
			BufferedWriter out1 = new BufferedWriter(fstream1);
			for ( Query q : topic.getQueryList() ){ 
				if (q.getSectionList() == null) continue;
				for( Section s : q.getSectionList()) {
					// Print Filler Label
					out1.write("0 ");

					// Print Sparse Feature Vector
					int k=1;
					for( Double val : s.getFeatureVector() ) {
						if (val != 0) out1.write(k+":"+Double.toString(val)+" ");

						k++;
					}
					out1.newLine();
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

		// Scale SVM Data
		
		File f1 = new File("data/realRunData.scale");
		f1.delete();
		File f2 = new File("data/realRunData.output");
		f2.delete();
		File f3 = new File("output/realOutput.txt");
		f3.delete();
		
		System.out.println("Scaling Features");
		
		PrintStream orig = System.out;
		try {
			
			System.setOut(new PrintStream(new File("data/realRunData.scale")));

			String scaleArgs = "-l 0 -r data/svmData.range data/realRunData";		
			svm_scale.main(scaleArgs.split(" ")); 	

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.setOut(orig);
		}

		System.out.println("Predicting Data");
		
		class NullOutputStream extends OutputStream {
			  @Override
			  public void write(int b) throws IOException {
			  }
			  public void write(byte[] b) throws IOException {
			  }
			  public void write(byte[] b, int off, int len) throws IOException {
			  }
			}
		// Get Predictions
		
		System.setOut(new PrintStream(new NullOutputStream()));
    
		String[] predictorArgs = new String[] { "data/realRunData.scale" , "data/svmData.model" , "data/realRunData.output" };		
		svm_predict.main(predictorArgs); 	
		//Thread.sleep(1000);
		
		System.setOut(orig);
		//Load SVM generated labels into data and print output to output file
		
		try {
			System.out.println("Generating Output");
			FileReader predictionFile = new FileReader("data/realRunData.output");
			BufferedReader br = new BufferedReader(predictionFile);
			FileWriter fstream11 = new FileWriter("output/realOutput.txt"); 
			BufferedWriter out21 = new BufferedWriter(fstream11);

			out21.write("TOPIC : " + topic.getText() + "\n-----------------------------------------------------------------");
			out21.newLine();

			for (Query query : topic.getQueryList()) {
				if (query.getSectionList() == null) continue;
				for (Section section : query.getSectionList()) {
					String line = br.readLine();
					if (line == null) break;
					if (line.equals("1.0"))
					{
						//System.out.println("Question output " + section.getText());
						out21.write(section.getText()); 
						out21.newLine();
						section.setSvmLabel(1);
					}
				}
			}
			out21.close();
			fstream11.close();
			br.close();
			predictionFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Question list written to output/realOutput.txt");
		
		File f4 = new File("data/realRunData.scale");
		f4.delete();
		File f5 = new File("data/realRunData.output");
		f5.delete();
		File f6 = new File("data/realRunData");
		f6.delete();
	}	
}
