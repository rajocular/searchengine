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
	static File freq_file = new File("Frequency.html");
	
	static PrintStream output_html;
	static PrintStream print_html;
	
	static Hashtable<String, Integer> search_table = new Hashtable<String,Integer>();
	static Hashtable<String, String> relate_table = new Hashtable<>();
	
	static String directory =System.getProperty("user.dir");
	static File file_dir = new File(directory+"\\Translated pages\\");
    static File[] files = file_dir.listFiles(); 
    
    static File file_dy = new File("webpages");
    static File[] sourcefiles = file_dy.listFiles();
	
	public static void main(String[] args) throws IOException, FileNotFoundException, NoSuchElementException, NullPointerException
	{
//		Translating the webpages for one time
		
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
	    if(index!=-1)
		{
			String name = files[index].getName();
			output_html = new PrintStream(new FileOutputStream(final_file));
			output_html.println("<html>\n"
					+ "<title>Search</title>"
					+ "<head>"
					+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"style/bootstrap.css \" />"
					+ "</head>"
					+ "<body>"
					+ "<div class=\"container text-center align-top\">"
					+ "<h3>Keyword: <i>"+search+"</i></h3></div><h3>Best Match: ");
			output_html.print("<a href=\""+sourcefiles[index]+"\">");
			output_html.print(name.substring(0, name.length()-4 )+"</a></h3><br><hr>");
			webpageranking(search,search_table,0);

		}
	    else
		{
			
			related(files,search_table,relate_table,search2);	
		}
	    
//	    to calculate frequencies of every word
	    frequency(files);
	    output_html.print("<p>To see top three words in every page <a href=\"Frequency.html\">Click here</a></p>");
	    
	    File htmlFile = new File("Results.html");
		Desktop.getDesktop().browse(htmlFile.toURI());
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
	public static void related(File[] files, Hashtable<String, Integer> search_table, Hashtable<String, String> relate_table, String[] search2) throws IOException 
	{
		int d,v,min=0,match=0,j=0;
		int length=0, flag=0;
		int[] val = new int[1000000];
		String[] word = new String[1000000];
		String[] frequency = new String[1000000];
		String[] url = new String[1000000];
		Hashtable<String,String> rela = new Hashtable<>();
		In inp = new In("content.txt");
		while(inp.hasNextLine())
		{
			word[length]=inp.readString();
			frequency[length]=inp.readString();
			url[length]=(String)inp.readLine();
			length++;
		}
		
//		editDistance is used to find related words
		for(v=0;v<search2.length;v++)
		{	
			min = search2[v].length();
			for(int i=0;i<length;i++)
			{
				d=Sequences.editDistance(search2[v],word[i]);
				if(d<=min/2)
				{
					min=d;
					val[j] = i;
					j++;
					match=i;
				}
			}		
			if(word[match].length()>=min)
			{
				relate_table.put(word[match], url[match]);
			}
		}
		
//		related search begins here
		for(v=0;v<j;v++)
			if(word[val[v]].length()>=min-1)
				rela.put( word[val[v]],url[val[v]]);
		String newkeyword = null,new_keyword=null;
		for(String s : relate_table.keySet())
		{
			if(newkeyword==null) newkeyword=s;
			else	newkeyword+=" "+s;
		}	
		if(match==0)
			flag = -1;
		
		if(flag == -1)
		{
			output_html = new PrintStream(new FileOutputStream(final_file));
			output_html.println("<html>\n"
					+ "<title>Search</title>"
					+ "<head>"
					+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"style/bootstrap.css \" />"
					+ "</head>"
					+ "<body>"
					+ "<div class=\"container text-center align-top\">"
					+ "<h3>No results Found..! Try a different Keyword</h3>");
			
		}
		else
		{
			int index = search(files,search_table,newkeyword);
			String[] w = newkeyword.split(" ");
			for(int g = w.length-1;g>=0;g--)
				
			{
				if(new_keyword==null) new_keyword=w[g];
				else	new_keyword+=" "+w[g];
			}
			if(index==-1)
			{
				String[] f = new_keyword.split(" ");
				output_html = new PrintStream(new FileOutputStream(final_file));
				
				for(v=0;v<f.length;v++)
				{
					int s=search(files,search_table,f[v]);
					String name = files[s].getName();
					output_html.println("<html>\n"
							+ "<title>Search</title>"
							+ "<head>"
							+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"style/bootstrap.css \" />"
							+ "</head>"
							+ "<body>"
							+ "<div class=\"container text-center align-top\">"
							+ "<h3>Did you Mean: <i>"+new_keyword+"</i></h3></div><h3>Best Match: ");
					output_html.print("<a href=\""+sourcefiles[s]+"\">");
					output_html.print(name.substring(0, name.length()-4)+"</a>");		
				}
			}
			else
			{
				output_html = new PrintStream(new FileOutputStream(final_file));
				String name = files[index].getName();
			
				output_html.println("<html>\n"
						+ "<title>Search</title>"
						+ "<head>"
						+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"style/bootstrap.css \" />"
						+ "</head>"
						+ "<body>"
						+ "<div class=\"container text-center align-top\">"
						+ "<h3>Did you Mean: <i>"+new_keyword+"</i></h3></div><h3>Best Match: ");
				output_html.print("<a href=\""+sourcefiles[index]+"\">");
				output_html.print(name.substring(0, name.length()-4)+"</a></h3><br><hr>");
			}
			webpageranking(new_keyword,search_table,0);	
		}
		
		
	}
	private static void frequency(File[] files)throws IOException {
		int d=1;
		print_html = new PrintStream(new FileOutputStream(freq_file));
		print_html.println("<html>\n<title>FrequencyCalculator</title>"
				+ "<head>"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"style/bootstrap.css \" />"
				+ "</head><body><table class=\"table table-hover\"><tr  class=\"table-active\"><td>File Name</td>");
		while(d<4)
		{
			print_html.print("<td>");
			print_html.print(d);
			print_html.print("</td>");
			d++;
		}
		for(int j =0;j<files.length;j++)
		{
			//System.out.println("file"+(j+1));
			TST<Queue<Integer>> st = new TST<Queue<Integer>>();
			int f=0;
			try
			{
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
							if(!st.contains(str))
							{
								st.put(str,new  Queue<Integer>());
								st.get(str).enqueue(f);
							}    
							else
							{
								st.get(str).enqueue(f);
							}
		    				}
						}
		        	}	        	
		        	s=br.readLine();
		    	}
				br.close();
				
			}
			catch(NoSuchElementException ns) {}
			SplayTree<String,Integer> stree = new SplayTree<String,Integer>();
			for(String key:st.keys())
			{
				stree.insert(key,st.get(key).size());
			}
			String name = files[j].getName();
			stree.printrange(3,name.substring(0, name.length()-4),print_html);
			stree.makeEmpty();
			st.makeEmpty();
		}
		print_html.println("</table>");
		print_html.close();
		
	}
	private static void webpageranking(String keyword, Hashtable<String, Integer> search_table, int range) throws IOException {
		search2 = keyword.split("\\W+");
		output_html.println("<p>");
		int[][] ii= new int[files.length][search2.length];
		output_html.print("<p class=\"text-warning\">*displayed results are in the order based on frequency</p>");
		for(int j =0;j<files.length;j++)
		{
			
			TST<Queue<Integer>> st = new TST<Queue<Integer>>();
			int f=0;
			try
			{
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
		    				if(!st.contains(str))
							{
								st.put(str,new  Queue<Integer>());
								st.get(str).enqueue(f);
							}    
							else
							{
								st.get(str).enqueue(f);
							}
		    				}
						}
		        	}	        	
		        	s=br.readLine();
		    	}
				br.close();
			}
			catch(NoSuchElementException ns) {}
			for(int v=0;v<search2.length;v++)
			{
				if(st.get(search2[v])!=null)
				{
					ii[j][v]=st.get(search2[v]).size();
				}	
			}
			
		}
		int[] a = new int[files.length];
		for(int i= 0;i<files.length;i++)
		{
			for(int j=0;j<search2.length;j++)
			{
					a[i]+= ii[i][j];	
			}			
		}
		int max,index=0;
		
		do
		{
			max=0;
			for(int i=0;i<files.length;i++)
			{
				if(a[i]>=max)
				{
					max=a[i];
					index=i;
				}
			}
			
			a[index]=0;
			String name =files[index].getName();
			output_html.println("<a href=\""+sourcefiles[index]+"\">");
			output_html.println(name.substring(0, name.length()-4)+"</a><br><br><br>");
			range++;
			
		}while(max!=0 && range<10);
		output_html.print("</p>");
	}

}
