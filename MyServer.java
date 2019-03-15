// A Java program for a Server 
import java.net.*;
import java.io.*;

public class MyServer
{
    //initialize socket and input stream
    //protected Socket SecondClient = null;
    private Socket		 socket = null;
    private ServerSocket server = null;

    // constructor with port
    public MyServer(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);


            // reads message from client until "Over" is sent
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
