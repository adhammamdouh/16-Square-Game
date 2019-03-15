import java.net.Socket;
import java.io.*;

public class ClientThread extends Thread {
    protected Socket socket;
    protected DataInputStream Read;
    protected DataOutputStream Write;

    public ClientThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        Read = new DataInputStream(socket.getInputStream());
        Write = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client accepted");
    }

    public void run() {
        String Line = "";
        while (true) {
            try {
                Line = Read.readUTF();
                if(Line.equals("out")){
                    socket.close();
                    break;
                }
                Write.writeUTF("Sent");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("Query: " + Line);
        }

    }
}
