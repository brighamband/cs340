import java.util.ArrayList;
import java.util.List;

public class AbcStringSource implements IStringSource {
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    int currIdx = 0;

    @Override
    public String next() {
        if (currIdx < 26) {
            String next = alphabet.substring(currIdx, currIdx + 5);
            currIdx++;
            return next;
        }
        return null;
    }
}
