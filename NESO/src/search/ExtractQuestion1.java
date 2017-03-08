package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import classes.*;

public class ExtractQuestion1 {
	private String line = "";
	BufferedReader reader = null;
	int qid = 0;
	
	public ExtractQuestion1(String url) throws IOException{
		
		Document doc2 = Jsoup.connect(url).get();
		String s =doc2.body().toString();
	    reader = new BufferedReader(new StringReader(s));
	}
	
	public boolean hasNext() throws IOException{
		
		while((line = reader.readLine()) != null){
			if (line.contains("question-summary search-result")){
				return true;
			}
			if(line==null){
				reader.close();
				break;
			}
		}
		return false;
	}
	
	//get vote, title, tags, contents for each question
	public Question next() throws Exception{
		Question q = new Question();
		String title="";
		String vote ="";
		String tag ="";
		StringBuilder response = new StringBuilder();
		int c= 0;
		Translator t = new Translator();
		
        while (!line.contains("<div class=\"started fr\">")){
        	line = reader.readLine();
        	//get vote count
        	if(line.contains("\"vote-count-post \"")){
        		
        		vote =line.substring(49, line.length()-17);
        		//response.append(vote.trim() + "\n");
        		qid++;
        	}
//<span> <a href="/questions/36286102/how-to-change-source-code-of-page-using-selenium-and-java" data-searchsession="/questions/36286102/how-to-change-source-code-of-page-using-selenium-and-java?s=1|2.0343" title="How to change source code of page? using selenium and java"> Q: How to change source code of page? using selenium and java </a> </span>        	
        	//get title
        	if(line.contains("class=\"result-link\"")){
        		line = reader.readLine();
        		int begin = line.indexOf("title=\"");
        		int end = line.indexOf("\">");
        		title = line.substring(begin+7,end);
        		//System.out.println(title);
        		String[] query = new String[1];
                query[0] = title;
                String[] question = t.transString(query, "zh-CN");
                title = question[0];
        	}
        	
        	//get tags
        	if(line.contains("tags user-tags")){
        		StringBuilder sb = new StringBuilder();
        		while(!(line=reader.readLine()).contains("</div>")){
        			int end = line.indexOf("</a>");
        			int begin = line.indexOf("rel=\"tag\">");
        			tag = line.substring(begin+10, end);
        			sb.append(tag + " ");
        		}
        		String alltags = sb.toString().trim();
        		String[] tags = alltags.split(" ");
        		//tags = t.transString(tags, "zh-CN");
        		q.setTags(tags);
        		//System.out.println(tags[1]);
        	}
        	//<div class="tags user-tags t-java t-javascript t-html t-jsoup"> 
//            <a href="/questions/tagged/java" class="post-tag" title="show questions tagged 'java'" rel="tag">java</a> 
//            <a href="/questions/tagged/javascript" class="post-tag" title="show questions tagged 'javascript'" rel="tag">javascript</a> 
//            <a href="/questions/tagged/html" class="post-tag" title="show questions tagged 'html'" rel="tag">html</a> 
//            <a href="/questions/tagged/jsoup" class="post-tag" title="show questions tagged 'jsoup'" rel="tag">jsoup</a> 
//            </div>
        	
        	//get content
        	if(line.contains("")){
        		
        	}
        q.setQid(qid + "");
        q.setVote(vote);
        q.setTitle(title);
        
       
        
        
        }
		
		return q;
	}

}
