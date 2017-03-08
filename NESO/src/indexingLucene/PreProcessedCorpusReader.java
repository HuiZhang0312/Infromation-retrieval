package indexingLucene;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import classes.Path;

public class PreProcessedCorpusReader {
	private BufferedReader br;
	private InputStream instream_collection;
	private InputStreamReader is;
	public PreProcessedCorpusReader() throws IOException {
		// This constructor should open the file in Path.DataTextDir
		// and also should make preparation for function nextDocument()
		// remember to close the file that you opened, when you do not use it any more
		
		//instream_collection = new FileInputStream(Path.Result);
		instream_collection = this.getClass().getClassLoader().getResourceAsStream(Path.Result);
		
		is = new InputStreamReader(instream_collection);
        br = new BufferedReader(is);   
	}
	

	public Map<String, String> nextDocument() throws IOException {
		String docno=br.readLine();
		if(docno==null) {
			instream_collection.close();
			is.close();
			br.close();
			return null;
		}
		String content =br.readLine();
		Map<String, String> doc = new HashMap<String, String>();
		doc.put(docno, content);
		return doc;
	}
}
