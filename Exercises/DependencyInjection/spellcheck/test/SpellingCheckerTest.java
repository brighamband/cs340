package spellcheck.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import spellcheck.src.*;
import spellcheck.src.Module;

import java.io.IOException;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpellingCheckerTest {

    @Test
    public void testMain() {
        final String url = "https://pastebin.com/raw/t6AZ5kx3";

        try {
            Injector injector = Guice.createInjector(new Module());
            SpellingChecker checker = injector.getInstance(SpellingChecker.class);

            SortedMap<String, Integer> mistakes = checker.check(url);
            System.out.println(mistakes);
            assertEquals(4, mistakes.size());
            assertTrue(mistakes.containsKey("be"));
            assertTrue(mistakes.containsKey("doesn"));
            assertTrue(mistakes.containsKey("funtion"));
            assertTrue(mistakes.containsKey("t"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
