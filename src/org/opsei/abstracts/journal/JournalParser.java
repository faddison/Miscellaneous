package org.opsei.abstracts.journal;

import java.awt.List;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.thoughtworks.xstream.XStream;

/*
 * Acts only on a single set of elements per instantiation
 */
public class JournalParser 
{
	public ArrayList<String> errorList;
	public ArrayList<String> unknownCategories = new ArrayList<String>();
	public int errors = 0;
	public int accepted = 0;
	public int refused = 0;
	public int unknown = 0;
	private ArrayList<String> acceptedCategories;
	private ArrayList<String> refusedCategories;
	
	public JournalParser()
	{
		errorList = new ArrayList<String>();
		
		acceptedCategories = new ArrayList<String>();
		acceptedCategories.add("original article");
	    acceptedCategories.add("invited paper");
	    acceptedCategories.add("review paper");
	    acceptedCategories.add("original paper");
	    acceptedCategories.add("case illustration");
	    acceptedCategories.add("special annual issue");
	    acceptedCategories.add("abstracts");
	    acceptedCategories.add("case-based update");
	    acceptedCategories.add("case for discussion");
	    
	    refusedCategories = new ArrayList<String>();
		refusedCategories.add("technical note");
		refusedCategories.add("neurosurgical forum");
		refusedCategories.add("editorial");
		refusedCategories.add("case illustration");
		refusedCategories.add("neurosurgical forum: letter to the editor");
		refusedCategories.add("laboratory investigation");
		refusedCategories.add("laboratory investigations");
		refusedCategories.add("neurosurgical forum: letters to the editor");
		refusedCategories.add("historical vignettes");
		refusedCategories.add("case illustrations");
		refusedCategories.add("historical vignette");
	}
	
	public ArrayList<Element> singleSelect(String filename, String selectPattern)
	{
//		System.out.println(filename);
		ArrayList<Element> elementList = new ArrayList<Element>();
		try
		{
			Document doc = Jsoup.parse(new File(filename), null);
			Elements links = doc.select(selectPattern);
			if (!links.isEmpty())
				elementList.addAll(links);
			else 
			{
				errorList.add(filename);
				return null;
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
//		showList(list);
		return elementList;
	}
	
	public ArrayList<String> getElementText(ArrayList<Element> elementList)
	{
		ArrayList<String> list = new ArrayList<String>();
		for (Element e: elementList)
		{
			list.add(e.text());
		}
		showList(list);
		return list;
	}	
	
	public String filterAbstractsByType(String filename, String selectQuery, String typesCSV)
	{
		
		
//		System.out.println("Checking... " + filename);
		ArrayList<Element> elementList = new ArrayList<Element>();
		try
		{
			Document doc = Jsoup.parse(new File(filename), null);
			Elements links = doc.select(selectQuery);
			if (!links.isEmpty())
				elementList.addAll(links);
			else 
			{
				errorList.add(filename);
				return null;
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		ArrayList<String> fileList = new ArrayList<String>();
		for (Element e: elementList)
		{
			String temp = filename + " = " + e.text();
			System.out.println(temp);
//			JournalUTIL.writeToFile("cns/abstract-types.txt", temp, true);
			if (typesCSV.toUpperCase().contains(e.text().toUpperCase()))
			{
				return filename;
			}
		}
		return null;
	}
	
	/*
	 * 
	 */
	public void showList(ArrayList<?> list)
	{
		for (Object o: list)
		{
			System.out.println(o.toString());
		}
//		System.out.println("----------");
	}

	/*
	 * 
	 */
	public void showErrors()
	{
		System.out.println();
		System.out.println("ERRORS:");
		for (String s: errorList)
		{
			System.out.println(s);
		}
		System.out.println(errorList.size());
	}

	/**
	 * @return the errorList
	 */
	public ArrayList<String> getErrorList() {
		return errorList;
	}
	
	public void countTypes(String filename)
	{
		HashMap<String, Integer> freq = new HashMap<String, Integer>();
		
		BufferedReader br = JournalUTIL.getStream(filename);
		String str;
		try
		{
			while ((str = br.readLine()) != null)
			{
				String type = str;
				if (freq.containsKey(type))
				{
					freq.put(type, freq.get(type) + 1);
				}
				else
					freq.put(type, 1);
			}
			System.out.println(freq.toString().replace(",", "\n"));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void extractAbstractText(String filename)
	{
		System.out.println(filename);
		try
		{
			Document doc = Jsoup.parse(new File(filename), null);
			Elements elements = doc.select("[class=AbstractSection]");
			for(Element e: elements)
			{
				String header = e.getElementsByTag("h3").first().text();
				System.out.println(header);
				System.out.println(e.getElementsByTag("div").first().text().substring(header.length()));
//				System.out.println(e.toString());
//				System.out.println(e.text().replace("&nbsp;", ""));
			}
		System.out.println();
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public JournalDocument abstractToJournalDocument(String filename)
	{
		JournalDocument journaldocument = new JournalDocument();
		
		try
		{
			
			Document doc = Jsoup.parse(new File(filename), null);
			
			Elements elements;
			Element element;
			
			String journal;
			String volume;
			String number;
			String year;
			String pages;
			String doi;
			String category = "";
			String title;
			
			System.out.println(filename);
			
			
			/**
			 * Get category, title, and authors
			 */
			elements = doc.select("[id=ContentHeading]").select("[class=text]");
			
			// Category
			if (elements.select("[class=articleCategory]").first() != null)
			category = elements.select("[class=articleCategory]").first().ownText().toLowerCase().trim();
			if (category.isEmpty())
				category = elements.select("[class=articleCategory]").first().text().toLowerCase().trim();
//			System.out.println(category);
		    
			//if (this.acceptedCategories.contains(category))
			if (this.acceptedCategories.contains(category))
			{
				this.accepted++;
				System.out.println("accepted: " +category);
			}
			else if (category.isEmpty())
			{
				this.unknown++;
				System.out.println("unknown category!");
				this.unknownCategories.add(filename);
				return null;
			}
			else
			{
				this.refused++;
				System.out.println("refused " +category);
				return null;
			}
					
			// Title
			title = elements.select("[title=Link to Article").first().ownText().toLowerCase().trim();
			if (title.isEmpty())
				title = elements.select("[title=Link to Article").first().text().toLowerCase().trim();
			
			// Authors
			for (Element e: elements.select("[class=authors]").select("a"))
			{
				String author = e.ownText().toLowerCase().trim();
//				System.out.println(author);
				journaldocument.addAuthor(author);
			}
			
			/**
			 * Get attributes
			 */
			
			elements = doc.select("[class=heading enumeration");
			
			// Journal
			journal = elements.select("[title=Link to the Journal of this Article]").first().ownText().toLowerCase().trim();
			
			// Volume
			volume = elements.select("[title=Link to the Issue of this Article]").first().text().split(",")[0].toLowerCase().replace("volume", "").trim();
						
			// Number
			number = elements.select("[title=Link to the Issue of this Article]").first().text().split(",")[1].toLowerCase().replace("number", "").trim();
			
			// Year
			year = elements.select("[class=secondary").first().ownText().toLowerCase().replaceAll("[(),]", "").trim();
//			year = elements.select("[class=secondary").first().
			
			// Pages
			pages = elements.select("[class=pagination").first().text().trim();
			if (pages.contains("-"))
			{
				int min = Integer.parseInt(pages.split("-")[0]);
				int max = Integer.parseInt(pages.split("-")[1]);
				pages = new Integer(max - min).toString();
			}
			
			// DOI
			doi = elements.select("[class=value").first().text().trim().toLowerCase();
			
			/*
			System.out.println(journal);
			System.out.println(volume);
			System.out.println(number);
			System.out.println(year);
			System.out.println(pages);
			System.out.println(doi);
			*/
			
			journaldocument.setJournal(journal);
			journaldocument.setVolume(volume);
			journaldocument.setNumber(number);
			journaldocument.setYear(year);
			journaldocument.setPages(pages);
			journaldocument.setDoi(doi);
			journaldocument.setTitle(title);
			
			
			/**
			 * Get the section headers and text
			 */
			elements = doc.select("[class=abstractText]").select("[class=AbstractSection]");
			if (elements.isEmpty())
			{
				elements = doc.select("[class=abstractText]").select("[class=Abstract");
				for (Element e: elements)
				{
					String section = "Abstract";
					String text = e.text();
					journaldocument.addSection(section, text);
				}
			}
			else
			{
				for(Element e: elements)
				{
					String section = e.getElementsByTag("h3").first().text().trim().toLowerCase();
					String text = e.getElementsByTag("div").first().text().substring(section.length()).trim().toLowerCase();
					
					journaldocument.addSection(section, text);
					
	//				System.out.println(section);
	//				System.out.println(text);
				}
			}
			// Check that there are any keywords
			if (doc.select("[class=abstractText]").select("[class=Keyword]").first() != null)
			{
				String[] keywords = doc.select("[class=abstractText]").select("[class=Keyword]").first().ownText().toLowerCase().trim().replace("key words", "").replace("keywords", "").split("-");
				for (String keyword: Arrays.asList(keywords))
				{
					journaldocument.addKeyword(keyword);
	//				System.out.println(keyword);
				}
				
	//			System.out.println(journaldocument.toString().replace(",", "\n"));
			}
			else
				System.out.println("No keywords found!");
		}
		catch (Exception e) 
		{
			System.out.println(e.toString());
			e.printStackTrace();
		}
		System.out.println();
		
		return journaldocument;
	}
	
	public JournalDocument abstractToJournalDocumentJSNP(String filename)
	{
		JournalDocument journaldocument = new JournalDocument();
		
		try
		{
			
			Document doc = Jsoup.parse(new File(filename), null);
			
			Elements elements;
			Element element;
			
			String journal;
			String volume;
			String number;
			String year;
			String pages;
			String doi;
			String category = "";
			String title;
			
			System.out.println(filename);
			
			
			/**
			 * Get category, title, and authors
			 */
			elements = doc.select("[id=main]");
			
			// Category
			if (elements.select("[class=title-article]").first() != null)
			category = elements.select("[class=title-article]").first().ownText().toLowerCase().trim();
			if (category.isEmpty())
				try {
				category = doc.select("[class=service_showAbstract]").select("[class=subtitle]").first().text().toLowerCase().trim();
				} catch (Exception e) {
					category = "";
					System.out.println("problem setting category");
					this.errors++;
				}
			System.out.println(category);
		    
			if (this.refusedCategories.contains(category))
			{
				this.refused++;
				System.out.println("refused " +category);
			}
			else if (category.isEmpty())
			{
				this.unknown++;
				System.out.println("unknown category!");
			}
			else
			{
				this.accepted++;
				System.out.println("accepted: " +category);
			}
			
					
			// Title
			title = elements.select("[class=service_showAbstract]").select("h3").first().ownText().toLowerCase().trim();
//			if (title.isEmpty())
//				title = elements.select("[title=Link to Article").first().text().toLowerCase().trim();
			
			// Authors
			
//			for (Element e: elements.select("[class=authors]").select("a"))
//			{
//				String author = e.ownText().toLowerCase().trim();
////				System.out.println(author);
//				journaldocument.addAuthor(author);
//			}
//			
			
			/**
			 * Get attributes
			 */
			
			elements = doc.select("[class=heading-article");
			
			// Journal
			//journal = elements.select("[title=Link to the Journal of this Article]").first().ownText().toLowerCase().trim();
			
			// year
			year = elements.select("span").select("em").first().text().split(" ")[1].toLowerCase().trim();
						
			// volume
			volume = elements.select("span").first().text().split("/")[1].split(" ")[2].toLowerCase().trim();
			
			// number
			number = elements.select("span").first().text().split("/")[2].split(" ")[2].toLowerCase().trim();
			
			// Pages
			if (elements.select("span").first().text().split("/").length > 3)
			{
				pages = elements.select("span").first().text().split("/")[3].split("(Pages|Page)")[1].toLowerCase().trim();
				if (pages.contains("-") && !pages.contains("a"))
				{
					int min = Integer.parseInt(pages.split("-")[0]);
					int max = Integer.parseInt(pages.split("-")[1]);
					pages = new Integer(max - min).toString();
				}
			}
			else
				pages = "0";
			
			// DOI
			//doi = elements.select("[class=value").first().text().trim().toLowerCase();
			
			/*
			System.out.println(journal);
			System.out.println(volume);
			System.out.println(number);
			System.out.println(year);
			System.out.println(pages);
			System.out.println(doi);
			*/
			
			journaldocument.setCategory(category);
			journaldocument.setVolume(volume);
			journaldocument.setNumber(number);
			journaldocument.setYear(year);
			journaldocument.setPages(pages);
			//journaldocument.setDoi(doi);
			journaldocument.setTitle(title);
			
			/*
			System.out.println(category);
			System.out.println(volume);
			System.out.println(number);
			System.out.println(year);
			System.out.println(pages);
			
			
			/**
			 * Get the section headers and text
			 */
			elements = doc.select("[class=service_showAbstract]").select("[class=secTitle]");
			if (elements.isEmpty())
			{
				String section = "Abstract";
				String text;
				if ((text = doc.select("[class=service_showAbstract]").select("[class=first last").text()).isEmpty())
					text = doc.select("[class=service_showAbstract]").select("[class=first").text() +" "+ doc.select("[class=service_showAbstract]").select("[class=first").text();
				text = text.trim().toLowerCase();	
				
				journaldocument.addSection(section, text);
				
				System.out.println(section);
				System.out.println(text);				
			}
			else
			{
				for (int i = 0; i < doc.select("[class=service_showAbstract]").select("[class=secTitle").size(); i++)
				{
					String section = doc.select("[class=service_showAbstract]").select("[class=secTitle]").get(i).text().trim().toLowerCase();
					String text = doc.select("[class=service_showAbstract]").select("[class=last]").get(i).text().trim().toLowerCase();
					
					journaldocument.addSection(section, text);
					
					System.out.println(section);
					System.out.println(text);
				}
			}
			// Check that there are any keywords
			elements = doc.select("[class=service_showAbstract").select("[class=container").select("[class=column").select("[class=box").select("a");
			if (elements != null && !elements.isEmpty())
			{
				for (Element e: elements)
				{
					String keyword = e.text().toLowerCase().trim();
					journaldocument.addKeyword(keyword);
					System.out.println(keyword);
				}
				
	//			System.out.println(journaldocument.toString().replace(",", "\n"));
			}
			else
				System.out.println("No keywords found!");
			
		}
		catch (IOException e) 
		{
			System.out.println(e.toString());
			e.printStackTrace();
			this.errors++;
			JournalUTIL.writeToFile("jnsp/errors.txt", filename+"\r\n"+e.getStackTrace().toString()+"\r\n", true);
		}
		System.out.println();
		
		return journaldocument;
		
	}
	
	public void journalDocumentToTextByYear(JournalDocument journalDocument, String outdir)
	{
		try
		{
			System.out.println("Starting document to text process...");
//			JournalUTIL.writeToFile(outdir+"category.txt", journalDocument.getCategory(), true);
			JournalUTIL.writeToFile(outdir+"titles-"+journalDocument.getYear()+".txt", journalDocument.getTitle(), true);
//			JournalUTIL.writeToFile(outdir+"titles.txt", journalDocument.getTitle(), true);
			/*
			for (String author: journalDocument.getAuthors())
			{
				JournalUTIL.writeToFile(outdir+"authors-"+journalDocument.getYear()+".txt", author, true);
			}
			*/
			
			for (String keyword: journalDocument.getKeywords())
			{
				JournalUTIL.writeToFile(outdir+"keywords-"+journalDocument.getYear()+".txt", keyword, true);
//				JournalUTIL.writeToFile(outdir+"keywords.txt", keyword, true);
			}
			
			for (String sectionText: journalDocument.getSections().values())
			{
				JournalUTIL.writeToFile(outdir+"text-"+journalDocument.getYear()+".txt", sectionText, true);
//				JournalUTIL.writeToFile(outdir+"text.txt", sectionText, true);
			}
			System.out.println("Finished document to text process...");
		}
		catch (Exception e)
		{
			System.out.println("Error: Skipping " + journalDocument.getDoi());
			e.printStackTrace();
		}
	}
	
	public void journalDocumentToText(JournalDocument journalDocument, String outdir)
	{
		try
		{
			System.out.println("Starting document to text process...");
			
			
//			JournalUTIL.writeToFile(outdir+"category.txt", journalDocument.getCategory(), true);
			JournalUTIL.writeToFile(outdir+"titles-"+journalDocument.getYear()+".txt", journalDocument.getTitle(), true);
//			JournalUTIL.writeToFile(outdir+"titles.txt", journalDocument.getTitle(), true);
			/*
			for (String author: journalDocument.getAuthors())
			{
				JournalUTIL.writeToFile(outdir+"authors-"+journalDocument.getYear()+".txt", author, true);
			}
			*/
			
			for (String keyword: journalDocument.getKeywords())
			{
				JournalUTIL.writeToFile(outdir+"keywords-"+journalDocument.getYear()+".txt", keyword, true);
//				JournalUTIL.writeToFile(outdir+"keywords.txt", keyword, true);
			}
			
			for (String sectionText: journalDocument.getSections().values())
			{
				JournalUTIL.writeToFile(outdir+"text-"+journalDocument.getYear()+".txt", sectionText, true);
//				JournalUTIL.writeToFile(outdir+"text.txt", sectionText, true);
			}
			System.out.println("Finished document to text process...");
		}
		catch (Exception e)
		{
			System.out.println("Error: Skipping " + journalDocument.getDoi());
			e.printStackTrace();
		}
	}
	
	public void journalDocumentToTextFormat2(JournalDocument journalDocument, String outdir)
	{
		try
		{
			System.out.println("Starting document to text process...");
			
			// journal
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "<journaldocument>", true);
			
//			// Set the doi xml tags
//			journalDocument.setDoi("<doi>"+journalDocument.getDoi()+"</doi>");			
//			// Write the doi 
//			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", journalDocument.getDoi(), true);
			
			// Set the title xml tags
			journalDocument.setTitle("<title>"+journalDocument.getTitle()+"</title>");			
			// Write the title 
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", journalDocument.getTitle(), true);
			
//			// Set the category xml tags
//			journalDocument.setCategory("<category>"+journalDocument.getCategory()+"</category>");			
//			// Write the title 
//			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", journalDocument.getCategory(), true);
						
			
			// authors
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "<authors>", true);
			for (String author: journalDocument.getAuthors())
			{
				JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", author, true);
			}
			// authors
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "</authors>", true);
			
			// keywords
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "<keywords>", true);
			for (String keyword: journalDocument.getKeywords())
			{
				JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", keyword, true);
//				JournalUTIL.writeToFile(outdir+"keywords.txt", keyword, true);
			}
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "</keywords>", true);
			
			// sections
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "<sections>", true);
			for (String sectionText: journalDocument.getSections().values())
			{
				JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", sectionText, true);
//				JournalUTIL.writeToFile(outdir+"text.txt", sectionText, true);
			}
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "</sections>", true);
			
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "</journaldocument>", true);
			JournalUTIL.writeToFile(outdir+journalDocument.getYear()+".txt", "", true);
			System.out.println("Finished document to text process...");
		}
		catch (Exception e)
		{
			System.out.println("Error: Skipping " + journalDocument.getDoi());
			e.printStackTrace();
		}
	}
	
	/*
	 * Serialize the JournalDocument to XML
	 */
	public boolean journalDocumentToXML(JournalDocument journaldocument, String outdir)
	{
		String filename = journaldocument.getYear() + "-" +
				  journaldocument.getVolume() + "-" +
				  journaldocument.getNumber() + "_" +
				  journaldocument.getDoi().substring(journaldocument.getDoi().indexOf("/") + 1) + 
				  ".xml";
		
		try 
		{
			
					  
			XMLEncoder xmle = new XMLEncoder(
								new BufferedOutputStream(
									new FileOutputStream(outdir + filename)));
			
			xmle.writeObject(journaldocument);
			xmle.close();
	
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return (new File(outdir+filename).length() > 1);
	}
	
	public boolean journalDocumentToXStream(JournalDocument journaldocument, String outdir)
	{
		if (journaldocument == null)
			return false;
		String filename = outdir +
				  journaldocument.getYear() + "-" +
				  journaldocument.getVolume() + "-" +
				  journaldocument.getNumber() + "_" +
				  journaldocument.getDoi().substring(journaldocument.getDoi().indexOf("/") + 1) + 
				  ".xml";
		
		XStream xstream = new XStream();
		xstream.alias("JournalDocument", JournalDocument.class);
		
//		xstream.addImplicitCollection(JournalDocument.class, "authors", "author", String.class);
//		xstream.aliasField("authors", JournalDocument.class, "authors");
		
//		xstream.addImplicitMap(JournalDocument.class, "sections", "section", String.class, "text");
		
		String xml = xstream.toXML(journaldocument);
		System.out.println(xml);
		System.out.println(filename);
		JournalUTIL.writeToFile(filename, xml, true);
		return true;
	}
	
}
