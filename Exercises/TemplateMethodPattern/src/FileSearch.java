import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends FileScanner {
	private String searchPattern;
	private Matcher searchMatcher;
	private int totalMatches;

	public FileSearch() {
		totalMatches = 0;
	};

	public FileSearch(String directory, String filePattern, String searchPattern, boolean recurse) {
		super(directory, filePattern, recurse);
		searchMatcher = Pattern.compile(searchPattern).matcher("");
		totalMatches = 0;
	}

	protected void run() {
		processDirectory(new File(directory));
		printResults("TOTAL MATCHES: " + totalMatches);
	}

	protected void performFileOperation(File file) {
		searchFile(file);
	}

	private static void usage() {
		usage("java FileSearch {-r} <dir> <file-pattern> <search-pattern>");
	}

	protected void setSearchVariables(String searchPat) {
		searchPattern = searchPat;
		searchMatcher = Pattern.compile(searchPat).matcher("");
	}

	// FileSearch specific methods

	private void searchFile(File file) {
		String fileName = "";
		
		try {
			fileName = file.getCanonicalPath();
		}
		catch (IOException e) {
		}
		
		fileMatcher.reset(fileName);
		if (fileMatcher.find()) {
			try {
				int curMatches = 0;

				InputStream data = new BufferedInputStream(new FileInputStream(file));
				try {
					Scanner input = new Scanner(data);
					while (input.hasNextLine()) {
						String line = input.nextLine();
						
						searchMatcher.reset(line);
						if (searchMatcher.find()) {
							if (++curMatches == 1) {
								System.out.println("");
								System.out.println("FILE: " + file);
							}
							
							System.out.println(line);
							++totalMatches;
						}
					}
				}
				finally {
					data.close();
					
					if (curMatches > 0) {
						System.out.println("MATCHES: " + curMatches);
					}
				}
			}
			catch (IOException e) {
				printError(file, UNREADABLE_FILE);
			}
		}
	}

	public static void main(String[] args) {
		FileSearch fileSearcher = new FileSearch();

		if (!fileSearcher.parseArgs(args, false)) {
			usage();
			return;
		}

		fileSearcher.run();
	}
}
