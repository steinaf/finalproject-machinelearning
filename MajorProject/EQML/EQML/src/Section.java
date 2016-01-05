
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="section")
@XmlType(propOrder={"text", "manualLabel", "svmLabel", "inTestingSet", "inTrainingSet", "interrogativeFeature", "wordAbsenceFeature", "wordRankFeatureList", "bigramRankFeatureList", "wordCoverageFeatureList"})
public class Section {

    private String text;
    private int manualLabel;
    private int svmLabel;
    @XmlTransient
    private Query query;
    private Boolean inTestingSet;
    private Boolean inTrainingSet;
    @XmlTransient
    private int length;

    private double interrogativeFeature;
    private double wordAbsenceFeature;
    private List<Double> wordRankFeatureList;
    private List<Double> bigramRankFeatureList;
    private List<Double> wordCoverageFeatureList;

    private Map<String,Integer> wordFrequencyHashmap;

    public void setText(String t) {
        text = t;
    }

    public String getText() {
        return text;
    }

    public void setLength( int l ) {
        length = l;
    }

    @XmlTransient
    public int getLength() {
        return length;
    }

    // manualLabel values
    //      0 = False
    //      1 = TRUE
    public int getManualLabel() {

        return manualLabel;
    }

    public void setManualLabel(int lab) {

        manualLabel = lab;
    }
     
    public int getSvmLabel() {
		return svmLabel;
	}

	public void setSvmLabel(int svmLabel) {
		this.svmLabel = svmLabel;
	}

	public double getInterrogativeFeature() {
		return interrogativeFeature;
	}

	public void setInterrogativeFeature(double interrogativeFeature) {
		this.interrogativeFeature = interrogativeFeature;
	}

	public double getWordAbsenceFeature() {
		return wordAbsenceFeature;
	}

	public void setWordAbsenceFeature(double wordAbsenceFeature) {
		this.wordAbsenceFeature = wordAbsenceFeature;
	}
	
	@XmlElementWrapper
    @XmlElement(name="wrf")
	public List<Double> getWordRankFeatureList() {
		return wordRankFeatureList;
	}

	public void setWordRankFeatureList(List<Double> wordRankFeatureList) {
		this.wordRankFeatureList = wordRankFeatureList;
	}

	@XmlElementWrapper
    @XmlElement(name="brf")
	public List<Double> getBigramRankFeatureList() {
		return bigramRankFeatureList;
	}

	public void setBigramRankFeatureList(List<Double> bigramRankFeatureList) {
		this.bigramRankFeatureList = bigramRankFeatureList;
	}

	@XmlElementWrapper
    @XmlElement(name="wcf")
	public List<Double> getWordCoverageFeatureList() {
		return wordCoverageFeatureList;
	}

	public void setWordCoverageFeatureList(List<Double> wordCoverageFeatureList) {
		this.wordCoverageFeatureList = wordCoverageFeatureList;
	}

	public void setQuery( Query q ) {
        query = q; 
    }

    @XmlTransient
    public Query getQuery() {
        return query;
    }

    public List<Double> getFeatureVector () {
        List<Double> fv = new ArrayList<Double>() ;

        fv.add( (Double) interrogativeFeature );
        fv.add( (Double) wordAbsenceFeature );
        fv.addAll( (List<Double>) wordRankFeatureList );
        fv.addAll( (List<Double>) bigramRankFeatureList );
        fv.addAll( (List<Double>) wordCoverageFeatureList );

        return fv;
    }

    public void setInTrainingSet ( Boolean x ) {
        inTrainingSet = x;
    }

    public void setInTestingSet ( Boolean x ) {
        inTestingSet = x;
    }

    public Boolean getInTrainingSet() {
        return inTrainingSet;
    }

    public Boolean getInTestingSet() {
        return inTestingSet;
    }

    public int getWordFrequency( String w ) {

        if ( wordFrequencyHashmap == null ) generateWordFrequencyHashmap() ;

        if ( wordFrequencyHashmap.containsKey( w ) ) return wordFrequencyHashmap.get( w ) ;
        else return 0;
    }

    private void generateWordFrequencyHashmap() {
        FrequencyListGenerator flg = new FrequencyListGenerator();
        try {
            wordFrequencyHashmap = flg.generateWordFrequencyMap( (Section) this ) ;
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage()) ;
        }
    }

    public Section(String text) {
			this.text = text;
	}
    
    public Section() {
    }

	public void setWordFrequencyHashmap( Map<String,Integer> hash) {
        wordFrequencyHashmap = hash;
    }

	@XmlTransient
    public Map<String,Integer> getWordFrequencyHashmap() {
        return wordFrequencyHashmap;
    }

}

