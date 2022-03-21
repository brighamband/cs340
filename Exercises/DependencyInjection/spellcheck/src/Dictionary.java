
package spellcheck.src;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class Dictionary implements IDictionary {

	private Set<String> words;

    @Inject
	public Dictionary(@Named("DictionaryFileName") String fileName) throws IOException {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            words = new TreeSet<>();
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                words.add(word);
            }
        }
	}

	public boolean isValidWord(String word) {
		return words.contains(word);
	}
}

