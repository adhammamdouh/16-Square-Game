import java.net.Socket;

public class ClientInfo {
    public Socket socket;
    public String Name;
    public boolean Available;

    public ClientInfo(Socket s,String N,boolean A){
        socket = s;
        Available = A;
        Name = N;
    }
}
