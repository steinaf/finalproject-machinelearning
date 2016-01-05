
import java.util.Scanner;

public class ManualLabeller {

	public void run( Data data ) throws NullPointerException {
		Scanner scanner = new Scanner (System.in);
		String input;
		int numSections=0;

		for ( Topic topic : data.getTopicList()) {
			
			System.out.println("------------TOPIC---------------");
			System.out.println( topic.getText() );
			System.out.println("--------------------------------");
			
			for ( Query query : topic.getQueryList() ) {	
				
				// if (query.getSectionList().size() == 0) continue;

				for ( Section section : query.getSectionList() ) {

					System.out.println("Section "+Integer.toString(++numSections)+" : ");
					System.out.println(section.getText());
					System.out.print("Label (y/n) : ");
					input = scanner.next();

					if ( input.charAt(0) == 'y' || input.charAt(0) == 'Y' ) 
						section.setManualLabel( 1 );
					else
						section.setManualLabel( 0 );
				}
			}
		}
	}
}
