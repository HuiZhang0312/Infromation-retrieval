package search; 

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.*;
import com.google.api.services.translate.model.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.*;


public class Translator {

	public String originalLanguage = "";

    public String[] transString(String[] string, String language) throws Exception {

        // set key created via google cloud console
        final TranslateRequestInitializer KEY_INITIALIZER = new TranslateRequestInitializer("AIzaSyBGx3wdcskRW71X6DpuKPOSCUGIdu3JCp8");

        // Set up the HTTP transport and JSON factory
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // set up translate
        final Translate translate = new Translate.Builder(httpTransport, jsonFactory, null)
                .setApplicationName("My Project")
                .setTranslateRequestInitializer(KEY_INITIALIZER)
                .build();

   

        // translate
        
        	
        //	String str = "Your assignment 3 is able to return top N documents for a given query. In this assignment, what you need to do is to enhance the retrieval model with pseudo relevance feedback. That is, for a given query, you need to implement at least the following steps:";
     
            
            String [] s = new String[string.length];
            
  
             
             Builder<String> b = ImmutableList.<String>builder().add(string[0]);
           
             for(int i = 1; i < string.length; i++)
            	b.add(string[i]);
            	   
            
            
            final ImmutableList<String> phrasesToTranslate = b.build();
           //zh-CN
            
         Iterator itr = translate.translations().list(phrasesToTranslate, language).execute().getTranslations().listIterator();
            	
         int i = 0;
         while(itr.hasNext()){
        	  
        	  TranslationsResource t = (TranslationsResource)itr.next();
        	s[i++] = t.getTranslatedText();
        	
        	if(originalLanguage.equals(""))
          	  originalLanguage = t.getDetectedSourceLanguage();
        	
          }
          
      
            // output: {"translations":[{"detectedSourceLanguage":"en","translatedText":"Bonjour le monde"},{"detectedSourceLanguage":"en","translatedText":"O√π puis-je promener mon chien"}]}
        
        
        return s;
    }
    
    public static void main(String args[]) throws Exception{
    	
    	Translator test = new Translator();
    //	String []string = {"I like the java language!", "I like the C language!","Your assignment 3 is able to return top N documents for a given query. In this assignment, what you need to do is to enhance the retrieval model with pseudo relevance feedback. That is, for a given query, you need to implement at least the following steps:"};
        //String []string = {"Java”Ô—‘"};
    	//String[] t = test.transString(string,"en");
    	
    	
    	
    	String query = "Java”Ô—‘";
    	
    	String[] q = new String[1];
    	
    	q[0] = query;
    	
    	String[] t = test.transString(q, "en");
    	
    	for(String s : t){
    		System.out.println(s);
    	}
    	
    }
}
    
    
    