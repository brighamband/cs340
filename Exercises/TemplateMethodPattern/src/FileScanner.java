import java.io.File;
import java.io.IOException;
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
    protected String filePattern;
    protected boolean recurse;
    protected Matcher fileMatcher;

    public FileScanner() {}

    public FileScanner(String directory, String filePattern, boolean recurse) {
        this.directory = directory;
        this.filePattern = filePattern;
        this.recurse = recurse;
        this.fileMatcher = Pattern.compile(filePattern).matcher("");
    }

    protected abstract void run();

    protected abstract void setSearchVariables(String searchPat);

    protected String getFileName(File file) {
        try {
            return file.getCanonicalPath();
        }
        catch (IOException e) {
            return "";
        }
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
                    performGeneralFileOperation(file);
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

    protected void performGeneralFileOperation(File file) {
        String fileName = getFileName(file);
        fileMatcher.reset(fileName);
        if (!fileMatcher.find()) {
            return;
        }

        performSpecificFileOperation(file);
    }

    protected abstract void performSpecificFileOperation(File file);

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

    protected boolean parseArgs(String[] args, boolean countLines) {
        int minArgsNeeded = 3;
        if (countLines) {
            minArgsNeeded = 2;
        }

        if (args.length == minArgsNeeded) {
            recurse = false;
            directory = args[0];
            filePattern = args[1];
            fileMatcher = Pattern.compile(filePattern).matcher("");

            if (!countLines) {
                setSearchVariables(args[2]);
            }
        } else if (args.length == (minArgsNeeded + 1) && args[0].equals("-r")) {
            recurse = true;
            directory = args[1];
            filePattern = args[2];
            fileMatcher = Pattern.compile(filePattern).matcher("");

            if (!countLines) {
                setSearchVariables(args[3]);
            }
        } else {
            return false;
        }

        return true;
    }
}
