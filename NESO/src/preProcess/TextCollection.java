package preProcess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import classes.Path;

public class TextCollection {
	// Initialzie fileinputstream and bufferedreader
	public InputStream is = null;
	public BufferedReader reader = null;
	
	public TextCollection() throws IOException{
		// Read file in Path.DataTextDir
		//InputStream filetag = this.getClass().getClassLoader().getResourceAsStream(Path.TagsDir);
		is = this.getClass().getClassLoader().getResourceAsStream(Path.DataDir);
		//fis = new FileInputStream(Path.DataDir);
		reader = new BufferedReader(new InputStreamReader(is));
	}
	
	public Map<String, Object> nextDocument() throws IOException{
		
		String line = reader.readLine();
		String docno  = null;
		String content = null;
		String tags = null;
		
		//Use map to store question id and content
		Map<String, Object> map = new HashMap<String, Object>();
		
		while(line!=null){
			docno = line;
			reader.readLine();//on vote line
			content =reader.readLine()+" ";// on title line
			content += reader.readLine()+" ";//on content line
			tags = reader.readLine();//on tags line
			//System.out.println(tags);
			tags = tags.substring(1,tags.length());
			tags = tags.substring(0, tags.length()-1);
			content += tags;
			map.put(docno, content.toCharArray());
			
			// get next question id
			//line = reader.readLine();
			return map;
			
		}
		
		reader.close();
		is.close();
		
		return null;
	}

}
