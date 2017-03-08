package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import classes.Document;
import classes.Path;
import indexingLucene.MyIndexReader;


public class QueryRetrievalModel{
	protected MyIndexReader indexReader;
	// Accoring to slides, most common value is 2000
	public double u = 2000;
	public long collectionLength;
	
	public QueryRetrievalModel(MyIndexReader ixreader) throws Exception {
		this.collectionLength = new GetLength().countTerm(Path.Result);
		indexReader = ixreader;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Document> retrieveQuery( String aQuery, int TopN ) throws IOException {
		// NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
		// implement your retrieval model here, and for each input query, return the topN retrieved documents
		// sort the docs based on their relevance score, from high to low
		
		// Implement of comparator class
		// According to TA assignment session 2
		Comparator<Document> comparator = new Comparator<Document>(){
			public int compare(Document d1,Document d2){
				if(d1.score()!=d2.score()){
					return d1.score()<d2.score()?1:-1;
				}else{
					return 1;
				} 					
			}		
		};		
		
		//System.out.println("aQuery:"+aQuery);
		//System.out.println("TopKTopN:"+TopN);
		
		// Get query content
		String[] tokens = aQuery.split(" "); 
		System.out.println("tokens"+tokens.length);
		
		// Output
		List<Document> output = new ArrayList<Document>();
		
		// Token info
		HashMap<String,HashMap> tokenInfo = new HashMap<String,HashMap>();
		
		// Relevant docs
		HashSet<Integer> docs=new HashSet<Integer>();
		
		for(String token:tokens){
			
			HashMap<Integer,Integer> info=new HashMap<Integer,Integer>();
			
			// Get posting list
			int[][] post = indexReader.getPostingList(token);
			
			// Has posting list
			if(post!=null){
				// And list has content
				if(post.length!=0){
					for(int i=0;i<post.length;i++){
						int docid=post[i][0];
						int freq=post[i][1];
						// Put docid and frequency into map
						info.put(docid, freq);
						
						// Put token and its info into tokenInfo
						tokenInfo.put(token,info);
						
						// Add docid into relevant docs
						docs.add(docid);	
					}
				}						
			}			
		}
	
		System.out.println("finish relevant doc");
		System.out.println("doc size:"+docs.size());
		
		// Calculate scores for each relevant doc
		Iterator it=docs.iterator();
		while(it.hasNext()){
			// Get docid
			int docid = Integer.parseInt(it.next().toString());
			
			// Get docno
			String docno =indexReader.getDocno(docid);
			
			// Get doc length
			int docLength = indexReader.docLength(docid);
			
			// Initial score each time
			double score = 1;
			
			for(String token : tokens){
				if(tokenInfo.containsKey(token)){
					// Get collection frequency
					long cf = indexReader.CollectionFreq(token);
					
					// Get info of that token
					HashMap info = tokenInfo.get(token);
					
					// Get frequency of that token
					// Need to judge whether map has that docid before computing, cause it may be null
					if(info.get(docid)!=null){
						int freq=Integer.parseInt(info.get(docid).toString());
						// Calculate probability and score according to formula
						double p = (freq+u*cf/collectionLength)/(docLength+u);
						score *= p;
					}
				}
			}	
			// score = 1/(1/score+1/vote);
			// Only record scores larger than 0, which is meaningful
			if(score>0){
				output.add(new Document(Integer.toString(docid),docno,score));
			}
		}
	
		System.out.println("finish calculate score");
		// Sort the output
		Collections.sort(output,comparator);
		
		// Return top n relevant documents list
		System.out.println("sublist size:"+output.subList(0, TopN).size());
		return output.subList(0, TopN);
	}

}
