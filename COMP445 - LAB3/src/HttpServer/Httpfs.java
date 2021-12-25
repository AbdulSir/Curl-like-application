package HttpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Httpfs {
	protected static boolean isVerbose=false;
	protected static boolean dirGiven=false;
	protected static String directory="C:\\Users\\Admin\\eclipse-workspace\\COMP445 - LAB2\\Resources";
	protected static int port = 8080;
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
        Socket socket = null;
        
		for (int i =0; i<args.length; i++)
		{
			if (args[i].equals("help"))
					System.out.println("This is help for the server server");
			else if (args[i].equalsIgnoreCase("-v"))
	        {
				isVerbose = true;
			}
	        else if (args[i].equalsIgnoreCase("-p"))
	        {
	        	String str= args[i+1];
	        	int temp = Integer.parseInt(str);
	            if (temp >1023 && temp <655351)
	           	   port = Integer.parseInt(str);
	        }
	        else if (args[i].equalsIgnoreCase("-d"))
	        {
	        	dirGiven = true;
	        	String givenPath = (args[i+1]);
	            directory = createRootPath(givenPath);
	        }
        }
		
        try 
        {
            serverSocket = new ServerSocket(port);
            System.out.println("Httpfs: Listening on port : " + port + " and awaiting for clients.");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        while (true) {
            try {
                socket = serverSocket.accept();
                System.out.println("********************************");
    			System.out.println("Httpfs: Client connected.");
    			System.out.println("********************************");
            } 
            catch (IOException e) 
            {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new Server(socket, directory).start();
        }
	}
	private static String createRootPath(String givenPath) throws IOException 
	{
		givenPath=givenPath.replaceAll("\\/", "\\\\");
		directory = directory + givenPath;
		Path path = Paths.get(directory);
		Files.createDirectories(path);
		return directory;
	}
			
}
