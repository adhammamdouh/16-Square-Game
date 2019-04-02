import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class HomePage extends JFrame {
    private JPanel Panel1;
    private JTextField textField1;
    private JPanel Panel2;
    private JButton Host;
    private JButton Join;
    private JPanel Panel3;

    private Socket socket = null;
    private DataInputStream Receive = null;
    private DataOutputStream Send	 = null;

    GamePage TwoSquares = null;


    public HomePage(int port) throws IOException {


        setTitle("Two Squares Game");

        setResizable(false);

        setBounds(400,110,550,500);

        setMinimumSize(new Dimension(550,500));

        Host.setEnabled(false);
        Join.setEnabled(false);

        add(Panel1);
        Host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectToServer(port,true);

                TwoSquares = new GamePage(Receive,Send,textField1.getText());
                setVisible(false);
            }
        });
        Join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectToServer(port,false);

                TwoSquares = new GamePage(Receive,Send,textField1.getText());
                TwoSquares.UpdatePlayer(true);
                setVisible(false);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Closed");
            }
        });
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                textField1.requestFocus(false);
            }
        });
        
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textField1.getText().equals(null) || textField1.getText().equals("")){
                    Host.setEnabled(false);
                    Join.setEnabled(false);
                    return;
                }
                Host.setEnabled(true);
                Join.setEnabled(true);
            }
        });
    }

    public void ConnectToServer(int port,boolean Host){
        try
        {

            socket = new Socket("192.168.1.20", port);

            System.out.println("Connected");

            Send = new DataOutputStream(socket.getOutputStream());
            Receive = new DataInputStream(socket.getInputStream());

            if(Host) {
                Send.writeUTF("1");
                Send.writeUTF(textField1.getText());
            }
            else if(!Host) {
                Send.writeUTF("0");
                Send.writeUTF(textField1.getText());
            }
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    @Override
    protected void finalize() throws IOException {
        System.out.println("out");
        Send.writeUTF("-1");
        Send.close();
        Receive.close();
        socket.close();

    }
}
