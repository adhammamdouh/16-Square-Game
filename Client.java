import java.net.*;
import java.io.*;

public class CLient
{
    // initialize socket and input output streams
    private Socket socket		 = null;
    private DataInputStream input = null;
    private DataInputStream FromServer = null;
    private DataOutputStream out	 = null;

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
            out = new DataOutputStream(socket.getOutputStream());
            FromServer = new DataInputStream(socket.getInputStream());
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
                if(line.equals("Over"))break;

                out.writeUTF(line);
                line = FromServer.readUTF();
                System.out.println(line);
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
            out.close();
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
