package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import classes.Document;
import classes.Path;
import indexingLucene.MyIndexReader;
import search.QueryRetrievalModel;

public class PseudoRFRetrievalModel {
	MyIndexReader ixreader;
	
	// According to slides, most used u value is 2000
	public double u = 2000;
	
	// Initial total length of all relevant documents
	public int totalDocLen = 0;

	public long collectionLen;
	
	public PseudoRFRetrievalModel(MyIndexReader ixreader) throws Exception{
		this.collectionLen = new GetLength().countTerm(Path.Result);
		this.ixreader=ixreader;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Document> RetrieveQuery(String aQuery, int TopN, int TopK, double alpha) throws Exception {	
		// this method will return the retrieval result of the given Query, and this result is enhanced with pseudo relevance feedback
		// (1) you should first use the original retrieval model to get TopK documents, which will be regarded as feedback documents
		// (2) implement GetTokenRFScore to get each query token's P(token|feedback model) in feedback documents
		// (3) implement the relevance feedback model for each token: combine the each query token's original retrieval score P(token|document) with its score in feedback documents P(token|feedback model)
		// (4) for each document, use the query likelihood language model to get the whole query's new score, P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')
		
		// Implement of comparator class which is used to sort (according to TA session)
		Comparator<Document> comparator = new Comparator<Document>(){
			public int compare(Document d1,Document d2){
				if(d1.score()<=d2.score()){
					return 1;
				}else{
					return -1;
				} 					
			}		
		};		

		// Get P(token|feedback documents)
		HashMap<String,Double> TokenRFScore = GetTokenRFScore(aQuery,TopK);

		// Get query content
		String[] queryTokens = aQuery.split(" ");

		// Token info
		HashMap<String,HashMap<Integer, Integer>> tokenInfo = new HashMap<String,HashMap<Integer,Integer>>();
		
		// Store relevant docs' docid
		HashSet<Integer> relevantDocs=new HashSet<Integer>();
		
		// Sort all retrieved documents from most relevant to least, and return TopN
		List<Document> results = new ArrayList<Document>();

		for(String token:queryTokens){
			// Get token posting list
			int[][] postingList = ixreader.getPostingList(token);

			// Used to store info(docid,freq) of each token in each doc
			HashMap<Integer,Integer> info=new HashMap<Integer,Integer>();

			// Here need to judge whether the posting list is null
			// As the query token may not appear in collection
			if(postingList!=null){
				for(int i = 0;i<postingList.length;i++){
					// Get value from posting list
					int docid = postingList[i][0];
					int freq = postingList[i][1];

					// Add docid into relevant docs
					relevantDocs.add(docid);
						
					// Put docid and frequency into info
					info.put(docid, freq);
						
					// Put token and its info into tokenInfo
					tokenInfo.put(token,info);	
				}
			}
		}

		// Calculate scores for each relevant doc
		// Use iterator to go through all relevant docs
		Iterator iterator = relevantDocs.iterator();
		while(iterator.hasNext()){
			// Initial score
			double score = 1;

			// Get docid
			int docid = Integer.parseInt(iterator.next().toString());
			
			// Get docno
			String docno = ixreader.getDocno(docid);
			
			// Get doc length
			int docLen = ixreader.docLength(docid);
			
			
			for(String token:queryTokens){
				if(tokenInfo.containsKey(token)){
					// Get info(docid, freq) of that token
					HashMap<Integer, Integer> info1 = tokenInfo.get(token);
					
					// Get collection frequency
					long collectionFreq = ixreader.CollectionFreq(token);
					
					// Get frequency of that token
					// Need to judge whether doc has that token before computing, cause it may be null
					if(info1.get(docid)!=null){
						int tokenFreq=Integer.parseInt(info1.get(docid).toString());
						// Calculate origin score of token
						double oldScore = (tokenFreq + u*collectionFreq/collectionLen)/(docLen + u);
							
						// Calculate new score with formula on slides page 33
						double newScore = alpha*oldScore + (1-alpha)*TokenRFScore.get(token);
							
						// Get the score of whole query
						score *= newScore;	
					}
				}
			}	
			
			GetLength gl = new GetLength();
			Map<String,String> voteMap = gl.getVote(Path.DataDir);
			int vote = Integer.parseInt(voteMap.get(docno));
			//System.out.println((double)1/vote);
			if(vote<0){
				vote = 0;
			}
			
			score = 1/(1/score+(double)1/vote);
			
			
			// Only record scores larger than 0, which is meaningful
			if(score>0){
				results.add(new Document(Integer.toString(docid),docno,score));
			}
		}

		// Sort the output
		Collections.sort(results,comparator);
		
		results = results.subList(0, TopN);
		
		return results;
}
	
	
	/**
	 * Calculate relevant feedback score for each token in aQuery 
	 * 
	 * @param aQuery
	 * @param TopK 
	 * @return 
	 * @throws Exception
	 * 
	 * */
	public HashMap<String,Double> GetTokenRFScore(String aQuery,  int TopK) throws Exception{
		// for each token in the query, you should calculate token's score in feedback documents: P(token|feedback documents)
		// use Dirichlet smoothing

		// Index reader for original model
		MyIndexReader oixreader = new MyIndexReader();

		// Get Top K documents (feedback documents) with original model
		List<Document> originResults = new QueryRetrievalModel(oixreader).retrieveQuery(aQuery, TopK);

		// Get query content
		String[] queryTokens = aQuery.split(" ");

		// As all feedback documents are treated as one big pseudo document
		// We need to get total length of all Top K relevant documents in originResults
		for(Document doc:originResults){
			// All document in originalResults have score > 0
			int docid =  Integer.parseInt(doc.docid());
			int length = oixreader.docLength(docid);
			totalDocLen += length;
		} 


		// Calculate each token's score in feedback documents
		// Save <token, score> in HashMap TokenRFScore, and return it
		HashMap<String,Double> TokenRFScore=new HashMap<String,Double>();

		for(String token:queryTokens){
			// Get token collection frequency
			int collectionFreq = (int) oixreader.CollectionFreq(token);
			
			// Get posting list
			int[][] postingList = oixreader.getPostingList(token);

			// Initial total frequency of token
			int totalFreq = 0;

			// Here need to judge whether the posting list is empty
			if(postingList!=null){
				for(int i = 0;i<postingList.length;i++){
					 totalFreq += postingList[i][1];
				}
			}

			// Calculate score
			double score=(totalFreq+u*collectionFreq/collectionLen)/(totalDocLen+u);

			// Save <token, score> in HashMap TokenRFScore
			// Only save score > 0, which is meaningful
			if(score > 0){
				TokenRFScore.put(token, score);	
			}
			

		}

		// Close index reader for original model
		oixreader.close();
		
		return TokenRFScore;
	}
	
}
