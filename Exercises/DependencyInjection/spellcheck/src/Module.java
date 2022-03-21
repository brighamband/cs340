package spellcheck.src;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class Module extends AbstractModule {
    @Override
    protected void configure() {
        // Bind fetcher
        bind(IFetcher.class).to(URLFetcher.class);
        // Bind extractor
        bind(IWordExtractor.class).to(WordExtractor.class);
        // Bind dictionary
        bind(IDictionary.class).to(Dictionary.class);
    }

    @Provides
    @Named("DictionaryFileName")
    public String dictionaryFilePath() {
        return "dict.txt";
    }
}
