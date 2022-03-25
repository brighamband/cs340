package spellcheck.src.mocks;

import spellcheck.src.IWordExtractor;

import java.util.ArrayList;
import java.util.List;

public class MockWordExtractor implements IWordExtractor {

    public List<String> extract(String content) {
        List<String> words = new ArrayList<>();

        words.add("hello");
        words.add("how");
        words.add("are");
        words.add("you");
        words.add("goodbye");

        return words;
    }
}

