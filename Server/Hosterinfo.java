import java.net.Socket;

public class HosterInfo {
    public Socket socket;
    public String Name;
    public boolean Available;

    public HosterInfo(Socket s, String N, boolean A){
        socket = s;
        Available = A;
        Name = N;
    }
}
