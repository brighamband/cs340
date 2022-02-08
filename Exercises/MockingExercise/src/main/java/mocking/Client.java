package mocking;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has code that is correct. (No really, we checked, it is correct.)
 *
 * Unfortunately, this code has a hard coded dependency to a class that has a lot of bugs.
 *
 * If we run unit tests on this class, we should be able to check that its code is correct
 * without running into the bugs in its dependency. To do that, we will need to use mocks.
 */
public class Client {

    /**
     * Creates a string representation of the passed-in value times the number of decimal digits it has
     * @param value the value used for calculation
     * @return a string representation of the value times the number of decimal digits
     */
    public String convertValue(int value) {

        // TODO: This dependency is hard-coded. This should be replaced by a factory method
        Service service = new Service();
        int num_digits = service.getDecimalDigitCount(value);
        int product = value * num_digits;
        return Integer.toString(product);
    }

    /**
     * Converts a string with multiple words into a list of single words that are in all-caps
     * @param sentence the string containing the sentence
     */
    public void createFormattedStrings(String sentence) {
        String[] strings = sentence.split("\\s");
        List<String> capStrings = new ArrayList<>();
        for (String s : strings) {
            capStrings.add(s.toUpperCase());
        }

        // TODO: This dependency is hard-coded. This should be replaced by a factory method
        Service service = new Service();
        service.processList(capStrings);
    }

    // TODO: write a factory method that returns a new Service
}
