import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Example input: 'java FileSearch -r . ".*\.java" void'
 */

public class Parent {
    protected String directory;  // Name of directory
    protected String pattern;    // File pattern
    protected boolean recurse;
    protected Matcher fileMatcher;   // File matcher

    public Parent(String directory, String pattern, boolean recurse) {
        this.directory = directory;
        this.pattern = pattern;
        this.recurse = recurse;
        this.fileMatcher = Pattern.compile(pattern).matcher("");
    }

    protected static void usage(String usageStr) {
        System.out.println("USAGE: " + usageStr);
    }
}
