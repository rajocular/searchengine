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
	
	static File final_file = new File("Results.html");
	
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
	    
//	    Retrieve user input
	    Scanner sc = new Scanner(System.in);
	    System.out.println("Enter keyword:");
	    String search = sc.nextLine();
	    sc.close();
	    
//	    creating a HTML file to reflect the search results
	    if(final_file.exists()) final_file.delete();
	    
	    
	    
	}
}
