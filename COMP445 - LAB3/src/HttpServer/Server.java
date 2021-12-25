package HttpServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Server extends Thread {
	private String directory="";
	private static String request = "";
	private static String requestHeaders = "";
	private static String response="";
	private final String version = "HTTP/1.0";
	private final static String extensions = "\r\n";
	private static final Exception ForbiddenPath = null;
	private Socket client;
	
	
	//socket will be an instance variable initialized in the constructor.
	public Server(Socket socket, String directory) throws IOException {
		this.client=socket;
		this.directory=directory;
		request="";
	}
	
	public void run() {
		try {
			request = readRequest(request);
			System.out.println("Client request:\n" + request);
			System.out.println("********************************");
			String [] str = request.split("\r\n");
			String [] str1 = str[0].split(" ");
			if (str1[0].equals("GET")) {
				handleGetRequests(str1[1]);
				
			}
			else
				handlePostRequests(str1[1]);
			client.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String readRequest(String request) throws IOException {
		BufferedReader in=null;
		try 
		{
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			int c = in.read();
			client.setSoTimeout(1000);
			while (c!=-1) {
					request += (char) c;
					c = in.read();
			}
		}
		
		catch (Exception e) {
		}
		requestHeaders = request.substring(request.indexOf(extensions) +2, request.lastIndexOf(extensions));
		//in.close();
		return request;
	}
	
	private void handleGetRequests(String path) {
		String[] pathnames;
		String body="";
		try {
			if (path.contains(".."))
			{
			    response = version + " 403 : Forbidden" + extensions + requestHeaders + extensions;
		    }
			else if (path.equals("/")) 
			{
				File f = new File(directory);
		        pathnames = f.list();
		        for (String pathname : pathnames) 
		        {
		        	body += pathname + "\n";
		        }
		        response = version + " 200 OK" + extensions + requestHeaders + extensions + body;
			}
			else 
			{	
				directory += path;
				Paths.get(directory);
				File f = new File(directory);
				if (f.isDirectory()) {
					pathnames = f.list();
					for (String pathname : pathnames) {
						body += pathname + "\n";
				    }
				}
				else 
				{ 
					Scanner sc = new Scanner(f); 
				    while (sc.hasNextLine())
				    {
				    	body= body + sc.nextLine() + "\n";
				    }
				    requestHeaders = "Content-Type: text/html" + extensions + "Content-Length: " + body.length() + extensions + requestHeaders;
				}
				response = version + " 200 OK" + extensions + requestHeaders + extensions + body;
			}
			OutputStream out = client.getOutputStream();
			out.write(response.getBytes());
			out.flush();
		}
		catch (FileNotFoundException e) 
		{
			try 
	    	{
				response = version + " 404 Not Found" + extensions + requestHeaders + extensions;
				OutputStream out = client.getOutputStream();
				out.write(response.getBytes());
				out.flush();
	    	}
			catch (IOException ioe) {
			}
		}
		catch (InvalidPathException | NullPointerException ex) 
		{
	    	try 
	    	{
		    	response = version + " 400 : Bad Request" + extensions + requestHeaders + extensions;
				OutputStream out = client.getOutputStream();
				out.write(response.getBytes());
				out.flush();
	    	}
			catch (IOException e) {
			}
	    }
		catch (IOException ioe) {
		}
		
	}


	private void handlePostRequests(String givenPath) {
		try {
			String requestBody=request.substring(request.indexOf(extensions + extensions) + 4);
			String temp="";
			temp = givenPath.replaceAll("\\/", "\\\\");
			directory+=temp;
			temp = directory.substring(0, directory.lastIndexOf("\\"));
			Path path = Paths.get(temp);
			Files.createDirectories(path);
			File file = new File (directory);
			file.createNewFile();
			FileWriter myWriter = new FileWriter(directory);
		    myWriter.write(requestBody);
		    myWriter.close();
		    response = version + " 201 : Created" + extensions + requestHeaders + extensions;
			OutputStream out = client.getOutputStream();
			out.write(response.getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (InvalidPathException | NullPointerException ex) 
		{
	    	try 
	    	{
		    	response = version + " 400 : Bad Request" + extensions + requestHeaders + extensions;
				OutputStream out = client.getOutputStream();
				out.write(response.getBytes());
				out.flush();
	    	}
			catch (Exception e) {
				System.out.println(directory);
			}
	    }
	}
}
	