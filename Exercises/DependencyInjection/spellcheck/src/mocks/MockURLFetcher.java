package spellcheck.src.mocks;

import spellcheck.src.IFetcher;

import java.io.IOException;

public class MockURLFetcher implements IFetcher {
    public String fetch(String text) throws IOException {
        return "Here is hard-coded content";
    }
}
