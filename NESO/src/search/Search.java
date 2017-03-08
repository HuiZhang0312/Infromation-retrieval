package search;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classes.Document;
import classes.Path;
import classes.Question;
import indexingLucene.MyIndexReader;

public class Search {
	
	public InputStream is = null;
	public BufferedReader reader = null;
	
	public Search(){
		is = this.getClass().getClassLoader().getResourceAsStream(Path.DataDir);
		reader = new BufferedReader(new InputStreamReader(is));
	}
	
	public List<Question> DoSearch(String query) throws Exception{
		// Open index, initialize the pseudo relevance feedback retrieval model,and extract queries
		MyIndexReader ixreader = new MyIndexReader();
		PseudoRFRetrievalModel PRFSearchModel = new PseudoRFRetrievalModel(ixreader);
		//String query = "java";
		
		List<Document> results = PRFSearchModel.RetrieveQuery(query, 20, 40, 0.4);
			
		Map<String, Question> qMap = new HashMap<String, Question>();
		
		String line = reader.readLine();
		while(line!=null){
			String qid = line;
			String vote = reader.readLine();
			String title = reader.readLine();
			String content = reader.readLine();
			String tagLine = reader.readLine();
			tagLine = tagLine.substring(1,tagLine.length());
			tagLine = tagLine.substring(0,tagLine.length()-1);
			String[] tags = tagLine.split(",");
			
			line = reader.readLine();
			
			Question q = new Question();
			q.setQid(qid.trim());
			//System.out.println(qid);
			
			q.setVote(vote);
			//System.out.println(vote);
			
			q.setTitle(title);
			//System.out.println(title);
			
			q.setContent(content);
			//System.out.println(content);
			
			q.setTags(tags);
			//System.out.println(tagLine);
			
			qMap.put(qid, q);	
			
		}
		
		
		List<Question> qList = new ArrayList<Question>();
		if (results != null) {
			int rank = 1;
			for (Document result : results) {
				
				Question question = qMap.get(result.docno().trim());
				qList.add(question);
				
				System.out.println(" Q0 " + result.docno() + " " + rank + " "
						+ result.score() + " MYRUN");
				rank++;
			}
		}	
		reader.close();
		is.close();
		return qList;
	}
	
	
	public static void main(String[] args) throws Exception{
		Search s = new Search();
		s.DoSearch("java");
	}
}
