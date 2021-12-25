package HttpCleint;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

public class Request {
	protected static boolean isVerbose=false;
	protected static boolean isHeader=false;
	protected static boolean isInLineText=false;
	protected static boolean isFile=false;
	protected static String headers="";
	protected static String inLineText="";
	protected static String filePath="";
	protected static String stringURL;
	protected String path="";
	protected String host="";
	protected String queries="";
	protected static int length=0;
	protected final String version = "HTTP/1.0";
	protected final static String extensions = "\r\n";
	protected int port =8080;
	
	
	//Initialize and decipher the request
	// what do i need to validate request? args.. such as isVerbose, isHeaders, headers, url, path, host.
	
	public Request(String [] args) throws MalformedURLException, UnknownHostException 
	{
		requestType(args);
		if (validateArgs())
		{
			System.out.println(args[args.length-1]);
			
		}
		else
		{
			System.out.println("this is the host: " + host + "-- and this is the stringURL: " + stringURL);
			System.out.println("invalid args..");
		}
	}
	
	 public static void requestType(String[] args)
	 {
	        for (int i =0; i<args.length; i++)
	        {
	            if (args[i].equalsIgnoreCase("-v"))
	            {
	                isVerbose = true;
	            }
	            else if (args[i].equalsIgnoreCase("-h"))
	            {
	                isHeader = true;
	                headers = headers.concat(args[i+1]+ extensions);
	                i++;
	            }
	            else if (args[i].equalsIgnoreCase("-d"))
	            {
	                isInLineText = true;
	                inLineText = (args[i+1]);
	                length=inLineText.length();
	                i++;
	            }
	            else if (args[i].equalsIgnoreCase("-f"))
	            {
	                isFile = true;
	                filePath = (args[i+1]);
	            }
	            else
	                stringURL= (args[i]);
	       }
	 }
	 
	 public boolean validateArgs() 
	 {
		 if ((isFile==true && isInLineText==true))// || (isFile==false && isInLineText==false)
			 return false;
		 if (stringURL.contains("http://localhost/"))
		 {
			 host = "localhost";
			 port = 8080;
			 System.out.println(port);
			 System.out.println(host);
			 return true;
		 }
		 else
		 {
			 try 
			 { 
				new URL(stringURL).toURI();
				URL url = new URL(stringURL);
				host = url.getHost();
				path = url.getPath();
				queries = url.getQuery();
				//port = url.getPort();
		        return true; 
		     } 
		          
		        // If there was an Exception 
		        // while creating URL object 
		        catch (Exception e) { 
		            return false; 
		        }
		 }
	 }

}
