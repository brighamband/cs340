import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends FileScanner {

	private Matcher searchMatcher;
	private int totalMatches;

	public FileSearch(String directory, String pattern, String searchPattern, boolean recurse) {
		super(directory, pattern, recurse);
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
		
		String dirName = "";
		String filePattern = "";
		String searchPattern = "";
		boolean recurse = false;
		
		if (args.length == 3) {
			recurse = false;
			dirName = args[0];
			filePattern = args[1];
			searchPattern = args[2];
		}
		else if (args.length == 4 && args[0].equals("-r")) {
			recurse = true;
			dirName = args[1];
			filePattern = args[2];
			searchPattern = args[3];
		}
		else {
			usage();
			return;
		}
		
		FileSearch fileSearcher = new FileSearch(dirName, filePattern, searchPattern, recurse);
		fileSearcher.run();
	}
}
