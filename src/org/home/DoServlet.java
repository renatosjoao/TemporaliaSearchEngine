package org.home;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.l3s.temporalia.IndexResult;
import de.l3s.temporalia.SearchIndex;

/**
 * Servlet implementation class DoServlet
 */
@WebServlet("/DoServlet")
public class DoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryTerm = request.getParameter("queryTerm").trim();
		String beginDate = request.getParameter("beginDate").trim();
		String endDate = request.getParameter("endDate").trim();
	
		final String OLD_FORMAT = "dd/MM/yyyy";
		final String NEW_FORMAT = "yyyy-MM-dd";

		String oldBeginDateString = " ";
		String newBeginDateString = " ";
		String oldEndDateString = " ";
		String newEndDateString = " ";

		
		if(beginDate!=""){
			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
			oldBeginDateString = beginDate;
			Date d = null;
			try {
				d = sdf.parse(oldBeginDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			sdf.applyPattern(NEW_FORMAT);
			newBeginDateString = sdf.format(d);
		}else{
			
		}
			
		if(endDate!=""){
			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
			oldEndDateString = endDate;
			Date d = null;
			try {
				d = sdf.parse(oldEndDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			sdf.applyPattern(NEW_FORMAT);
			newEndDateString = sdf.format(d);
		}else{
		}
		SearchIndex SI = new SearchIndex();
		LinkedList<IndexResult> search_res = null;
		try {
			search_res = SI.search(queryTerm, newBeginDateString, newEndDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String results =formatOutput(search_res);
		//System.out.println(results);
		response.setContentType("text/html");
		response.getWriter().write(results);
		
	}

	
	
	/**
	 *
	 * @param search_res
	 * @return
	 */
	public String formatOutput(LinkedList<IndexResult> search_res){
		String htmlRow = "";
		for(int i = 0; i < search_res.size(); i++){
			String summary = search_res.get(i).getPageText();
			if(summary.length() >= 200){ 
				summary = summary.substring(0,200);
				}else{
					summary = search_res.get(i).getPageText();
				}
			htmlRow += "<div class=\"col-xs-12 col-sm-12 col-md-7\">"
					+"<h5><a href=\"#\" title=\"\">"+search_res.get(i).getPageTitle()+"</a></h5>"
					+"<p> <font color=\"green\">"+ search_res.get(i).getPageId() + "</font>  <font color=\"#815FA7\"><span class=\"badge\">"+ search_res.get(i).getPageDate() + "</badge></font>  </p>"
					+"<p  class=\"small text-justify\"> <font color=\"black\">"+ summary+"..." + "</font></p>"						
	               // +"<span class=\"plus\"><a href=\"#\" title=\"\"><i class=\"glyphicon glyphicon-plus\"></i></a></span>"
	                +"</div>"
					+"<span class=\"clearfix border\"></span>";

					//+"<hr color: black; height: 1px; background-color:black;\" />";
			//htmlRow += "<tr> ["+i+"] \t" +search_res.get(i).getPageId()+"  </tr> <br> <tr> "+search_res.get(i).getPageTitle()+"  </tr> <br>";
			//System.out.println(htmlRow);
			 
		}
		//System.out.println(htmlRow);
		String formatedOutput = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\">"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
				+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">"
				+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">"
				+ "<title></title></head>"
				+ "<body>"
				+ "<h3 class=\"lead\"><strong class=\"text-danger\">"+search_res.size()+"</strong> results were found.</h3>"		
				+"<div class=\"block\">"
				+ htmlRow 
				+"</div>"
				+ "</body></html>";
		return formatedOutput;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
