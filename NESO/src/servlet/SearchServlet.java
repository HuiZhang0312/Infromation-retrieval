package servlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.Question;
import search.*;

public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = -1869746441076831984L;

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings("resource")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		// Set content type and encoding
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		// Initial translator 
		Translator t = new Translator();
		
		String originLanguage = null;
		
		// Get query content
		String query = request.getParameter("search");
		//System.out.println(query);
		
		String[] q = new String[1];
		
		q[0] = query;
		
		//System.out.println(q[0]);
		
		//String[] search = request.getParameter("search").split(" ");
		// Initial translate result
		String[] search = null;
		
		try {
			// Translate and get result
			search = t.transString(q, "en");
			
			originLanguage = t.originalLanguage;
			//for(String s:search){
			//	System.out.println(s);
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Create URL
		//String url = "http://stackoverflow.com/search?q=";
		String url = "";
		
		for(int i=0;i<search.length;i++){
			if(i!=search.length-1){
				url+=search[i]+" ";
			}else{
				url+=search[i];
			}
		}
		
		// Extract questions
		ExtractQuestion questions= new ExtractQuestion(url);
		
		
//		String path = getServletContext().getRealPath("WEB-INF/classes/");
//		//System.out.println(path);
//		File file = new File(path+"Questions.txt");
//		FileWriter writer = new FileWriter(file);
//		
//		while(questions.hasNext()){
//			try {
//				Question question = questions.next();
//				 writer.write(question.getQid()+"\n"+question.getVote()+"\n"+question.getTitle()+"\n"+question.getContent()+"\n"+Arrays.toString(question.getTags())+"\n");
//				 writer.flush();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
		
		request.setAttribute("questions", questions);
		request.getSession().setAttribute("query", query);
		request.getSession().setAttribute("search", url);
		request.getSession().setAttribute("originLanguage", originLanguage);
		//request.setAttribute("query", query);
		//request.setAttribute("originLanguage", originLanguage);
		
		request.getRequestDispatcher("result.jsp").forward(request, response);
		
		/*while(questions.hasNext()){
    		Question aQuestion = questions.next();
    		String tags[] = aQuestion.getTags();
    		System.out.println(aQuestion.getQid() + "\t" + aQuestion.getVote() + "\t" + aQuestion.getTitle() + "\t" + Arrays.toString(tags));
    	}*/
		
		
		//response.sendRedirect(url);
	}

}
