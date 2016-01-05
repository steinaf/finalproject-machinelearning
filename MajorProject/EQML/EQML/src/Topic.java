
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="topic")
@XmlType(propOrder={"text", "queryList"})
public class Topic {
    String text;
    List<Query> queryList;

    public String getText() { 
        return text;
    }

    public void setText( String t ) { 
        text = t ;
    }

    @XmlElement(name="query")
    public List<Query> getQueryList() {
        return queryList;
    }

    public void setQueryList( List<Query> q ) {
        queryList = q;
    }

    public Topic ( String t ) {
        text = t ;
    }
    
    public Topic() {}

    public void addQuery( Query q ) {
        if ( queryList == null ) queryList = new ArrayList<Query>() ;

        queryList.add(q);
        q.setTopic(this);
    }

}
