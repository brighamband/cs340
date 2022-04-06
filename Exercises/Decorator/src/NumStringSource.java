import java.nio.charset.Charset;
import java.util.Random;

public class NumStringSource implements IStringSource {
    String nums = "0123456789";
    int currIdx = 0;

    @Override
    public String next() {
        if (currIdx < 10) {
            String next = nums.substring(currIdx, currIdx + 4);
            currIdx++;
            return next;
        }
        return null;
    }
}
