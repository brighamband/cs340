
package spellcheck;

import java.io.IOException;
import java.net.URL;
import java.util.SortedMap;


public class Main {

	public static void main(String[] args) {
	
		try {
			// Make fetcher
			IFetcher fetcher = new URLFetcher();
			// Make extractor
			IWordExtractor extractor = new WordExtractor();
			// Make dictionary
			IDictionary dictionary = new Dictionary("dict.txt");

			SpellingChecker checker = new SpellingChecker(fetcher, extractor, dictionary);
			SortedMap<String, Integer> mistakes = checker.check(args[0]);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

