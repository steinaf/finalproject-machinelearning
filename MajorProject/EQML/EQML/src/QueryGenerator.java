
import java.util.*;


public class QueryGenerator {
	private ArrayList<Query> queryList;

	List<String> suffixList = Config.getQuerySuffixList();

	ArrayList<Query> generateQueryList(String topic) {
		queryList = new ArrayList<Query>();

		for ( String suffix : suffixList) {
			queryList.add( new Query(topic+" "+suffix) );
		}
		return queryList;
	}
}
