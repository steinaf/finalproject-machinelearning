
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="data")
public class Data {
    List<Topic> topicList = new ArrayList<Topic>();

    @XmlElement(name="topic")
    public List<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList( List<Topic> t ) {
        topicList = t;
    }

    public void addTopic( Topic t ) {
        topicList.add(t);
    }
}
