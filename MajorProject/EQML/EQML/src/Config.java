
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {

    static int bigramFeatureListSize;
    static int bigramBucketSize;
    static int wordRankFeatureListSize;
    static int wordRankBucketSize;
    static int wordCoverageFeatureListSize;
    static int wordCoverageBucketSize;
    static List<String> querySuffixList;
    static List<String> interrogativeIndicatorList;
    static List<String> wordAbsenceList;
    static List<String> linkBlacklist;
    static int numberTopics;
    static int sectionsPerTopic;
    static int pagesPerQuery;

    public static int getPagesPerQuery() {
		return pagesPerQuery;
	}

	public static void setPagesPerQuery(int pagesPerQuery) {
		Config.pagesPerQuery = pagesPerQuery;
	}

	public static int getSectionsPerTopic() {
		return sectionsPerTopic;
	}

	public static void setSectionsPerTopic(int sectionsPerTopic) {
		Config.sectionsPerTopic = sectionsPerTopic;
	}

	public static int getNumberTopics() {
		return numberTopics;
	}

	public static void setNumberTopics(int numberTopics) {
		Config.numberTopics = numberTopics;
	}

	static public int getBigramFeatureListSize() {
        return bigramFeatureListSize;
    }

    static public int getBigramBucketSize() {
        return bigramBucketSize;
    }

    static public int getWordRankFeatureListSize() {
        return wordRankFeatureListSize;
    }

    static public int getWordRankBucketSize() {
        return wordRankBucketSize;
    }

    static public int getWordCoverageBucketSize() {
        return wordCoverageBucketSize;
    }

    static public int getWordCoverageFeatureListSize() {
        return wordCoverageFeatureListSize;
    }

    static public List<String> getQuerySuffixList() {
        return querySuffixList;
    }

    static public List<String> getInterrogativeIndicatorList() {
        return interrogativeIndicatorList;
    }
    
    static public List<String> getLinkBlacklist() {
        return linkBlacklist;
    }

    static public List<String> getWordAbsenceList() {
        return wordAbsenceList;
    }

    static {
        // static constructor - load all values from config.properties file

        Properties prop = new Properties();
        FileInputStream fin = null;
        try {
            // load a properties file
            fin = new FileInputStream("config.properties");
            prop.load(fin);

            // ("property", DEFAULT VALUE)
            try {
                bigramFeatureListSize =         Integer.parseInt(prop.getProperty("bigramFeatureListSize",      "150"));
                bigramBucketSize =              Integer.parseInt(prop.getProperty("bigramBucketSize",           "1"));
                wordRankFeatureListSize =       Integer.parseInt(prop.getProperty("wordRankFeatureListSize",    "150"));
                wordRankBucketSize =            Integer.parseInt(prop.getProperty("wordRankBucketSize",         "1"));
                wordCoverageFeatureListSize =   Integer.parseInt(prop.getProperty("wordCoverageFeatureListSize","150"));
                wordCoverageBucketSize =        Integer.parseInt(prop.getProperty("wordCoverageBucketSize",     "5"));
                numberTopics =        			Integer.parseInt(prop.getProperty("numberTopics",    			"5"));
                sectionsPerTopic =        		Integer.parseInt(prop.getProperty("sectionsPerTopic",     		"2000"));
                pagesPerQuery = 				Integer.parseInt(prop.getProperty("pagesPerQuery",     			"5"));
            } catch (NumberFormatException e) {
               // System.err.println("Number format exception in config : " + e.printStackTrace());
            	e.printStackTrace();
            }

            querySuffixList =                   Arrays.asList(prop.getProperty("querySuffixList", "questions,problems,solved examples").split(","));
            interrogativeIndicatorList =        Arrays.asList(prop.getProperty("interrogativeIndicatorList", "what,why,explain,find,calculate,describe").split(","));
            wordAbsenceList =                   Arrays.asList(prop.getProperty("wordAbsenceList","because,yes,no,therefore").split(","));
            linkBlacklist = 					Arrays.asList(prop.getProperty("linkBlacklist",".pdf,.doc").split(","));
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    } //end constructor
}
