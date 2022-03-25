package spellcheck.src.mocks;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import spellcheck.src.IDictionary;
import spellcheck.src.IFetcher;
import spellcheck.src.IWordExtractor;
import spellcheck.src.mocks.MockDictionary;
import spellcheck.src.mocks.MockURLFetcher;
import spellcheck.src.mocks.MockWordExtractor;

public class MockModule extends AbstractModule {
    @Override
    protected void configure() {
        // Bind fetcher
        bind(IFetcher.class).to(MockURLFetcher.class);
        // Bind extractor
        bind(IWordExtractor.class).to(MockWordExtractor.class);
        // Bind dictionary
        bind(IDictionary.class).to(MockDictionary.class);
    }

    @Provides
    @Named("DictionaryFileName")
    public String dictionaryFilePath() {
        return "dict.txt";
    }
}
