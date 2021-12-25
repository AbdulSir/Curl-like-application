package HttpCleint;

import java.io.IOException;
import java.net.URISyntaxException;

public class Httpc {

	public static void main(String[] args) throws IOException, URISyntaxException {
		try
		{
			if (args[0].equals("help") && args.length==1) 
			{
				System.out.println("httpc is a curl-like application but supports HTTP protocol only.");
				System.out.println("Usage:\r\n" + 
						"\thttpc command [arguments] \nThe commands are:\r\n" + 
						"\tget \t executes a HTTP GET request and prints the response. \n\tpost \t executes a HTTP POST request and prints the response."
						+ "\n\thelp \t prints this screen.\r\n" + 
						"Use \"httpc help [command]\" for more information about a command.");
			}
			
			else if (args[0].equals("help") && args[1].equals("get") && args.length==2) 
			{
				System.out.println("usage: httpc get [-v] [-h key:value] URL");
				System.out.println("Get executes a HTTP GET request for a given URL.");
				System.out.println("-v \tPrints the detail of the response such as protocol, status, and headers.");
				System.out.println("-h key:value \tAssociates headers to HTTP Request with the format 'key:value'.");
			}
			
			else if (args[0].equals("help") && args[1].equals("post") && args.length==2) 
			{
				System.out.println("usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL");
				System.out.println("Post executes a HTTP POST request for a given URL with inline data or from file.");
				System.out.println("-v \tPrints the detail of the response such as protocol, status, and headers.");
				System.out.println("-h key:value \tAssociates headers to HTTP Request with the format 'key:value'.");
				System.out.println("-d string \tAssociates an inline data to the body HTTP POST request.");
				System.out.println("-f \tfileAssociates the content of a file to the body HTTP POST request.");
				System.out.println("Either [-d] or [-f] can be used but not both.");
			}
			
			else if (args[0].equals("get"))
			{
				GetRequest getRequest = new GetRequest(args);
				getRequest.sendRequest(args);
			}
			
			else if (args[0].equals("post"))
			{
				PostRequest postRequest = new PostRequest(args);
				postRequest.sendRequest(args);
			}
			
			else
			{
				System.out.println("Invalid command.. ");
				System.exit(0);
			}
		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			e.getMessage();
		}
		
		//c1.get("www.httpbin.org", 80, "/status/418");
			
	}


}
