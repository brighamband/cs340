package spellcheck.src;

import java.io.IOException;

public interface IFetcher {
    String fetch(String text) throws IOException;
}
