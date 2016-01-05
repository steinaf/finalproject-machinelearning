import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class DataFile {

	private static final String DATA_XML = "data/data.xml";

	public static void saveToXml(Data data) throws JAXBException, IOException {

		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(Data.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		// Write to System.out
		//m.marshal(topic, System.out);

		// Wipe Data File
		File f = new File(DATA_XML);
		f.delete();
		// Write to File
		m.marshal(data, new File(DATA_XML));
	}

	public static Data loadFromXml() throws JAXBException, IOException {
		// get variables from our xml file

		JAXBContext context = JAXBContext.newInstance(Data.class);
		Unmarshaller um = context.createUnmarshaller();

		Data data = (Data) um.unmarshal(new FileReader(DATA_XML));

		for ( Topic topic : data.getTopicList()) {
			if ( topic.getQueryList() == null ) continue;
			
			for (Query q : topic.getQueryList()) {
				q.setTopic(topic);
				
				if (q.getSectionList() == null) continue;

				for (Section s : q.getSectionList()) {
					s.setQuery(q);
				}
			}
		}
		return data;
	}
} 