package preProcess;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import classes.Path;

public class PreProcess {
	
	public static void main(String[] args) throws Exception{
		PreProcess p = new PreProcess();
		p.DoPreProcess();
	}
	
	@SuppressWarnings("unused")
	public void DoPreProcess() throws Exception{

		TextCollection corpus = new TextCollection();
		
		// loading stopword list and initiate the StopWordRemover and WordNormalizer class
		StopWordRemover stopwordRemover = new StopWordRemover();
		WordNormalizer normalizer = new WordNormalizer();
		
		// initiate the BufferedWriter to output result
		File file = new File("E:/Workspaces/MyEclipse 2016 CI/.metadata/.me_tcat7/webapps/IR_Model_Web/WEB-INF/classes/data/result.txt");
		FileWriter wr = new FileWriter(file);

		// initiate a doc object, which can hold document number and document content of a document
		Map<String, Object> doc = null;
		
		int count = 0;
		while((doc = corpus.nextDocument())!=null){
			// load question id of the document
			String docno = doc.keySet().iterator().next();

			// load content
			char[] content = (char[]) doc.get(docno);
			
			// write id into the result file
			wr.append(docno + "\n");
			
			// initiate the WordTokenizer class
			WordTokenizer tokenizer = new WordTokenizer(content);
			
			// initiate a word object, which can hold a word
			char[] word = null;
			
			// process the document word by word iteratively
			while ((word = tokenizer.nextWord()) != null) {
				// each word is transformed into lowercase
				word = normalizer.lowercase(word);

				// filter out stopword, and only non-stopword will be written
				// into result file
				if (!stopwordRemover.isStopword(word))
					wr.append(normalizer.stem(word) + " ");
					//stemmed format of each word is written into result file	
			}
			wr.append("\n");// finish processing one document
			count++;
			//System.out.println(count);
			
		}
		wr.close();
	}
	
}
