
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.examples.*;
import org.jsoup.nodes.Document;

// To Parse Plain text from HTML page
public class HTMLParser {	
    String parse (String url) throws Exception {
        FileWriter fstream1;
        BufferedWriter out1;
        Document doc = null;
        String parsedText="          ";
        
        try {
            fstream1 =new FileWriter("data/webText",true);
            out1 =new BufferedWriter(fstream1);
            
            doc = Jsoup.connect(url).timeout(0).get();		 		
            HtmlToPlainText formatter = new HtmlToPlainText();
            
            String plainText = formatter.getPlainText(doc);
            parsedText = html2text(plainText);
            
            out1.write(parsedText);
            out1.newLine();
            
            out1.close();
            fstream1.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("html parser - page load ignored : " + url);
        }
        //System.out.println("returning " + parsedText.substring(0, 5) +"....");
        return parsedText;
    }

    public static String html2text(String html) {		
        return(html.replaceAll("\\<.*?>",""));		// replaces text like <1234> also :/
    }
}
