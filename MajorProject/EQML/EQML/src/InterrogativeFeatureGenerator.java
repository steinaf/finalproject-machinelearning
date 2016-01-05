


public class InterrogativeFeatureGenerator extends SingleFeatureGenerator {
    void loadConfig() {
        wordList = Config.getInterrogativeIndicatorList(); 
    }
    
    // To count the num of ?
    @Override
    int special( String s ) {
        int num = 0;

        for ( int i=0; i < s.length(); i++ ) {
            if ( s.charAt(i) == '?' ) {
                num++;
                
                // Ignore simultaneous ? marks
                while ( s.charAt(++i) == '?' ) ;
                // No need for i-- since the current char is not ? and thus useless.
            }
        }
        return num;
    }
}
