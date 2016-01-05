
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchAPI {	
	List<String> generateLinkList(String query) throws Exception {		
		String key="AIzaSyCFIcaooeYgLskAvwTXHBqhPKOmn7pTv1Y";   // API Key
		List<String> linkList = new ArrayList<String>();
		
		System.out.println("Entered Search API for query : "+query);
		
		int pageNo,i=0;
		
		// connecting to API and accessing web-page
		for(pageNo=0; pageNo < Config.getPagesPerQuery(); pageNo++) {					
			URL url = new URL("https://www.googleapis.com/customsearch/v1?key="+key+"&cx=013036536707430787589:_pqjad5hr1a&q="+query+"&alt=json"+"&start="+(pageNo*10+1));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output;
			System.out.println("Output from Server .... \n");
			outerloop:
			while ((output = br.readLine()) != null) {
				//if(output.contains(".pdf") || output.contains(".doc") ) continue;
				if(output.contains("\"link\": \"")){  
					for ( String s : Config.getLinkBlacklist()) {
						if ( output.contains(s)) {
							System.out.println("Skipped : " + output);
							continue outerloop;
						}
					}
					linkList.add(output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\",")));						// Storing the links to be accessed for Sections
					System.out.println(linkList.get(i));       //Will print the google search links
					i++;
				}     
			}
			conn.disconnect();
			System.out.println();

		}
		System.out.println("Terminating Search API : "+query);
		return linkList;
	}
}
