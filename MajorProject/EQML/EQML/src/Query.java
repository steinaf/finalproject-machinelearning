
import java.util.*;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="query")
@XmlType(propOrder={"text", "linkList", "sectionList"})
public class Query {

	private List<Section> sectionList;

    private List<String> linkList;

	private String text;

	@XmlTransient
	private List<Entry<String,Integer>> wordFrequencyList;
	
	@XmlTransient
	private List<Entry<String,Integer>> bigramFrequencyList;

	@XmlTransient
	private Topic topic;
	
	public void setText(String t) {
		text = t;
	}

	public String getText() {
		return text;
	}

	public void setSectionList(List<Section> sList) {
		this.sectionList = sList; 
	}

	@XmlElementWrapper
	@XmlElement(name="section")
	public List<Section> getSectionList() {
		return sectionList;
	}

	@XmlTransient
	public List<Entry<String,Integer>> getWordFrequencyList() {
		if (wordFrequencyList == null) {
			FrequencyListGenerator flg = new FrequencyListGenerator();
			try {
				flg.generateWordFrequencyList(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return wordFrequencyList;
	}

	public void setWordFrequencyList(List<Entry<String,Integer>> wfl) {
        wordFrequencyList = wfl;
	}

	public void setBigramFrequencyList(List<Entry<String,Integer>> bfl) {
        bigramFrequencyList = bfl;
	}

	@XmlTransient
	public List<Entry<String,Integer>> getBigramFrequencyList() {
		if (bigramFrequencyList == null) {
			FrequencyListGenerator flg = new FrequencyListGenerator();
			try {
				flg.generateBigramFrequencyList(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return bigramFrequencyList;
	}

	public void addSection(Section s) {

        if ( sectionList == null ) {
            sectionList = new ArrayList<Section>();
        }

		sectionList.add(s) ;
		s.setQuery(this);
	}
	
	public Query(String t, Topic topic) {
		text = t;
		this.topic = topic;
	}

	public Query(String t) {
		text = t;
	}
	
	public Query() {}

    public void addLink( String l ) {
        if ( linkList == null ) {
            linkList = new ArrayList<String>();
        }

        linkList.add(l);
    }

    @XmlElementWrapper
    @XmlElement(name="link")
    public List<String> getLinkList() {
        return linkList;
    }
    
    public void setLinkList( List<String> l) {
    	linkList = l;
    }

	public void setTopic(Topic t) {
		this.topic = t;
	}
	
	@XmlTransient
	public Topic getTopic() {
		return topic;
	}

}

