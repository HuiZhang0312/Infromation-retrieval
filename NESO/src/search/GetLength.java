package search;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import classes.Path;

public class GetLength {
	
	public static void main(String[] args) throws Exception{
		GetLength gl = new GetLength();
		//long i = gl.countTerm(Path.Result);
		
		//System.out.println(i);
		
		Map<String,String> voteMap = gl.getVote(Path.DataDir);
		
		for(String id:voteMap.keySet()){
			String vote = voteMap.get(id);
			System.out.println(id+" "+vote);
		}
	}
	
	
	  public long countTerm(String filename) throws Exception{

	        InputStream fis = null;
	        BufferedReader reader = null;        
	         
	        //read sample.txt
	        //fis = new FileInputStream(filename);
	        fis = this.getClass().getClassLoader().getResourceAsStream(filename);
	        reader = new BufferedReader(new InputStreamReader(fis));
	        int count = 0;
	     
	        //Reading File line by line using BufferedReader
	        //You can get next line using reader.readLine() method.
	        String line = reader.readLine();

	         while(line != null){
	        	 String[] str = reader.readLine().split(" ");
	        	 count += str.length;
	        	 line = reader.readLine();
	        
	         }

	          reader.close();
	          fis.close();
	          return count;

	    }
	  
	  public Map<String, String> getVote(String file) throws Exception{
		  	InputStream fis = null;
	        BufferedReader reader = null;        
	         
	        //read sample.txt
	        //fis = new FileInputStream(file);
	        fis = this.getClass().getClassLoader().getResourceAsStream(file);
	        reader = new BufferedReader(new InputStreamReader(fis)); 
	        Map<String, String> map = new HashMap<String, String>();
	        String line = reader.readLine();
	        while(line!=null){
	        	String docno = line;
	        	//System.out.print(docno+" ");
	        	String vote = reader.readLine();
	        	//System.out.println(vote);
	        	reader.readLine();
	        	reader.readLine();
	        	reader.readLine();
	        	line = reader.readLine();
	        	map.put(docno,vote);

	        } 

	        reader.close();
	        fis.close();
	        return map;  
	  }
}
