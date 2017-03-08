package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import classes.*;

public class ExtractQuestion {
	private String line = "";
	private String line1 = "";
	BufferedReader reader = null;
	BufferedReader reader2 = null;
	int qid = 0;
	FileInputStream filetag = null;
	BufferedReader readertag = null;
	
	public String full_url;
	String linetag="";
	
	//String originLanguage = null;
	
	File file = new File("E:/Workspaces/MyEclipse 2016 CI/.metadata/.me_tcat7/webapps/IR_Model_Web/WEB-INF/classes/data/Questions.txt");
	
	
	public ExtractQuestion(String segment) throws IOException{
		
		try {
			//this.originLanguage = originLanguage;
			
			InputStream filetag = this.getClass().getClassLoader().getResourceAsStream(Path.TagsDir);
			readertag = new BufferedReader(new InputStreamReader(filetag));
			linetag = readertag.readLine().trim();
		
			//System.out.println(linetag);

			FileWriter fw = new FileWriter(file);
			fw.write("");
			fw.close();
			
			readertag.close();
			filetag.close();
		} catch (IOException e) {
			System.out.println("ERROR: cannot load alltags file.");
		}
		
		
		
		String new_segment = segment.trim();
		if(!isTag(new_segment)){
			if(new_segment.contains(" ")){
				String[] searchWords = new_segment.split(" ");
				int n = searchWords.length;
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i<n-1;i++){
					sb.append(searchWords[i] + "%20");
				}
				sb.append(searchWords[n-1]);
				String search = sb.toString().trim();
				//full_url = "http://stackoverflow.com/"+"search?q="+search;
				full_url = "http://stackoverflow.com/search?tab=relevance&pagesize=50&q="+search;    
			}else{
				full_url = "http://stackoverflow.com/search?tab=relevance&pagesize=50&q="+new_segment;
				//http://stackoverflow.com/search?tab=relevance&pagesize=50&q=homework
			}
	    }
		else{
			 full_url = "http://stackoverflow.com/questions/tagged/"+new_segment+"?sort=frequent&pageSize=50";
			//full_url="http://stackoverflow.com/questions/tagged/c%2b%2b?sort=frequent&pageSize=50"; //for c++
		}
		//System.out.println(full_url);
		Document doc = Jsoup.connect(full_url).get();
		String s =doc.body().toString();
	    reader = new BufferedReader(new StringReader(s));
		 // used to print source code	
//	    StringBuilder response = new StringBuilder();
//        while((line=reader.readLine())!=null){
//        	response.append(line + "\n");
//        }
//        reader.close();
//        System.out.println(response.toString());
	    

	}
	
	public boolean hasNext() throws IOException{
		
		while(!(line = reader.readLine()).contains("</body>")){
			if (line.contains("\"question-summary")){
				return true;
			}
			if(line==null){
				reader.close();
				break;
			}
		}
		return false;
	}
	

	public boolean isTag( String word ) {
		// return true if the input word is a stopword, or false if not
		
		if(linetag.contains(word)){
			return true;}
		else{return false;}
	}
	
	int i=1;
	//get vote, title, tags, contents for each question
	public Question next() throws Exception{
		FileWriter writer = new FileWriter(file,true);
		Question q = new Question();
		String title="";
		String url="";
		String art_url="";
		String vote ="";
		String tag ="";
		StringBuilder response = new StringBuilder();
		int c= 0;
		//Translator t = new Translator();
		
		//<span class="vote-count-post high-scored-post"><strong>1001</strong></span> 
		//<span class="vote-count-post "><strong>818</strong></span> 
       while (!line.contains("<div class=\"started fr\">")){
        	line = reader.readLine();
        	//get vote count
        	if(line.contains("\"vote-count-post ")){
        		int begin = line.indexOf("<strong>");
        		int end = line.indexOf("</strong");
        		vote =line.substring(begin+8,end);
        		//System.out.println(vote);
        		//response.append(vote.trim() + "\n");
        		q.setVote(vote);
        		qid++;
        	}
//<span> <a href="/questions/36286102/how-to-change-source-code-of-page-using-selenium-and-java" data-searchsession="/questions/36286102/how-to-change-source-code-of-page-using-selenium-and-java?s=1|2.0343" title="How to change source code of page? using selenium and java"> Q: How to change source code of page? using selenium and java </a> </span>        	
        	//get title
        	if(line.contains("<div class=\"summary\">")){
        		line = reader.readLine();
        		while(!line.contains("<a href=\"/questions")){
        			line=reader.readLine();
        		}
        		
        		int begin = line.indexOf("\">");
        		int end = line.indexOf("</a>");
        		title = line.substring(begin+2,end);
        		
//        		String[] query = new String[1];
//                query[0] = title;
//                String[] question = t.transString(query, originLanguage);
//                title = question[0];
        		
        		int url_begin = line.indexOf("<a href=");
        		int url_end = line.indexOf("\" ");
        		url = line.substring(url_begin+9,url_end);
        		art_url = "http://stackoverflow.com"+url;
        		//System.out.println(url);
        		
        	}
        	
        	//get tags
        	if(line.contains("<div class=\"tags")){
        		StringBuilder sb = new StringBuilder();
        		while(!(line=reader.readLine()).contains("</div>")){
        			int begin = line.indexOf("show questions tagged");
		    		int end = line.indexOf("rel=\"tag\">");
        			tag = line.substring(begin+23, end-3);
        			sb.append(tag + " ");
        		}
        		String alltags = sb.toString().trim();
        		String[] tags = alltags.split(" ");
        		q.setTags(tags);
        		//System.out.println(tags[1]);
        	}
   	
        q.setQid(qid + "");
       
        q.setTitle(title);
           
        }
     //get content
   	Document doc2 = Jsoup.connect(art_url).get();
   //	Document doc2 = Jsoup.connect("http://stackoverflow.com/questions/2691646/data-mining-textbook").get();
		String ss =doc2.body().toString();
	    reader2 = new BufferedReader(new StringReader(ss));
	    line1 = reader2.readLine();
	    while(!line1.contains("post-taglist")){
	    	line1=reader2.readLine();
	    	if(line1.contains("class=\"postcell\"")){
	    		StringBuilder art_sb = new StringBuilder();
	    		line1=reader2.readLine();
	    		line1=reader2.readLine();
	    		line1=reader2.readLine();
	    		while(!line1.contains("</div>")){
	    		if(line1.contains("<p>")){
	    			if(line1.contains("</a>")){
	    				int link_start = line1.indexOf("<a",0);
	    				int link_end = line1.indexOf("\">",0);
	    				int link_SOend = line1.indexOf("</a>",0);
	    				int p_start = line1.indexOf("<p>");
	    				int p_end = line1.indexOf("</p>");
	    				String segment1 = line1.substring(p_start+3,link_start);
	    				String segment2 = line1.substring(link_end+2,link_SOend);
	    				String segment = segment1+segment2;
	    				art_sb.append(segment+" ");
	    				link_start=line1.indexOf("<a", link_start+1);
	    				
	    				while(link_start!=-1){//second hyperlink and more
	    					link_end = line1.indexOf("\">",link_end+1);
	    					segment1 = line1.substring(link_SOend+4, link_start);
		    				link_SOend = line1.indexOf("</a>",link_SOend+1);
		    				segment2 = line1.substring(link_end+2, link_SOend);
		    				segment = segment1+segment2;
		    				art_sb.append(segment+" ");
		    				link_start=line1.indexOf("<a", link_start+1);
	    				}
	    					segment = line1.substring(link_SOend+4,p_end);
	    					String[] segments = segment.split("<code>|</code>|<strong>|</strong>|<em>|</em>");
	    					for(int i =0;i<segments.length;i++){
	    						art_sb.append(segments[i]+" ");
	    					}
//	    					String art1 = art_sb.toString().trim();
//	    					String[] segments1 = art1.split("</code>");
//	    					for(int j =0;j<segments1.length;j++){
//	    						art_sb1.append(segments1[j]+" ");
//	    					}
	    					//art_sb.append(segment+" ");
	    				//</a>.....</p>			
	    			}else{
	    			
	    				int end = line1.indexOf("</p>");	
	    				int begin = line1.indexOf("<p>");
	    				String art = line1.substring(begin+3, end);
	    				String[] segments = art.split("<code>|</code>|<strong>|</strong>|<em>|</em>");
    					for(int i =0;i<segments.length;i++){
    						art_sb.append(segments[i]+" ");
    					}
//    					String art1 = art_sb.toString().trim();
//    					String[] segments1 = art1.split("</code>");
//    					for(int j =0;j<segments1.length;j++){
//    						art_sb1.append(segments1[j]+" ");
//    					}
	    				//System.out.println(art);
	    				//art_sb.append(art+" ");
	    			}
	    			
	    		}
	    		line1=reader2.readLine();
	    		
	    		}
//	          <div> 
//	          <div class="post-text" itemprop="text"> 
//	           <p>If you followed a DM course, which textbook was used? </p> 
//	           <p>I know about <a href="http://www.cs.waikato.ac.nz/~ml/weka/book.html" rel="nofollow noreferrer">Data Mining: Practical Machine Learning Tools and Techniques (Second Edition)</a> and <a href="http://www.kdnuggets.com/polls/2005/data_mining_textbooks.htm" rel="nofollow noreferrer">this poll</a>. What did you effectively use?</p> 
//	          </div> 
//	          <div class="post-taglist">   
	    		
	    		String allart= art_sb.toString().trim();
	    		
//	    		String[] query1 = new String[1];
//                query1[0] = allart;
//                String[] question1 = t.transString(query1, originLanguage);
//                allart = question1[0];
	    		
	    		q.setContent(allart);
	    		
	    	}
	    	//reader2.close();
	    }
		 // used to print source code	
//	    StringBuilder res = new StringBuilder();
//       while((line1=reader2.readLine())!=null){
//       	res.append(line1 + "\n");
//       }
//       reader2.close();
//       System.out.println(res.toString());
	    

		
	    writer.write(q.getQid()+"\n"+q.getVote()+"\n"+q.getTitle()+"\n"+q.getContent()+"\n"+Arrays.toString(q.getTags())+"\n");
	    writer.flush();
	    writer.close();
 
		
		return q;
	}
	
	
	
	
}


//<div class="question-summary" id="question-summary-40858515"> 
//<div class="statscontainer"> 
// <div class="statsarrow"></div> 
// <div class="stats"> 
//  <div class="vote"> 
//   <div class="votes"> 
//    <span class="vote-count-post "><strong>1</strong></span> 
//    <div class="viewcount">
//     vote
//    </div> 
//   </div> 
//  </div> 
//  <div class="status answered"> 
//   <strong>1</strong>answer 
//  </div> 
// </div> 
// <div class="views " title="31 views">
//   31 views 
// </div> 
//</div> 
//<div class="summary"> 
// <h3><a href="/questions/40858515/write-and-read-data-from-console-at-run-time-in-java-program" class="question-hyperlink">Write and read data from console at run time in java program</a></h3> 
// <div class="excerpt">
//   I have one shell script which has some commands, one of which command is expecting to enter some data at runtime. I am running this shell script using exec() method. Currently I am entering data ... 
// </div> 
// <div class="tags t-java t-shell t-console t-command t-processbuilder"> 
//  <a href="/questions/tagged/java" class="post-tag" title="show questions tagged 'java'" rel="tag">java</a> 
//  <a href="/questions/tagged/shell" class="post-tag" title="show questions tagged 'shell'" rel="tag">shell</a> 
//  <a href="/questions/tagged/console" class="post-tag" title="show questions tagged 'console'" rel="tag">console</a> 
//  <a href="/questions/tagged/command" class="post-tag" title="show questions tagged 'command'" rel="tag">command</a> 
//  <a href="/questions/tagged/processbuilder" class="post-tag" title="show questions tagged 'processbuilder'" rel="tag">processbuilder</a> 
// </div> 
// <div class="started fr"> 
//  <div class="user-info "> 
//   <div class="user-action-time">
//     asked 
//    <span title="2016-11-29 05:38:24Z" class="relativetime">54 mins ago</span> 
//   </div> 
//   <div class="user-gravatar32"> 
//    <a href="/users/6264949/tushar-digambar-deshpande">
//     <div class="gravatar-wrapper-32">
//      <img src="https://www.gravatar.com/avatar/7ab8a47c95d423969d3426dcf28c3eba?s=32&amp;d=identicon&amp;r=PG&amp;f=1" alt="" width="32" height="32">
//     </div></a> 
//   </div> 
//   <div class="user-details"> 
//    <a href="/users/6264949/tushar-digambar-deshpande">Tushar Digambar Deshpande</a> 
//    <div class="-flair"> 
//     <span class="reputation-score" title="reputation score " dir="ltr">41</span>
//     <span title="8 bronze badges"><span class="badge3"></span><span class="badgecount">8</span></span> 
//    </div> 
//   </div> 
//  </div> 
// </div> 
//</div> 
//</div>
//<div class="question-summary" id="question-summary-40858435"> 
//<div class="statscontainer"> 
// <div class="statsarrow"></div> 
// <div class="stats"> 
//  <div class="vote"> 
//   <div class="votes"> 
//    <span class="vote-count-post "><strong>-1</strong></span> 
//    <div class="viewcount">
//     votes
//    </div> 
//   </div> 
//  </div> 
//  <div class="status answered"> 
//   <strong>1</strong>answer 
//  </div> 
// </div> 
// <div class="views " title="26 views">
//   26 views 
// </div> 
//</div> 
//<div class="summary"> 
// <h3><a href="/questions/40858435/java-code-to-fetch-the-latest-folder-name-in-a-directory" class="question-hyperlink">Java Code to Fetch the Latest Folder Name in a Directory [on hold]</a></h3> 
// <div class="excerpt">
//   Am new to Java and seeking your help here. I have a Parent Folder/Directory Named as : DP_E2E_POC Under the Parent Folder i have many sub Folders, i need to find the latest Folder Name under the ... 
// </div> 
// <div class="tags t-java t-selenium"> 
//  <a href="/questions/tagged/java" class="post-tag" title="show questions tagged 'java'" rel="tag">java</a> 
//  <a href="/questions/tagged/selenium" class="post-tag" title="show questions tagged 'selenium'" rel="tag">selenium</a> 
// </div> 
// <div class="started fr"> 
//  <div class="user-info "> 
//   <div class="user-action-time">
//     asked 
//    <span title="2016-11-29 05:30:09Z" class="relativetime">1 hour ago</span> 
//   </div> 
//   <div class="user-gravatar32"> 
//    <a href="/users/4985758/satish-dhanaraj">
//     <div class="gravatar-wrapper-32">
//      <img src="https://www.gravatar.com/avatar/e2d8d870d33dd4a6612e3d1e48c90d68?s=32&amp;d=identicon&amp;r=PG&amp;f=1" alt="" width="32" height="32">
//     </div></a> 
//   </div> 
//   <div class="user-details"> 
//    <a href="/users/4985758/satish-dhanaraj">Satish Dhanaraj</a> 
//    <div class="-flair"> 
//     <span class="reputation-score" title="reputation score " dir="ltr">21</span>
//     <span title="6 bronze badges"><span class="badge3"></span><span class="badgecount">6</span></span> 
//    </div> 
//   </div> 
//  </div> 
// </div> 
//</div> 
//</div>
