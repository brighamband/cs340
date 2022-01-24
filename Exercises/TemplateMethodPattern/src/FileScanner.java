import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Example input: 'java FileSearch -r . ".*\.java" void'
 */

public abstract class FileScanner {
    // Constants
    protected final static int NON_DIR = 0;
    protected final static int UNREADABLE_DIR = 1;
    protected final static int UNREADABLE_FILE = 2;

    protected String directory;  // Name of directory
    protected String pattern;    // File pattern
    protected boolean recurse;
    protected Matcher fileMatcher;   // File matcher

    public FileScanner(String directory, String pattern, boolean recurse) {
        this.directory = directory;
        this.pattern = pattern;
        this.recurse = recurse;
        this.fileMatcher = Pattern.compile(pattern).matcher("");
    }

    protected abstract void run();

    protected void openFile() {

    }

    protected void processDirectory(File dir) {
        if (!dir.isDirectory()) {
            printError(dir, NON_DIR);
            return;
        }

        if (!dir.canRead()) {
            printError(dir, UNREADABLE_DIR);
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                if (file.canRead()) {
                    performFileOperation(file);
                }
                else {
                    printError(file, UNREADABLE_FILE);
                }
            }
        }

        if (recurse) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    processDirectory(file);
                }
            }
        }
    }

    protected abstract void performFileOperation(File file);

    protected void printError(File fileOrDir, int errorType) {
        if (errorType == NON_DIR) {
            System.out.println(fileOrDir + " is not a directory");
        } else if (errorType == UNREADABLE_DIR) {
            System.out.println("Directory " + fileOrDir + " is unreadable");
        } else if (errorType == UNREADABLE_FILE) {
            System.out.println("File " + fileOrDir + " is unreadable");
        }
    }

    protected void printResults(String resultsStr) {
        System.out.println(resultsStr);
    }

    protected static void usage(String usageStr) {
        System.out.println("USAGE: " + usageStr);
    }
}
