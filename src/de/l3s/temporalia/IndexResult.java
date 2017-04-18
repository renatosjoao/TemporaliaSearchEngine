package de.l3s.temporalia;
/**
 * @author Renato Stoffalette Joao
 * @mail renatosjoao@gmail.com
 * @version 1.0
 * @date 02.2017
 * 
 */
public class IndexResult {

	String pageTitle = null;
	String pageId = null;
	String pageText = null;
	String pageDate = null;

	public IndexResult() {
		super();
	}

	public IndexResult(String pageTitle, String pageId, String pageText,
			String pageDate) {
		super();
		this.pageTitle = pageTitle;
		this.pageId = pageId;
		this.pageText = pageText;
		this.pageDate = pageDate;
	}

	/**
	 * @return the pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle
	 *            the pageTitle to set
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the pageId
	 */
	public String getPageId() {
		return pageId;
	}

	/**
	 * @param pageId
	 *            the pageId to set
	 */
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	/**
	 * @return the pageText
	 */
	public String getPageText() {
		return pageText;
	}

	/**
	 * @param pageText
	 *            the pageText to set
	 */
	public void setPageText(String pageText) {
		this.pageText = pageText;
	}

	/**
	 * @return the pageDate
	 */
	public String getPageDate() {
		return pageDate;
	}

	/**
	 * @param pageDate
	 *            the pageDate to set
	 */
	public void setPageDate(String pageDate) {
		this.pageDate = pageDate;
	}

}
