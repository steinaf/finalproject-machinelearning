




public class WordAbsenceFeatureGenerator extends SingleFeatureGenerator {
    void loadConfig() {
        // load wordList from config for bad words
        
        // randomlist for testing till config implemented
        wordList = Config.getWordAbsenceList();
        		//Arrays.asList("because", "yes", "no", "therefore");
    }
}
