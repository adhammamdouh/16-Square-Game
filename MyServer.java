import java.net.*;
import java.io.*;

public class MyServer
{
    //initialize socket and input stream
    private Socket		 socket = null;
    private ServerSocket server = null;
    private DataInputStream in	 = null;

    public MyServer(int port)
    {
        try
        {
            server = new ServerSocket(port);
            
            while (true)
            {
                try {
                    System.out.println("Server started");

                    System.out.println("Waiting for a client ...");
                    socket = server.accept();

                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
                // new thread for a client
                new ClientThread(socket).start();
            }
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        MyServer server = new MyServer(5000);
    }
} 
