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
import java.util.Arrays;

public final class JournalUTIL 
{
	
	public static ArrayList<File> getAllFiles(String dir)
	{
		File folder = new File(dir);
		ArrayList<File> list = new ArrayList<File>();
		list.addAll(Arrays.asList(folder.listFiles()));
		return list;
	}
	
	public static void showList(ArrayList<?> list)
	{
		for (Object o: list)
		{
			System.out.println(o.toString());
		}
		System.out.println(list.size());
	}
	
	public static boolean writeToFile(String filename, String text, boolean append)
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
				return false;
			}
		}
		return true;
	}
	
	public static BufferedReader getStream(String filename)
	{
		try
		{
			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			return br;
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
			return null;
	}
	
}
