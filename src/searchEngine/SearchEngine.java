package searchEngine;

//necessary packages are imported here
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;

import files.*;

public class SearchEngine {

	static String search;
	static String[] search2;
	static String str;
	static int freq;
	
	static File final_file = new File("Results.html");
	
	static Hashtable<String, Integer> search_table = new Hashtable<String,Integer>();
	
	public static void main(String[] args) throws IOException, FileNotFoundException, NoSuchElementException, NullPointerException
	{
//		Translating the webpages for one time
		File file_dir = new File("webpages");
	    File[] sourcefiles = file_dir.listFiles();
	    file_dir = new File("Translated pages");
	    if(!file_dir.exists())
	    {
	    	file_dir.mkdir();
	    	Translator trans = new Translator();
	    	for( int h =0 ;h<sourcefiles.length;h++)
		    {
		     	trans.translate_to_text(sourcefiles[h]);
		    }
	    }
	    File[] files = file_dir.listFiles();
	    
//	    Retrieve user input
	    Scanner sc = new Scanner(System.in);
	    System.out.println("Enter keyword:");
	    String search = sc.nextLine();
	    sc.close();
	    
//	    creating a HTML file to reflect the search results
	    if(final_file.exists()) final_file.delete();
	    
//	    search begins here
	    int index=search(files,search_table,search);
	    
	}
	
	public static int search(File[] files, Hashtable<String, Integer> search_table, String search) throws IOException
	{
		search2 = search.split("\\W+");
		
//		to store the word frequencies
		File f = new File("content.txt");
		if(f.exists()) f.delete();
		
		PrintStream print_tree = new PrintStream(new FileOutputStream(f));
	    PrintStream console = System.out;
	    In input;
		int v,len,sum=0,max=0,index=-1;
		for(int j=0;j<files.length;j++)
		{
			input = new In(files[j]);
			v=0;
			len=0;
			try{
				BufferedReader br = new BufferedReader(new FileReader(files[j]));
				String s = br.readLine();
		    	while(s!=null)
		    	{
		    		StringTokenizer sto = new StringTokenizer(s);   
		    		while(sto.hasMoreTokens())
		        	{
		    			str = sto.nextToken().toLowerCase();
		    			if(str.matches("[a-zA-Z][a-zA-Z0-9]+"))
						{
		    				if(!(str.equalsIgnoreCase("what")||	str.equalsIgnoreCase("is")||str.equalsIgnoreCase("the")||str.equalsIgnoreCase("of")||str.equalsIgnoreCase("and")||str.equalsIgnoreCase("to")||str.equalsIgnoreCase("if")||str.equalsIgnoreCase("this")||str.equalsIgnoreCase("must")||str.equalsIgnoreCase("such")||str.equalsIgnoreCase("each")||str.equalsIgnoreCase("that")||str.equalsIgnoreCase("in")||str.equalsIgnoreCase("for")||str.equalsIgnoreCase("by")||str.equalsIgnoreCase("be")||str.equalsIgnoreCase("from")||str.equalsIgnoreCase("you")||str.equalsIgnoreCase("not")||str.equalsIgnoreCase("we")||str.equalsIgnoreCase("as")||str.equalsIgnoreCase("are")||str.equalsIgnoreCase("with")||str.equalsIgnoreCase("any")||str.equalsIgnoreCase("or")||str.equalsIgnoreCase("above")||str.equalsIgnoreCase("can")||str.equalsIgnoreCase("also")||	str.equalsIgnoreCase("an")||str.equalsIgnoreCase("it")||str.equalsIgnoreCase("but")||str.equalsIgnoreCase("at")||str.equalsIgnoreCase("use")||str.equalsIgnoreCase("have")||str.equalsIgnoreCase("will")||str.equalsIgnoreCase("do")||str.equalsIgnoreCase("more")||str.equalsIgnoreCase("yes")||str.equalsIgnoreCase("open")||	str.equalsIgnoreCase("may")||str.equalsIgnoreCase("which")||str.equalsIgnoreCase("on")||str.equalsIgnoreCase("all")	))

		    				{
		    					if(search_table.containsKey(str))
								{
									freq = search_table.get(str);
									search_table.put(str, ++freq);
								}
								else search_table.put(str, 1);
		    				}
		    				
						}
		        	}	        	
		        	s=br.readLine();
		    	}
				br.close();
			}
			catch(NoSuchElementException ne) {}
			for(v=0;v<search2.length;v++)
			{
				
				for(String key : search_table.keySet())
				{
					if(search2[v].equals(key))
					{
						len++;
						sum += search_table.get(key);
						break;
					}
				}	
			}
			if(len==search2.length) 
			{	
				if(sum>max)
				{
					max=sum;
					index = j;
				}
			}
	        for(String key:search_table.keySet())
	        {
	        	System.setOut(print_tree);
	        	System.out.println(key+" "+(int)search_table.get(key)+" "+files[j].getName());
	        	System.setOut(console);
	        }
	        sum = 0;
	        len=0;
			search_table.clear();
		}
		return index;
	}
}
