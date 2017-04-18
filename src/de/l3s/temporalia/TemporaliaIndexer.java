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
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.xml.sax.SAXException;

public class TemporaliaIndexer {

	private static IndexWriter writer;
	private static String indexDir;
	private static StandardAnalyzer stAnal = null;
	
	/**
	 *
	 * @param args
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ParseException{
		TemporaliaParser tp = new TemporaliaParser();
		File f = new File(args[0]);
		String[] extension = new String[] { "xml" };
		System.out.println("Getting all files in " + f.getCanonicalPath()+ " including those in subdirectories");
		List<File> files = new ArrayList<File>();
		if(f.isDirectory()){
			files = (List<File>) FileUtils.listFiles(f, extension,true);
		}else{
			files.add(f);
		}
		indexDir = args[1]; //This is the location in which I am writing my index.
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDir));
		stAnal = new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET);
		writer = new IndexWriter(indexDirectory, new IndexWriterConfig(stAnal));
		int numDocs = 0;
		
		for(File ff : files){
			System.out.println("indexing "+ff.getName());
			ArrayList<TemporaliaDoc> currentFileDocs = tp.getDocumentsList(ff);
			numDocs += currentFileDocs.size();
			for(TemporaliaDoc tD : currentFileDocs){
				Document doc = new Document();
				String content = tD.getTitle() + "\n" + tD.getText();
				doc.add(new TextField("title", tD.getTitle(), Field.Store.YES));	
			    doc.add(new TextField("id",tD.getDocID(),Field.Store.YES));
			   	doc.add(new TextField("text", tD.getText(), Field.Store.YES));
			   	doc.add(new TextField("content", content, Field.Store.YES));
			   	//String date = tD.getDate().replaceAll("-","");
			   	String date = tD.getDate();
			   	SimpleDateFormat dateField = new SimpleDateFormat("yyyy-MM-dd");
			   	long milliseconds = 0;
			   	try {
			   	    Date d = dateField.parse(date);
			   	    milliseconds = d.getTime();
			   	} catch (ParseException e) {
			   	    e.printStackTrace();
			   	}
			   	doc.add(new LongPoint("dateL",milliseconds));
				doc.add(new TextField("date", date, Field.Store.YES));
				writer.addDocument(doc);
			}
		}
		writer.close();
		System.out.println(numDocs + " were indexed.");
		
		
		
	}
	
	/**
	 * 
	 * @param create - boolean value
	 * @return
	 * @throws IOException
	 */
	public IndexWriter getIndexWriter(boolean create) throws IOException {
		if (writer == null) {
			Directory indexDirectory = FSDirectory.open(Paths.get(indexDir));
			writer = new IndexWriter(indexDirectory, new IndexWriterConfig(stAnal));
		}
		return writer;
	}

	/**
	 * Closes the Indexer as it is needed
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public void close() throws CorruptIndexException, IOException {
		writer.close();
	}
}
