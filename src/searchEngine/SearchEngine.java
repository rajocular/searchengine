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

	
	public static void main(String[] args) throws IOException, FileNotFoundException, NoSuchElementException, NullPointerException
	{
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
	}
}
