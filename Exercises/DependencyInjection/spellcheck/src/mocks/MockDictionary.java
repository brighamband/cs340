package spellcheck.src.mocks;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import spellcheck.src.IDictionary;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MockDictionary implements IDictionary {

    private Set<String> words;

    @Inject
    public MockDictionary(@Named("DictionaryFileName") String fileName) throws IOException {
        words = new TreeSet<>();
        words.add("hello");
        words.add("how");
        words.add("are");
        words.add("you");
        words.add("goodbye");
    }

    public boolean isValidWord(String word) {
        return words.contains(word);
    }
}
