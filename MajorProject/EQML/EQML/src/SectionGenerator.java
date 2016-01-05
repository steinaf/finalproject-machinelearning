
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import spiaotools.SentParDetector;

public class SectionGenerator {

	private static final Pattern PARAGRAPH_REGEX = Pattern.compile("<p>(.+?)</p>");
	private static final Pattern SENTENCE_REGEX = Pattern.compile("<s>(.+?)</s>");

	public List<Section> generateSections(String text) {

		System.out.println("Inside SectionGenerator");
		String paragraph, newParagraph="", sentence;
		List<String> paragraphList, sentenceList, sentenceListMod;
		int noSentences, length, i, j, k;

		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.UK);

		List<Section> sectionList = new ArrayList<Section>() ;

		SentParDetector object = new SentParDetector() ;
		String taggedText = object.markupRawText(3, text);

		taggedText =  taggedText.replace("\n", " ").replace("\r", " ");
		paragraphList = getTagValues(taggedText, PARAGRAPH_REGEX);

		i=0;
		while(i < paragraphList.size()){

			paragraph = paragraphList.remove(i);
			sentenceList = getTagValues(paragraph, SENTENCE_REGEX);

			j=0;
			while(j<sentenceList.size()) {

				if(sentenceList.get(j).length()>400) {

					sentence = sentenceList.remove(j);
					sentenceListMod = getSentenceList(iterator, sentence);

					k=0;
					while(k<sentenceListMod.size()) {

						sentence = sentenceListMod.get(k);
						sentenceList.add(j+k, sentence);
						k++;
					}
					j = j+k;
					continue;
				}
				j++;
			}

			noSentences = sentenceList.size();
			length=0;k=0;
			while(k<noSentences) {
				length=length+sentenceList.get(k).trim().length();
				k++;
			}

			if((length > 500 && noSentences>3) || noSentences >=6 ) {

				k=0;
				paragraph=""; newParagraph="";
				while(k<noSentences/2) {
					paragraph=paragraph + "<s>" + sentenceList.get(k) + "</s>";
					k++;
				}

				while(k<noSentences) {
					newParagraph = newParagraph + "<s>" + sentenceList.get(k) + "</s>";
					k++;
				}

				paragraphList.add(i, paragraph);
				paragraphList.add(i+1, newParagraph);
				continue;
			}

			k=0; paragraph = ""; length=0;
			while(k<noSentences) {
				paragraph=paragraph+sentenceList.get(k).trim()+"\n";
				k++;
			}

			paragraphList.add(i, paragraph);
			i++;

		}

		k=0;
		while(k<paragraphList.size()) {

			Section s = new Section();
			paragraph = paragraphList.get(k);
			if(paragraph.length()>20) {
				s.setText(paragraph);
				sectionList.add(s);
			}
			k++;
		}

		return sectionList;
	}

	private static List<String> getTagValues(final String text, final Pattern regexPattern) {
		final List<String> tagValues = new ArrayList<String>();
		final Matcher matcher = regexPattern.matcher(text);
		while (matcher.find()) {
			tagValues.add(matcher.group(1).replaceAll("<s n=\".+?\">", "<s>"));
		}
		return tagValues;
	}

	private static List<String> getSentenceList(BreakIterator bi, String source) {
		bi.setText(source);
		List<String> sentences =  new ArrayList<String>();

		int lastIndex = bi.first();
		while (lastIndex != BreakIterator.DONE) {
			int firstIndex = lastIndex;
			lastIndex = bi.next();

			if (lastIndex != BreakIterator.DONE) {
				String sentence = source.substring(firstIndex, lastIndex);
				sentences.add(sentence);
			}
		}
		return sentences;
	}
}