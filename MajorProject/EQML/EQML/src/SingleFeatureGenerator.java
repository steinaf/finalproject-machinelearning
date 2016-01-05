

import java.util.List;

abstract class SingleFeatureGenerator {
   
    protected List<String> wordList;

    public double generateFeature( Section sec ) {

        double feature = 0;

        for ( String s : wordList ) {
            feature += sec.getWordFrequency( s ) ;
        }

        // Question mark handling
        feature += special( sec.getText() );

        return ( feature / sec.getLength() );
    }

    int special( String s ) { 
        return 0;
    }

    abstract void loadConfig() ;

    public SingleFeatureGenerator() {
        loadConfig();
    }
}
