package search_engine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;

public class Translator {
	String directory = System.getProperty("user.dir");
	public void translate_to_text(File files) throws IOException
	{
		PrintWriter print_text;
		String result = Jsoup.parse(files, "ISO-8859-1").select("body").text();
    	String filename = files.getName();
    	print_text = new PrintWriter(directory+"\\Translated files\\"+filename.substring(0,filename.length()-4 )+".txt");
        print_text.println(result);
        print_text.close();
	}
	
}
