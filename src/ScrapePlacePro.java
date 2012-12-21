import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.thoughtworks.selenium.DefaultSelenium;


public class ScrapePlacePro 
{

	DefaultSelenium selenium;
	public int min = 1;
	public int max = 6000;
	public int count = 0;
	
	public ScrapePlacePro(){}
	
	public void scrub() 
	{
		new File("company-descriptions.txt").delete();
		for (int i = min; i < max; i++)
		{
			writeToFile("company-descriptions.txt", "-----------------------------------------", true);
			String filename = "company-" + i + ".html";
			File file = new File(filename);
			// check if company exists
			if (file.length() > 500)
			System.out.println();
			{
				//
				//Job Preferences
				 
				try
				{
					  FileInputStream fstream = new FileInputStream(filename);
					  // Get the object of DataInputStream
					  DataInputStream in = new DataInputStream(fstream);
					  BufferedReader br = new BufferedReader(new InputStreamReader(in));
					  String strLine;
					  //Read File Line By Line
					  while ((strLine = br.readLine()) != null)  
					  {
						  if (strLine.contains("Company Description"))
						  {
							  System.out.println(i + " - " + count);
							  count++;
							  
							  while (!(strLine = br.readLine()).contains("Job Preferences"))
							  {
								  System.out.println(strLine);
								  writeToFile("company-descriptions.txt", strLine, true);
							  }
						  }
						  
					  }
					  in.close();
					  
		    	}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		System.out.println("Count: " + count);
	}

	public void scrape()
	{
		try 
		{
			System.out.println("Executing scrape");
			setUp();
			login();
			extractCompanies();
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://placeprocanada.com/");
		selenium.start();
	}
	
	public void login()
	{
		System.out.println("Logging in");
		selenium.open("/students");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=txtLogin", "faddison");
		selenium.type("id=txtPassword", "Placie1!");
		selenium.type("id=txtAccessCode", "ubcstudent");
		selenium.click("id=btnLogin");
		selenium.waitForPageToLoad("30000");
	}
	
	public void extractCompanies()
	{
		for (int i = min; i < max; i++)
		{
			System.out.println("Finding company " + i);
			selenium.open("/students/JobDetails.aspx?CID=" + i);
			selenium.waitForPageToLoad("30000");
			String text = selenium.getBodyText();
			writeToFile("company-" + i + ".html", text, false);
		}
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

}
