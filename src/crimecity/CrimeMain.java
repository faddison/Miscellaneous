package crimecity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CrimeMain
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try 
		{
			Keyboard kb = new Keyboard();
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream("crimecitycodes3.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  ArrayList<String> values = new ArrayList<String>();
			  System.out.println("Reading mafia codes...");
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   
			  {
				  // Print the content on the console
				  values.add(strLine);
			  }
			  //Close the input stream
			  in.close();
			  
			  System.out.println("Preparing to enter codes...");
			  Thread.sleep(3000);
			  for (int i = 0; i < values.size(); i++)
			  {
				  String s = values.get(i);
				  System.out.println(i+"/"+values.size());
				  kb.type(s);
				  Thread.sleep(250);
				  kb.type("\n");
				  Thread.sleep(250);
				  kb.type("\n");
				  Thread.sleep(1500);
			  }
				  
			  
			  
		    }
			catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  
		    }
			  
	}

}
