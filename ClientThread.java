import java.net.Socket;
import java.io.*;

public class ClientThread extends Thread {
    protected Socket socket = null;
    protected DataInputStream Receive = null;
    protected DataOutputStream Send = null;

    public ClientThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        Receive = new DataInputStream(socket.getInputStream());
        Send = new DataOutputStream(socket.getOutputStream());
        System.out.println("One client Connected to server : " + socket.getPort());
    }

    public void run() {
        String Line = "";
        while (true) {
            try {
                Line = Receive.readUTF();
                if(Line.equals("Exit")) {
                    Send.writeUTF("Connection closed");
                    break;
                }
                Send.writeUTF("Server : " + "Message Received");

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("Query: " + Line);
        }
        try
        {
            Receive.close();
            Send.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }
}
