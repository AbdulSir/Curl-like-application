package HttpCleint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;

public class GetRequest extends Request {
	
	public GetRequest(String [] args) throws IOException, URISyntaxException 
	{
		super(args);
	}
	public void sendRequest(String [] args) {
		try 
		{
			System.out.println(host + " " + port);
			Socket s = new Socket(host, port);
			String request = "";
			System.out.println("*******************************************************");
			System.out.println("The Client is connected to the Server");
			System.out.println("*******************************************************");
			//InputStream in = s.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			OutputStream out = s.getOutputStream();
			if (!(queries==null))
				request = "GET " + path + "?" + queries + " " + version + extensions + headers + extensions;
			else 
				request = "GET " + path + " " + version + extensions + headers + extensions;
			System.out.println("Client request:\n" + request);
			System.out.println("********************************");
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
	
	public void responseBuilder(BufferedReader in) throws IOException {
		try {
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
			System.out.println("Server response:\n" + response);
			System.out.println("********************************");
		}
		catch(SocketException se) {
		}
	}
}