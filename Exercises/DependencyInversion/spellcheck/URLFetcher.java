
package spellcheck;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class URLFetcher implements IFetcher {

	public String fetch(String text) throws IOException {
        URL url = new URL(text);

		URLConnection connection = url.openConnection();
		
		StringBuilder contentBuffer = new StringBuilder();

        try (InputStream input = connection.getInputStream()) {
            int c;
            while ((c = input.read()) >= 0) {
                contentBuffer.append((char) c);
            }
        }
	
		return contentBuffer.toString();
	}
}

