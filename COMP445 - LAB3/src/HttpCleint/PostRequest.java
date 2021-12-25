package HttpCleint;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class PostRequest extends Request {
	public PostRequest(String [] args) throws MalformedURLException, UnknownHostException {	
		super(args);
	}

	
	public void sendRequest(String [] args) throws URISyntaxException {
		try 
		{
			System.out.println(host + port);
			Socket s = new Socket(host, port);
			String request = "";
			System.out.println("*******************************************************");
			System.out.println("The Client is connected to the Server");
			System.out.println("*******************************************************");
			InputStream in = s.getInputStream();
			OutputStream out = s.getOutputStream();
			if (isInLineText)
				request = "POST " + path + " " + version + extensions + "Content-Length:" + length + extensions+ headers + extensions+ inLineText;
			else
				request = "POST " + path + " " + version + extensions + "Content-Length:" + length + extensions+ headers + extensions+ readFile(filePath);	
			System.out.println(request);
			out.write(request.getBytes());
			out.flush();
			responseBuilder(in);
			in.close();
			out.close();
			s.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void responseBuilder(InputStream in) throws IOException {
		StringBuilder response= new StringBuilder();
		int responseChar = in.read();
		if (isVerbose) {
			while(responseChar != -1) 
			{	
				response.append((char) responseChar);
				responseChar = in.read();
			}
		}
		else
		{
			String responseHolder= "";
			while(responseChar!=-1) 
			{	
				responseHolder = responseHolder + (char) responseChar;
				if (responseHolder.contains("\r\n\r\n"))
					response.append((char) responseChar);
				responseChar = in.read();
			}
		}
		System.out.println(response);
	}
	
	public static String readFile(String filePath) throws IOException
	{	
		StringBuilder fileData= new StringBuilder();
		FileReader fr = new FileReader(filePath); 
		int i; 
	    while ((i=fr.read()) != -1) 
	    {
	    	fileData.append((char) i);
	    	length++; 	
	    }
	    fr.close();
	    return fileData.toString();
	}
	
}