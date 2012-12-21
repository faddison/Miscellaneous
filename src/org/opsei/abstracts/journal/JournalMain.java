package org.opsei.abstracts.journal;

import java.io.File;
import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JournalMain {

	static String file1;
	static String file2;
	static String file3;
	static String file4;
	static JournalScraper jscraper;
	static JournalParser jparser;
	static ArrayList<String> issues;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		file1 = "D:/code/jns-surgery/all-issues-html-links.html";
		file2 = "jnsp/jnsp-curl-link.txt";
		file3 = "cns/main-page-trimmed.html";
		file4 = "cns/cns-issues-links.txt";
		jscraper = new JournalScraper();
		jparser = new JournalParser();
		
//		extractIssuesCNS();
//		scrapeIssuesCNS();
//		extractAbstractLinksCNS("cns/issues-html", "cns/cns-abstract-links.txt");
//		extractAbstractsCNS("cns/cns-abstract-links.txt", "cns/abstracts-html/", "http://springerlink.com");
//		parseSingleSelect("cns/abstracts-html/", "[class=articleCategory]");
//		filterAbstractsByType("cns/abstracts-html/", "[class=articleCategory]", 
//										"original paper, original papers, " +
//										"case report, case reports" +
//										"invited paper, invited papers");

//		jparser.countTypes("jnsp/abstracts-text/category.txt");
//		extractAbstractText("cns/abstracts-html/");
//		abstractToDocument("cns/abstracts-html/");
//		abstractToXML("cns/abstracts-html/", "cns/abstracts-xml/");
//		abstractToXStream("cns/abstracts-html/", "cns/abstracts-xml/");
//		abstractToText("jnsp/abstracts-html/", "jnsp/abstracts-text/");
//		abstractToText("cns/abstracts-html/", "cns/abstracts-text/");
//		abstractToTextFormat2("cns/abstracts-html/", "cns/abstracts-text-format2/");
		try{
		abstractToTextFormat2("jnsp/abstracts-html/", "jnsp/abstracts-text-format2/");
		} catch(Exception e) { e.toString();}
//		extractIssues();
//		scrapeIssues();
//		extractAbstractLinks();
//		extractAbstracts("/jnsp/abstract-links/");
//		removeAbstracts();
//		filterAbstracts();
		
	}
	
	public static void abstractToXStream(String indir, String outdir)
	{
		JournalDocument journaldocument = new JournalDocument();
		
		for (File f: JournalUTIL.getAllFiles(indir))
		{
			journaldocument = jparser.abstractToJournalDocument(f.getPath());
			if (jparser.journalDocumentToXStream(journaldocument, outdir))
				System.out.println("XML Created: " + f.getName());
			else 
				System.out.println("ERROR creating XML!");
		}
	}
	
	public static void abstractToText(String indir, String outdir)
	{
//		new File(outdir+"authors.txt");
//		new File(outdir+"keywords.txt");
//		new File(outdir+"text.txt");
		
		JournalDocument journaldocument = new JournalDocument();
		
		for (File f: JournalUTIL.getAllFiles(indir))
		{
			if (indir.contains("jnsp"))
				journaldocument = jparser.abstractToJournalDocumentJSNP(f.getPath());
			else
				journaldocument = jparser.abstractToJournalDocument(f.getPath());
			if (journaldocument == null)
				System.out.println("Journal Document was null");
			else
				jparser.journalDocumentToText(journaldocument, outdir);
		}
		System.out.println(jparser.unknownCategories.toString().replace(",", ",\n"));
		System.out.println("accepted: " + jparser.accepted);
		System.out.println("refused: " + jparser.refused);
		System.out.println("unknown: " + jparser.unknown);
		System.out.println("errors: " + jparser.errors);
		
	
	}
	
	public static void abstractToTextFormat2(String indir, String outdir)
	{
//		new File(outdir+"authors.txt");
//		new File(outdir+"keywords.txt");
//		new File(outdir+"text.txt");
		
		JournalDocument journaldocument = new JournalDocument();
		
		for (File f: JournalUTIL.getAllFiles(indir))
		{
			if (indir.contains("jnsp"))
				journaldocument = jparser.abstractToJournalDocumentJSNP(f.getPath());
			else
				journaldocument = jparser.abstractToJournalDocument(f.getPath());
			if (journaldocument == null)
				System.out.println("Journal Document was null");
			else
				jparser.journalDocumentToTextFormat2(journaldocument, outdir);
		}
		System.out.println(jparser.unknownCategories.toString().replace(",", ",\n"));
		System.out.println("accepted: " + jparser.accepted);
		System.out.println("refused: " + jparser.refused);
		System.out.println("unknown: " + jparser.unknown);
		System.out.println("errors: " + jparser.errors);
		
	
	}
	
	public static void abstractToXML(String indir, String outdir)
	{
		JournalDocument journaldocument = new JournalDocument();
		
		for (File f: JournalUTIL.getAllFiles(indir))
		{
			journaldocument = jparser.abstractToJournalDocument(f.getPath());
			if (jparser.journalDocumentToXML(journaldocument, outdir))
				System.out.println("XML Created: " + f.getName());
			else 
				System.out.println("ERROR creating XML!");
		}
	}
	
	public static void extractAbstractText(String dir)
	{
		for (File f: JournalUTIL.getAllFiles(dir))
		{
			jparser.extractAbstractText(f.getPath());
			System.out.println("----------------------");
		}
	}
	
	public static void abstractToDocument(String dir)
	{
		for (File f: JournalUTIL.getAllFiles(dir))
		{
			jparser.abstractToJournalDocument(f.getPath());
			System.out.println("----------------------");
		}
	}
	
	public static void AbstractToObject()
	{
		
	}
	public static void filterAbstractsByType(String dir, String selectQuery, String typesCSV)
	{
		ArrayList<String> fileList = new ArrayList<String>();
		for (File f: JournalUTIL.getAllFiles(dir))
		{
			String file = jparser.filterAbstractsByType(f.getPath(), selectQuery, typesCSV);
			if (file != null)
				fileList.add(file);
		}
		
//		JournalUTIL.showList(fileList);
	}
	
	public static void parseSingleSelect(String dir, String selectQuery)
	{
		ArrayList<Element> elementList = new ArrayList<Element>();
		for (File f: JournalUTIL.getAllFiles(dir))
		{
			elementList = jparser.singleSelect(f.getPath(), selectQuery);
			if (elementList != null)
				jparser.getElementText(elementList);
		}
		
		jparser.showErrors();
	}
	
	public static void extractAbstracts(String dir)
	{
		jscraper.setupSelenium("http://thejns.org");
		
		for (File f: JournalUTIL.getAllFiles(dir))
		{
			System.out.println(f.getPath());
			jscraper.initAbstractLinks(f.getPath());
			jscraper.extractAbstracts();
		}
	}
	
	public static void extractAbstractsCNS(String infile, String outdir, String domain)
	{
		jscraper.setupSelenium(domain);
		ArrayList<String> list = jscraper.initializeList(infile);
		jscraper.extractAbstractsCNS(list, outdir);
	}
	
	public static void removeAbstracts()
	{
		String path = "./jnsp/abstracts-html/";
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				files = listOfFiles[i].getName();
				//System.out.println(files);
				
				jscraper.removeFalseAbstracts(path+files);
			}
		}
		
	}
	
	/*
	 * Extract the link to each abstract per issue
	 */
	public static void extractAbstractLinks()
	{
		issues = jscraper.initIssues(file2);
		for (int i = 0; i < issues.size(); i++)
		{
			String link = issues.get(i);
			String outfile = "jnsp/issues-html/" + link.replace("/", "-").substring(1) + ".html";
			jscraper.extractAbstractLinks(outfile);
		}
	}
	
	/*
	 * Extract the link to each abstract per issue
	 */
	public static void extractAbstractLinksCNS(String indir, String outfile)
	{
		for (File f: JournalUTIL.getAllFiles(indir))
		{
			jscraper.extractAbstractLinksCNS(outfile, f.getPath());
		}
		System.out.println(jscraper.count);
	}
	
	/*
	 * Scrape all the issues for table of contents (abstract links)
	 */
	public static void scrapeIssues()
	{
		issues = jscraper.initIssues(file2);
		jscraper.setupSelenium("http://thejns.org");
		jscraper.scrapeIssues("jnsp/issues-html/");
	}
	
	public static void scrapeIssuesCNS()
	{
		issues = jscraper.initIssues(file4);
		jscraper.setupSelenium("http://springerlink.com");
		jscraper.scrapeIssues("cns/issues-html/");
	}
	
	public static void filterAbstracts()
	{
		ArrayList<Element> filteredList;
		// Jsoup doc.Select() Patterns
		String selectPattern = "[class=abstractTitle]";
		
		String path = "./jnsp/abstracts-html/";
		String filename;
		for (File f: JournalUTIL.getAllFiles(path))
		{
			System.out.println(f.getPath());
			filteredList = jscraper.filterAbstracts(f.getPath(), selectPattern);
			
			for (Element e: filteredList)
			{
				System.out.println(f.getPath());
				System.out.println(e.toString());
				System.out.println();
			}
		}
	}
	
	/*
	 * Extract the list of links of all issues
	 */
	public static void extractIssues()
	{
		jscraper.extractIssues(file1, file2);
	}
	
	public static void extractIssuesCNS()
	{
		jscraper.extractIssuesCNS(file3, file4);
	}

}
