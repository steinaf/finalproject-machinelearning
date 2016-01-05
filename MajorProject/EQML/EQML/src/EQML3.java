// Deprecated. Use manual model gen

import java.io.*;

public class EQML3 {
	private static final String DATA_PATH = "data/";
	private static final String DATA_FILENAME = "svmData";
	
	public static void main (String args[]) throws IOException {
		
		// Generate Model with optimal c and g parameters
		// tools/easy.py TRAININGFILE 
		
		String[] predictorArgs = new String[] { DATA_PATH+DATA_FILENAME+".scale" , DATA_PATH+DATA_FILENAME+".model" , DATA_PATH+DATA_FILENAME+".output" };		
		svm_predict.main(predictorArgs); 	

		//Load SVM generated labels into data and print output to output file
		try {
			Data data = DataFile.loadFromXml();
			
			BufferedReader br = new BufferedReader(new FileReader(DATA_PATH+DATA_FILENAME+".output"));
			FileWriter fstream1 = new FileWriter(DATA_PATH+"output",true); 
			BufferedWriter out1 = new BufferedWriter(fstream1);

			for (Topic topic : data.getTopicList()) {
				for (Query query : topic.getQueryList()) {
					for (Section section : query.getSectionList()) {
						if (br.readLine().equals("1.0"))
						{
							out1.write(section.getText()); 
							out1.newLine();
							section.setSvmLabel(1);
						}
					}
				}
			}
			fstream1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
