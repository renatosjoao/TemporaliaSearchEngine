package de.l3s.temporalia;
/**
 * @author Renato Stoffalette Joao
 * @mail renatosjoao@gmail.com
 * @version 1.0
 * @date 02.2017
 * 
 */
public class TemporaliaDoc {

	String docID = null;
	String title = null;
	String date = null;
	String text = null;

	/**
	 *
	 * @param docID
	 * @param title
	 * @param date
	 * @param text
	 */
	public TemporaliaDoc(String docID, String title, String date, String text) {
		super();
		this.docID = docID;
		this.title = title;
		this.date = date;
		this.text = text;
	}
	/**
	 * @return the docID
	 */
	public String getDocID() {
		return docID;
	}
	/**
	 * @param docID the docID to set
	 */
	public void setDocID(String docID) {
		this.docID = docID;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	
	
	
}
