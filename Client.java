// A Java program for a Client
import java.net.*;
import java.io.*;

public class CLient
{
    // initialize socket and input output streams
    private Socket socket		 = null;
    private DataInputStream input = null;
    private DataInputStream Receive = null;
    private DataOutputStream Send	 = null;

    // constructor to put ip address and port
    public CLient(int port)
    {
        // establish a connection
        try
        {
            socket = new Socket("localhost", port);
            System.out.println("Connected");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            Send = new DataOutputStream(socket.getOutputStream());
            Receive = new DataInputStream(socket.getInputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (true)
        {
            try
            {
                line = input.readLine();
                Send.writeUTF(line);
                System.out.println(Receive.readUTF());
                if(line.equals("Exit"))break;

            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }

        // close the connection
        try
        {
            input.close();
            Send.close();
            Receive.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        CLient client = new CLient(5000);
    }
}
