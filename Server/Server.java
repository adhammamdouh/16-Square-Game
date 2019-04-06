// A Java program for a Server 

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{
    private Socket MainSocket = null;
    private ServerSocket server = null;
    private DataInputStream _Receive = null;

    public static volatile ArrayList<HosterInfo> Hoster = null;
    // constructor with port
    public Server(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            Hoster = new ArrayList<>();

            System.out.println("Server started");

            String Which = null;
            while (true)
            {
                try {

                    System.out.println("Waiting for a client ...");

                    MainSocket = server.accept();
                    System.out.println("One client Connected to server : " + MainSocket.getPort());

                    _Receive = new DataInputStream(MainSocket.getInputStream());

                    Which = _Receive.readUTF();

                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }

                if(Which.equals("1")) {
                    Thread HostThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Socket player = MainSocket;
                            DataInputStream Receive = null;
                            DataOutputStream Send = null;
                            try {
                                Receive = new DataInputStream(player.getInputStream());
                                Send = new DataOutputStream(player.getOutputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String Name = CheckName(Receive,Send);
                            if(Name.equals("0"))return;
                            else if(Name.equals("-1"))return;
                            Hoster.add(new HosterInfo(MainSocket,Name,true));
                        }
                    });
                    HostThread.setDaemon(true);
                    HostThread.start();
                }

                else if(Which.equals("0")) {
                    // new thread every two client
                    Thread clientThread = new Thread(new Runnable() {

                        Socket Player1 = null;
                        Socket Player2 = null;
                        DataInputStream ReceivePlayer1 = null;
                        DataOutputStream SendPlayer1 = null;

                        DataInputStream ReceivePlayer2 = null;
                        DataOutputStream SendPlayer2 = null;
                        int HostIndex = 0;

                        @Override
                        public void run() {
                            String Name = null;
                            try {
                                Player2 = MainSocket;
                                SendPlayer2 = new DataOutputStream(Player2.getOutputStream());
                                ReceivePlayer2 = new DataInputStream(Player2.getInputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Name = CheckName(ReceivePlayer2,SendPlayer2);
                            if(Name.equals("0"))return;
                            if(Name.equals("-1"))return;

                            if(!SendHostersElements())return;

                            try {
                                SendPlayer1.writeUTF(Name);
                                SendPlayer2.writeUTF(Hoster.get(HostIndex).Name);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            while (true) {
                                String num = "";
                                num = SendReceive(num,ReceivePlayer1,SendPlayer1,ReceivePlayer2,SendPlayer2);
                                if(num.equals("17"))break;

                                num = "0";
                                num = SendReceive(num,ReceivePlayer2,SendPlayer2,ReceivePlayer1,SendPlayer1);
                                if(num.equals("17"))break;
                            }
                            stop();
                        }

                        public String SendReceive(String num, DataInputStream Player1Read,DataOutputStream Player1Send,DataInputStream Player2Read,DataOutputStream Player2Send){
                            while (!num.equals("-1") && !num.equals("17")){
                                try {
                                    num = Player1Read.readUTF();
                                }catch (Exception e){
                                    try {
                                        Player2Send.writeUTF("error");
                                    } catch (IOException e1) {
                                        num = "17";
                                        break;
                                    }
                                    num = "17";
                                    break;
                                }

                                try {
                                    Player2Send.writeUTF(num + "");
                                }catch (Exception e){
                                    num = "17";
                                    break;
                                }

                                try {
                                    String Ack = Player2Read.readUTF();
                                } catch (Exception e){
                                    try {
                                        Player1Send.writeUTF("error");
                                    } catch (IOException e1) {
                                        num = "17";
                                        break;
                                    }
                                    num = "17";
                                    break;
                                }

                                try {
                                    Player1Send.writeUTF("sent");
                                } catch (Exception e){
                                    num = "17";
                                    break;
                                }
                            }
                            return num;
                        }

                        public boolean SendHostersElements(){
                            while (true) {
                                for (int i = 0; i < Hoster.size(); ++i) {
                                    try {
                                        if(Hoster.get(i).Available)
                                            SendPlayer2.writeUTF(Hoster.get(i).Name);
                                    } catch (IOException e) {
                                        return false;
                                    }
                                }
                                try {
                                    SendPlayer2.writeUTF("end");
                                } catch (IOException e) {
                                    return false;
                                }

                                String HostName = null;

                                try {
                                    HostName = ReceivePlayer2.readUTF();
                                } catch (IOException e) {
                                    return false;
                                }

                                if (HostName.equals("refresh"))continue;
                                while (true) {
                                    if (CheckConnection(HostName)) {
                                        try {
                                            SendPlayer1.writeUTF("start");
                                        } catch (IOException e) {
                                            for(int i = 0 ; i < Hoster.size() ; ++i){
                                                if(Hoster.get(i).Name.equals(HostName)){
                                                    Hoster.remove(i);
                                                    break;
                                                }
                                            }
                                            return false;
                                        }

                                        try {
                                            SendPlayer2.writeUTF("start");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        return true;
                                    } else {
                                        try {
                                            SendPlayer2.writeUTF("Not Available");
                                            HostName = ReceivePlayer2.readUTF();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(HostName.equals("refresh"))break;
                                }
                            }
                        }

                        public boolean CheckConnection(String Name){
                            for(int i = 0 ; i < Hoster.size() ; ++i){
                                if(Hoster.get(i).Name.equals(Name) && Hoster.get(i).Available){
                                    Player1 = Hoster.get(i).socket;
                                    try {
                                        ReceivePlayer1 = new DataInputStream(Player1.getInputStream());
                                        SendPlayer1 = new DataOutputStream(Player1.getOutputStream());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        SendPlayer1.writeUTF("Connected");
                                        ReceivePlayer1.readUTF();
                                    } catch (IOException e) {
                                        Hoster.remove(i);
                                        --i;
                                        return false;
                                    }
                                    Hoster.get(i).Available = false;
                                    HostIndex = i;
                                    return true;
                                }
                            }
                            return false;
                        }

                        public synchronized void stop(){
                            for(int j = 0 ; j < Hoster.size() ; ++j){
                                if(Hoster.get(j).socket == Player2){
                                    Hoster.remove(j);
                                    break;
                                }
                            }
                            try {
                                ReceivePlayer1.close();
                                SendPlayer1.close();
                                ReceivePlayer2.close();
                                SendPlayer2.close();
                                if(!Player1.isClosed())
                                    Player1.close();
                                if(!Player2.isClosed())
                                    Player2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    clientThread.setDaemon(true);
                    clientThread.start();
                }
            }

        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public String CheckName(DataInputStream R , DataOutputStream S){
        String name = null;
        try {
            name = R.readUTF();
        } catch (IOException e) {
            return "0";
        }
        boolean approved = true;

        for(int i = 0 ; i < Hoster.size() ; ++i){
            Socket socket = Hoster.get(i).socket;
            if(Hoster.get(i).Name.equals(name)){
                approved = false;
                break;
            }
            if(!Hoster.get(i).Available)continue;

            DataInputStream Receive = null;
            DataOutputStream Send = null;
            try {
                 Send = new DataOutputStream(socket.getOutputStream());
                Receive = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Send.writeUTF("connected");
                Receive.readUTF();
            } catch (IOException e) {
                Hoster.remove(i);
                i--;
                continue;
            }

        }
        if(approved){
            try {
                S.writeUTF("1");
            } catch (IOException e) {
                return "0";
            }
            return name;
        }
        try {
            S.writeUTF("-1");
        } catch (IOException e) {
            return "0";
        }
        return "-1";
    }

    public static void main(String args[])
    {
        Server server = new Server(5001);
    }
} 
