// A Java program for a Server 
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyServer
{
    private Socket MainSocket = null;
    private ServerSocket server = null;
    private DataInputStream _Receive = null;
    private String Name = null;

    public static volatile ArrayList<ClientInfo> Hoster = null;
    // constructor with port
    public MyServer(int port)
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
                    Name = _Receive.readUTF();

                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }

                if(Which.equals("1")) {
                    Hoster.add(new ClientInfo(MainSocket,Name,true));

                }

                else if(Which.equals("0")) {
                    // new thread for a client
                    Thread clientThread = new Thread(new Runnable() {

                        Socket Player1 = null;
                        Socket Player2 = null;
                        DataInputStream ReceivePlayer1 = null;
                        DataOutputStream SendPlayer1 = null;

                        DataInputStream ReceivePlayer2 = null;
                        DataOutputStream SendPlayer2 = null;

                        @Override
                        public void run() {
                            int i = 0;
                            for(i = 0 ; i < Hoster.size() ; ++i){
                                if(Hoster.get(i).Available){
                                    Player1 = Hoster.get(i).socket;
                                    break;
                                }
                            }

                            try {
                                Player2 = MainSocket;
                                ReceivePlayer2 = new DataInputStream(Player2.getInputStream());
                                SendPlayer2 = new DataOutputStream(Player2.getOutputStream());
                                SendPlayer1 = new DataOutputStream(Player1.getOutputStream());
                                ReceivePlayer1 = new DataInputStream(Player1.getInputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {

                                SendPlayer1.writeUTF(Name);
                                SendPlayer2.writeUTF(Hoster.get(i).Name);
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
                            stop(i);
                        }

                        public String SendReceive(String num, DataInputStream Player1Read,DataOutputStream Player1Send,DataInputStream Player2Read,DataOutputStream Player2Send){
                            while (!num.equals("-1") && !num.equals("17")){
                                try {
                                    num = Player1Read.readUTF();
                                }catch (Exception e){
                                    try {
                                        Player2Send.writeUTF("error");
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
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
                                        e1.printStackTrace();
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

                        public synchronized void stop(int i){
                            Hoster.remove(i);
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

    public static void main(String args[])
    {
        MyServer server = new MyServer(5001);
    }
} 
