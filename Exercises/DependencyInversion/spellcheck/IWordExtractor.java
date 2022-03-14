package spellcheck;

import java.util.List;

public interface IWordExtractor {
    List<String> extract(String content);
}
