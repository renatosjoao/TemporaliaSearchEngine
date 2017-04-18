package de.l3s.similarities;
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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
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
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

public class Similarities {

	static IndexSearcher is = null;
	static IndexReader rdr = null;

	// donald trump@2011-2013
	// mu=100,1000,2000
	// lambda0.3,0.5,0.7
	// only top5

	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

		Options options = new Options();
		options.addOption("i", "smoothing", true, "Language Model Smoothing. 1 for Dirichlet, 2 for Jelinek-Mercer");
		options.addOption("m", "mu",true,"Dirichlet Prior mu");
		options.addOption("l", "lambda",true,"Jelinek-Mercer Smoothing lambda");
		options.addOption("q", "query",true,"Query Term");
		options.addOption("b", "begin",true,"Begin Date");
		options.addOption("e", "end",true,"End Date");
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException e) {
			e.printStackTrace();
		}

		String SmoothingTech = cmd.getOptionValue("smoothing");
		
		if(SmoothingTech == null ){
			formatter.printHelp("LMSmoothing -i <technique> [ -l <lambda> or -m <mu> ] -q <queryTerm> -b <beginDate> -e <endDate> \n ", options);			
			}else{
				
				rdr = DirectoryReader.open(FSDirectory.open(new File("/home/renato/Downloads/Temporalia/index").toPath()));
				is = new IndexSearcher(rdr);
				
				
				//DIRICHLET 
				if(SmoothingTech.equals("1")){
					String muS  = cmd.getOptionValue("m");
					if(muS == null){
						formatter.printHelp("LMSmoothing -i <technique> [ -l <lambda> or -m <mu> ] -q <queryTerm> -b <beginDate> -e <endDate> \n ", options);
					}else{
						int mu = Integer.parseInt(muS);
						LMDirichletSimilarity LM = new LMDirichletSimilarity(mu);
						
						String queryTerm  = cmd.getOptionValue("query");
						if(queryTerm == null){
							formatter.printHelp("LMSmoothing -i <technique> [ -l <lambda> or -m <mu> ] -q <queryTerm> -b <beginDate> -e <endDate> \n ", options);
						}else{
							String beginDate = cmd.getOptionValue("b");
							String endDate  = cmd.getOptionValue("e");
							TopDocs tdd = null;
							if((beginDate==null) || (endDate==null) ){
								tdd = searchTerm(queryTerm, LM);							
							}else{
								tdd = searchRange(queryTerm, beginDate, endDate, LM);
							}
							
							ScoreDoc[] td = tdd.scoreDocs;
							for (int i = 0; i < td.length; i++) {
								Document hitDoc = is.doc(td[i].doc);
								String title = hitDoc.get("title");
								String ID = hitDoc.get("id");
								System.out.println("["+i+"] : "+ ID + "\t score : "+td[i].score);
								
						}
							
						}
					}
					
				}
				//JELINEk-MERCER
				if(SmoothingTech.equals("2")){
					String lambdaS  = cmd.getOptionValue("l");
					if(lambdaS == null){
						
					}else{
						float lambda = Float.parseFloat(lambdaS);
						LMJelinekMercerSimilarity LJ = new LMJelinekMercerSimilarity(lambda);
						String queryTerm  = cmd.getOptionValue("q");
						
						if(queryTerm == null){
							formatter.printHelp("LMSmoothing -i <technique> [ -l <lambda> or -m <mu> ] -q <queryTerm> -b <beginDate> -e <endDate> \n ", options);
						}else{
							String beginDate = cmd.getOptionValue("b");
							String endDate  = cmd.getOptionValue("e");
							
							TopDocs tdd = null;
							if((beginDate==null) || (endDate==null) ){
								tdd = searchTerm(queryTerm, LJ);							
							}else{
								tdd = searchRange(queryTerm, beginDate, endDate, LJ);
							}
							
							ScoreDoc[] td = tdd.scoreDocs;
							for (int i = 0; i < td.length; i++) {
								Document hitDoc = is.doc(td[i].doc);
								String title = hitDoc.get("title");
								String ID = hitDoc.get("id");
								System.out.println("["+i+"] : "+ ID + "\t score : "+td[i].score);
						}
							
						}
					}
				}
				
			
			
			

			rdr.close();
		}
	}

	/**
	 *
	 * @param q
	 * @param sim
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static TopDocs searchTerm(String q, Similarity sim) throws IOException, ParseException {
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		Query query = parser.parse(q);
		is.setSimilarity(sim);
		TopDocs hits = is.search(query, 5);
		return hits;
	}

	/**
	 *
	 * @param term
	 * @param begin
	 * @param end
	 * @param sim
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws java.text.ParseException
	 */
	public static TopDocs searchRange(String term, String begin, String end, Similarity sim) throws ParseException, IOException, java.text.ParseException {
		QueryParser parser1 = new QueryParser("content", new StandardAnalyzer());
		Query query1 = parser1.parse(term);
		SimpleDateFormat dateField = new SimpleDateFormat("yyyy-MM-dd");
		long millisecondsBegin = 0;
		long millisecondsEnd = 0;
		Date dateBegin = dateField.parse(begin);
		Date dateEnd = dateField.parse(end);
		millisecondsBegin = dateBegin.getTime();
		millisecondsEnd = dateEnd.getTime();
		Query query2 = LongPoint.newRangeQuery("dateL", millisecondsBegin,millisecondsEnd);

		BooleanQuery bQ = new BooleanQuery.Builder()
				.add(query1, BooleanClause.Occur.MUST)
				.add(query2, BooleanClause.Occur.MUST).build();

		is.setSimilarity(sim);
		TopDocs hits = is.search(bQ, 5);
		// ScoreDoc[] hits = is.search(bQ, 100).scoreDocs;
		// TotalHitCountCollector hit = new TotalHitCountCollector();
		// is.search(bQ, hit);
		return hits;
	}

}
