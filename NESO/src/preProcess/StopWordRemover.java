package preProcess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import classes.Path;

public class StopWordRemover {
	// Initialzie fileinputstream and bufferedreader
		public InputStream fis = null;
		public BufferedReader reader = null;
		public Set<String> stops = new HashSet<String>();

		public StopWordRemover() throws IOException {

			//fis = new FileInputStream(Path.StopwordDir);
			fis = this.getClass().getClassLoader().getResourceAsStream(Path.StopwordDir);
			reader = new BufferedReader(new InputStreamReader(fis));
			// read in the stop list line by line
			String line = reader.readLine();
			while (line != null) {
				// eliminate the whitespace at the end of each line
				stops.add(line.trim());
				line = reader.readLine();
			}

			//Close the reader and fis
			reader.close();
			fis.close();
		}


		public boolean isStopword(char[] word) {
			// return true if the input word is a stopword, or false if not
			return stops.contains(String.valueOf(word));
		}
}
