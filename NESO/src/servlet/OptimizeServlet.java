package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.Question;
import indexingLucene.Index;
import preProcess.PreProcess;
import search.Search;

/**
 * Servlet implementation class OptimizeServlet
 */
@WebServlet("/OptimizeServlet")
public class OptimizeServlet extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 341006698831237359L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public OptimizeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Set content type and encoding
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		String search = (String) request.getSession().getAttribute("search");
		System.out.println("Translated search:"+search);
		
		
		PreProcess p = new PreProcess();
		Index i = new Index();
		Search s = new Search();
		
		List<Question> qList = null;
		
		try {
			p.DoPreProcess();
			i.WriteIndex();
			
			qList = s.DoSearch(search);
			System.out.println("wssssssssss" + qList.isEmpty());
			System.out.println(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("qList", qList);
		
		request.getRequestDispatcher("optimize.jsp").forward(request, response);
		
		
	}

}
