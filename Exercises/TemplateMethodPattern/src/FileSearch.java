import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends Parent {

	private Matcher searchMatcher;
	private int totalMatches;

	public FileSearch(String directory, String pattern, String searchPattern, boolean recurse) {
		super(directory, pattern, recurse);
		searchMatcher = Pattern.compile(searchPattern).matcher("");
		totalMatches = 0;
		
		searchDirectory(new File(directory));
		
		System.out.println("");
		System.out.println("TOTAL MATCHES: " + totalMatches);
	}
	
	private void searchDirectory(File dir) {
		if (!dir.isDirectory()) {
			nonDir(dir);
			return;
		}
		
		if (!dir.canRead()) {
			unreadableDir(dir);
			return;
		}
		
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				if (file.canRead()) {
					searchFile(file);
				}
				else {
					unreadableFile(file);
				}
			}
		}
		
		if (recurse) {
			for (File file : dir.listFiles()) {
				if (file.isDirectory()) {
					searchDirectory(file);
				}
			}
		}
	}
	
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
				unreadableFile(file);
			}
		}
	}
	
	private void nonDir(File dir) {
		System.out.println(dir + " is not a directory");
	}
	
	private void unreadableDir(File dir) {
		System.out.println("Directory " + dir + " is unreadable");
	}
	
	private void unreadableFile(File file) {
		System.out.println("File " + file + " is unreadable");
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
		
		new FileSearch(dirName, filePattern, searchPattern, recurse);
	}
	
	private static void usage() {
		usage("java FileSearch {-r} <dir> <file-pattern> <search-pattern>");
	}

}
