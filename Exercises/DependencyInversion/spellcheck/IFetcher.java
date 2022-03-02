package spellcheck;

import java.io.IOException;

public interface IFetcher {
    String fetch(String text) throws IOException;
}
