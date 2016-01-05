import java.io.*;

// Take Input of Topics (1 by 1), generate Sections and Manually Label them, store in Data File.

class EQML1 {

	public static void main(String[] args) throws Exception { 

		Data data = new Data();
		Topic topic = null;

		String pageData = "";
		//int numSections;
		
		for (int i=0; i < Config.getNumberTopics(); i++) {		
			// Take Topic as Input 
			try{
				System.out.print("Enter the topic : ");
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				topic = new Topic(bufferRead.readLine());
				data.addTopic(topic);
			} catch(IOException e) {
				e.printStackTrace();
			}
			//numSections=0;

			System.out.println("Entered topic is : "+topic.getText());

			// Generate Queries
			QueryGenerator queryGenerator = new QueryGenerator();       
			topic.setQueryList(queryGenerator.generateQueryList(topic.getText()));

			// Output the queries generated
			for (Query q : topic.getQueryList()) {
				q.setTopic(topic);
				System.out.println(q.getText());
			}

			HTMLParser htmlParser = new HTMLParser();
			SearchAPI searchAPI = new SearchAPI();

			try {
				for (Query q : topic.getQueryList()) {	
					
					// Generate Links from Search API
					q.setLinkList(searchAPI.generateLinkList(q.getText().replace(' ','+')));
	
					for (String link : q.getLinkList()) {
						pageData = pageData + htmlParser.parse(link);
						//break; //TEST
					}
					// All pages loaded into pageData
					
					// Generate Sections
					SectionGenerator sectionGenerator = new SectionGenerator(); 
					
					q.setSectionList(sectionGenerator.generateSections(pageData)); 
					
					for( Section s : q.getSectionList()) {
						s.setQuery(q);
						//numSections++;
					}
					
					pageData = "";
					//if (numSections >= Config.getSectionsPerTopic()) break;
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("Sections for "+topic.getText()+" generated successfully.");
		} // end for loop
		
		// Label Sections for all data
		
		try {
			ManualLabeller manualLabeller = new ManualLabeller();						
			manualLabeller.run(data);
		} catch (NullPointerException e) {					
			e.printStackTrace();
		}

		DataFile.saveToXml(data);
	}
}
