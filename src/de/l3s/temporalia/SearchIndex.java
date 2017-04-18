package de.l3s.temporalia;
/**
 * @author Renato Stoffalette Joao
 * @mail renatosjoao@gmail.com
 * @version 1.0
 * @date 02.2017
 * 
 */
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;

public class SearchIndex {

	static IndexSearcher is = null;
	static IndexReader rdr =	null;
	private int resCount = 0;
	private String index_dir ="/home/renato/Downloads/Temporalia/index";
	
	
	public SearchIndex() throws IOException {
		super();
		rdr =	DirectoryReader.open(FSDirectory.open(new File(index_dir ).toPath()));
		is = new IndexSearcher(rdr);
	}

	/**
	 *
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws IOException
	 * @throws java.text.ParseException 
	 */
	public LinkedList<IndexResult> search(String arg1, String arg2, String arg3) throws IOException, java.text.ParseException {
		LinkedList<IndexResult> ir = new LinkedList<IndexResult>();
		ScoreDoc[] td = null;
		if((arg2 == " ") && (arg3 == " ")){
			try {
				td = searchTerm(arg1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if((arg1!=" ")&& (arg2!=" ") && (arg3!=" ")){
				try {
					td = searchRange(arg1, arg2, arg3);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		}
		if((arg2!=" ") && (arg3==" ")){
			try {
				td = search_at_Date(arg1,arg2);
		 	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if((arg2==" ") && (arg3!=" ")){
			try {
				td = search_at_Date(arg1,arg3);
		 	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
			for (int i = 0; i < td.length; i++) {
				Document hitDoc = is.doc(td[i].doc);
				String title = hitDoc.get("title"); if(title==null){ title = " ";}
				String ID = hitDoc.get("id"); 		if(ID==null){ ID = " ";}
				String text = hitDoc.get("content");	
				String date = hitDoc.get("date");	
				if(date == null){
					date = "";
					}else{  
						final String OLD_FMT = "yyyyMMdd";
						final String NEW_FORMAT = "dd.MM.yyyy";
						SimpleDateFormat sdf = new SimpleDateFormat(OLD_FMT);
						Date d = null;
						d = sdf.parse(date);
						sdf.applyPattern(NEW_FORMAT);
						date = sdf.format(d);
					}
				ir.add(new IndexResult(title,ID,text,date));
				}				
		rdr.close();
		return ir;
	}
	
	/**
	 *
	 * @param term
	 * @param date
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws java.text.ParseException 
	 */
	public ScoreDoc[] search_at_Date(String term, String date) throws IOException, ParseException, java.text.ParseException{
		QueryParser parser1 = new QueryParser("content", new StandardAnalyzer());
		Query query1 = parser1.parse(term);
		
		SimpleDateFormat dateField = new SimpleDateFormat("yyyy-MM-dd");
	   	long milliseconds = 0;
	   	Date dateExact = dateField.parse(date);
		milliseconds = dateExact.getTime();		
		Query query2 = LongPoint.newExactQuery("dateL",milliseconds);
		
		BooleanQuery bQ = new BooleanQuery.Builder()
		.add(query1, BooleanClause.Occur.MUST)
		.add(query2, BooleanClause.Occur.MUST)
		.build();
		ScoreDoc[] hits = is.search(bQ, 100).scoreDocs;
		TotalHitCountCollector hit = new TotalHitCountCollector();
		is.search(bQ, hit);
		resCount = hit.getTotalHits();
		System.out.println(resCount);

		return hits;
		
	}
	
	/**
	 *
	 * @param q
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public ScoreDoc[] searchTerm(String q)	throws IOException, ParseException {
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		Query query = parser.parse(q);
		ScoreDoc[] hits = is.search(query, 100).scoreDocs;	
		TotalHitCountCollector hit = new TotalHitCountCollector();
		is.search(query, hit);
		resCount = hit.getTotalHits();
		System.out.println(resCount);
		return hits;
}
	/**
	 *
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws java.text.ParseException 
	 */
	public ScoreDoc[] searchRange(String term, String begin, String end) throws ParseException, IOException, java.text.ParseException{
		QueryParser parser1 = new QueryParser("content", new StandardAnalyzer());
		Query query1 = parser1.parse(term);
	 	SimpleDateFormat dateField = new SimpleDateFormat("yyyy-MM-dd");
	   	long millisecondsBegin = 0;
	   	long millisecondsEnd = 0;
	   	Date dateBegin = dateField.parse(begin);
	   	Date dateEnd = dateField.parse(end);
		millisecondsBegin = dateBegin.getTime();		
		millisecondsEnd = dateEnd.getTime();
		Query query2 = LongPoint.newRangeQuery("dateL",millisecondsBegin,millisecondsEnd);
		
		BooleanQuery bQ = new BooleanQuery.Builder()
		.add(query1, BooleanClause.Occur.MUST)
		.add(query2, BooleanClause.Occur.MUST)
		.build();
		
		ScoreDoc[] hits = is.search(bQ, 100).scoreDocs;
		
		TotalHitCountCollector hit = new TotalHitCountCollector();
		is.search(bQ, hit);
		resCount = hit.getTotalHits();
		System.out.println(resCount);

		return hits;
	}
	

}
