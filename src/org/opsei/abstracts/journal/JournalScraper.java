package org.opsei.abstracts.journal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import com.thoughtworks.selenium.DefaultSelenium;

public class JournalScraper 
{
	DefaultSelenium selenium;
	ArrayList<String> list;
	ArrayList<String> abstractLinks;
	ArrayList<Element> filteredList;
	String timeout = "360000";
	String infile;
	public int count = 0;
	
	public JournalScraper()
	{
		filteredList = new ArrayList<Element>();
	}
	
	/**
	 * @return the list
	 */
	public ArrayList<String> getList() {
		return list;
	}

	/**
	 * @return the abstractLinks
	 */
	public ArrayList<String> getAbstractLinks() {
		return abstractLinks;
	}

	/**
	 * @return the filteredList
	 */
	public ArrayList<Element> getFilteredList() {
		return filteredList;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	/**
	 * @param abstractLinks the abstractLinks to set
	 */
	public void setAbstractLinks(ArrayList<String> abstractLinks) {
		this.abstractLinks = abstractLinks;
	}

	/**
	 * @param filteredList the filteredList to set
	 */
	public void setFilteredList(ArrayList<Element> filteredList) {
		this.filteredList = filteredList;
	}

	public ArrayList<String> initializeList(String filename)
	{
		try 
		{
			list = new ArrayList<String>();
			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)  
			{
				list.add(strLine);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<String> initAbstractLinks(String filename)
	{
		try 
		{
			this.infile = filename;
			abstractLinks = new ArrayList<String>();
			FileInputStream fstream = new FileInputStream(infile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)  
			{
				abstractLinks.add(strLine);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return abstractLinks;
	}
	
	/*
	 * 
	 */
	public void extractAbstracts()
	{
		String dir = "jnsp/abstracts-html/";
		System.out.println();
		for (String s: abstractLinks)
		{
			String strFile = s.substring(1).replace("/", "-") + ".html";
			if ((new File(dir+strFile)).exists())
			{
				
				System.out.println(strFile + " already exists.");
			}
			else
			{
				selenium.open(s);
				selenium.waitForPageToLoad(timeout);
				String htmlSource = selenium.getHtmlSource();		
				writeToFile(dir+strFile, htmlSource, false);
				//System.out.println(strFile);
			}
		}
		System.out.println();
	}
	
	/*
	 * 
	 */
	public void extractAbstractsCNS(ArrayList<String> list, String outdir)
	{
		for (String link: list)
		{
			String outfile =  outdir + link.substring(1, link.length() - 1).replace("/", "-") + ".html";
			if ((new File(outfile)).exists())
			{
				System.out.println(outfile + " already exists.");
			}
			else
			{
				selenium.open(link);
				selenium.waitForPageToLoad(timeout);
				String htmlSource = selenium.getHtmlSource();		
				writeToFile(outfile, htmlSource, false);
				System.out.println(outfile);
			}
		}
		System.out.println();
	}
	
	public ArrayList<String> initIssues(String infile)
	{
		try 
		{
			this.infile = infile;
			list = new ArrayList<String>();
			FileInputStream fstream = new FileInputStream(infile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)  
			{
				list.add(strLine);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void setupSelenium(String domain)
	{
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", domain);
		selenium.start();
		selenium.setTimeout(timeout);
	
		selenium.open("/");
		selenium.waitForPageToLoad(timeout);
	}
	
	public void scrapeIssues(String dir)
	{
		for (int i = 0; i < list.size(); i++)
		{
			String link = list.get(i);
			String outfile = dir + link.substring(1).replace("/", "-").concat(".html");
			if (fileExists(outfile))
			{
				System.out.println(outfile + " already exists.");
			}
			else
			{
				selenium.open(link);
				selenium.waitForPageToLoad(timeout);
				String htmlSource = selenium.getHtmlSource();		
				writeToFile(outfile, htmlSource, false);
			}
		}
	}
	
	public void removeFalseAbstracts(String filename)
	{
		try
		{
			Document doc = Jsoup.parse(new File(filename), null);
//			Elements links = doc.select("[class=title-article]");
			Elements links = doc.select("[class=abstractTitle]");
			for (Element e: links)
			{
				System.out.println(filename + " = " + e.text());
			}
			//System.out.println(links.size());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Element> filterAbstracts(String filename, String selectPattern)
	{
		try
		{
			Document doc = Jsoup.parse(new File(filename), null);
//			Elements links = doc.select("[class=title-article]");
//			Elements links = doc.select("[class=abstractTitle]");
//			Elements links = doc.select(selectPattern);
			Elements links = doc.select("[class=service_showAbstract]").select("h3");
			if (!links.isEmpty())
				filteredList.addAll(links);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return filteredList;
	}
	
	/*
	 * 
	 */
	public void extractAbstractLinks(String filename)
	{		
		String dir = "jnsp/abstract-links/";
		String file = filename.replace(".html", "-abstract-links.txt").replace("jnsp/issues-html/", "");
				
		if (!fileExists(filename))
		{
			System.out.println(filename + " does not exist.");	
		}
		else
		{
			// look for the abstract links
			try 
			{
				Document doc = Jsoup.parse(new File(filename), null);
				Elements links = doc.select("a");
				for (Element e: links)
				{
					String temp = e.attr("href");
//					if (temp.contains("/doi/abs/"))
					if (e.text().contains("Abstract"))
					{
						System.out.println(temp);
						writeToFile(dir+file, temp, true);
					}					
				}
				
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
	}
	
	public void extractAbstractLinksCNS(String outfile, String infile)	
	{			
		try 
		{
			Document doc = Jsoup.parse(new File(infile), null);			
			Elements links = doc.select("[class*=primitive contribution journalArticle]").select("a");

			for (Element e: links)
			{
				String link = e.attr("href");
				if (link != null && !link.contains("springer") && link.endsWith("/"))
				{
					System.out.println(link);
					writeToFile(outfile, link, true);
					this.count++;
				}
				else
					System.out.println("INVALID link");
//				
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 */
	public void extractIssues(String infile, String outfile) 
	{
		try
		{
			  FileInputStream fstream = new FileInputStream(infile);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  int count = 0;
			  while ((strLine = br.readLine()) != null)  
			  {
				  if (strLine.contains("http://thejns.org/toc/ped"))
				  {
					  // extract the links
					  int start = strLine.indexOf("http://thejns.org/toc/ped");
					  int end = strLine.indexOf(" class=");
					  String link = strLine.substring(start, end - 1);
					  link = link.substring(link.indexOf("/toc/ped"));
					  
					  //System.out.println(start + " " + end + " " + strLine);
					  System.out.println(link);
					  count++;
					  list.add(link);
					  
					  // write the links to file
					  writeToFile(outfile, link, true);
				  }
			  }
			  System.out.println(count);
			  in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	/*
	 * 
	 */
	public void extractIssuesCNS(String infile, String outfile) 
	{
		try 
		{
			Document doc = Jsoup.parse(new File(infile), null);
			Elements links = doc.select("[href~=/content/");
//			Elements links = doc.select("[href~=/content/\n[4]-\n[4]/\n*");
			
			for (Element e: links)
			{
				String str = e.attr("href");
//				int volume = Integer.parseInt(str.substring(str.indexOf("/", 17), str.indexOf("/", 18)));
				int volume = Integer.parseInt(str.substring(19, str.indexOf("/", 20)));
//				System.out.println(volume);
				if (volume < 13 || str.length() > 22)
				{
					// remove the last slash
					str = str.substring(0, str.length() -1);
					System.out.println(str);
					writeToFile(outfile, str, true);
				}
				else
					System.out.println(str + " is a volume not an issue");
			}
			
			
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		/*
		try
		{
			  FileInputStream fstream = new FileInputStream(infile);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  int count = 0;
			  while ((strLine = br.readLine()) != null)  
			  {
				  if (strLine.contains("http://thejns.org/toc/ped"))
				  {
					  // extract the links
					  int start = strLine.indexOf("http://thejns.org/toc/ped");
					  int end = strLine.indexOf(" class=");
					  String link = strLine.substring(start, end - 1);
					  link = link.substring(link.indexOf("/toc/ped"));
					  
					  //System.out.println(start + " " + end + " " + strLine);
					  System.out.println(link);
					  count++;
					  list.add(link);
					  
					  // write the links to file
					  writeToFile(outfile, link, true);
				  }
			  }
			  System.out.println(count);
			  in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		*/
	}
	
	public void writeToFile(String filename, String text, boolean append)
	{
		boolean exists = (new File(filename)).exists();
		if (exists && !append) 
		{
		    System.out.println(filename + " already exists");
		}
		else 
		{
		    try
			{
				System.out.println("Writing file " + filename);
				FileWriter fstream = new FileWriter(filename, append);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(text);
				out.newLine();
				out.close();
				
			} 
			catch (IOException e)
			{
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
	}
	
	public boolean fileExists(String filename)
	{
		return (new File(filename)).exists();
	}
}