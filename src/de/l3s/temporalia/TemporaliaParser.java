package de.l3s.temporalia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


//Checking fo the total number of documents.
//55.927
//Republic of Ireland @ 2011-2011 (1428)
// @2013-2011 ( 0 ) 
//America  @2011-2013 ( 8387)
//


public class TemporaliaParser {

	String filesDir = null;

	public TemporaliaParser() {
		super();

	}
	
	/**
	 *
	 * @param filename
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	protected ArrayList<TemporaliaDoc> getDocumentsList(File filename)throws ParserConfigurationException, SAXException, IOException {
		File tempFile = new File(".temp.xml");
		PrintWriter tempFileWriter  = new PrintWriter(tempFile);
		BufferedReader bff = new BufferedReader(new FileReader(filename));
		String line = null;
		while((line = bff.readLine()) != null){
			//line = line.replaceAll("&gt;", ">")//.replaceAll("&lt;", "<").replaceAll("&amp;nbsp;", " ");
			line = line.replaceAll("&amp;","&");
			line = line.replaceAll("&","&amp;");
			tempFileWriter.println(line);
		}
		tempFileWriter.flush();
		tempFileWriter.close();
		bff.close();
		
		ArrayList<TemporaliaDoc> fileDocs = new ArrayList<TemporaliaDoc>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setSchema(null);
		factory.setNamespaceAware(false);

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(".temp.xml"));
		
		NodeList docList = doc.getElementsByTagName("doc");
		int docListsize = docList.getLength();
		if (docListsize != 0) {
			for (int i = 0; i < docListsize; i++) {
				Element parent = (Element) docList.item(i);
				String docID = parent.getAttribute("id").trim();
				NodeList textList = null;
				textList = parent.getElementsByTagName("text");
				NodeList metaList = null;
				metaList = parent.getElementsByTagName("meta-info");
				NodeList kids = null;
				kids = metaList.item(0).getChildNodes();
				String title = null;
				title = kids.item(1).getTextContent().trim();
				String date = null;
				date = kids.item(3).getTextContent().trim();
				String text = null;
				if(textList.getLength()<=0){
					text = " ";
					}else{
						text = textList.item(0).getTextContent();
						}
				TemporaliaDoc tempDoc = new TemporaliaDoc(docID, title, date, text);
				fileDocs.add(tempDoc);
			}
		}
		return fileDocs;
	}
}
