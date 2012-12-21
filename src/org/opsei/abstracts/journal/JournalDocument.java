package org.opsei.abstracts.journal;

import java.util.ArrayList;
import java.util.HashMap;

public class JournalDocument {

	private String journal;
	private String year;
	private String number;
	private String volume;
	private String pages;
	private String doi;
	private String title;
	private String category;
	private ArrayList<String> authors;
	private HashMap<String, String> sections;
	private ArrayList<String> keywords;
	private String date;
	
	public JournalDocument()
	{
		authors = new ArrayList<String>();
		sections = new HashMap<String, String>();
		keywords = new ArrayList<String>();
	}

	public boolean addAuthor(String author)
	{
		ArrayList<String> list = getAuthors();
		boolean result = list.add(author);
		setAuthors(list);
		return result;
	}
	
	public void addSection(String section, String text)
	{
		HashMap<String, String> map = getSections();
		map.put(section, text);
		setSections(map);
	}
	
	public boolean addKeyword(String keywords)
	{
		ArrayList<String> list = getKeywords();
		boolean result = list.add(keywords);
		setKeywords(list);
		return result;
	}
	
	/**
	 * @return the journal
	 */
	public String getJournal() {
		return journal;
	}

	/**
	 * @return the volume
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * @return the doi
	 */
	public String getDoi() {
		return doi;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return the authors
	 */
	public ArrayList<String> getAuthors() {
		return authors;
	}

	/**
	 * @return the sections
	 */
	public HashMap<String, String> getSections() {
		return sections;
	}

	/**
	 * @return the keywords
	 */
	public ArrayList<String> getKeywords() {
		return keywords;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param journal the journal to set
	 */
	public void setJournal(String journal) {
		this.journal = journal;
	}


	/**
	 * @param volume the volume to set
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}

	/**
	 * @param doi the doi to set
	 */
	public void setDoi(String doi) {
		this.doi = doi;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}

	/**
	 * @param sections the sections to set
	 */
	public void setSections(HashMap<String, String> sections) {
		this.sections = sections;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the pages
	 */
	public String getPages() {
		return pages;
	}

	/**
	 * @param pages the pages to set
	 */
	public void setPages(String pages) {
		this.pages = pages;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JournalDocument \n[journal=" + journal + "\nyear=" + year
				+ "\nnumber=" + number + "\nvolume=" + volume + "\npages="
				+ pages + "\ndoi=" + doi + "\ntitle=" + title + "\ncategory="
				+ category + "\nauthors=" + authors + "\nsections=" + sections
				+ "\nkeywords=" + keywords + "\ndate=" + date + "]";
	}
}
