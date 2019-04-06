import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class HomePage extends JFrame {
    private JPanel Panel;
    private JTextField NameField;
    private JTextField PortField;
    private JTextField IPField;
    private JButton Host;
    private JButton Join;
    private JLabel WelcomeLabel;
    private JLabel ToLabel;
    private JLabel SquareLabel;
    private JLabel GameLabel;

    private Font font = new Font("",Font.CENTER_BASELINE,50);

    private Socket socket = null;
    private DataInputStream Receive = null;
    private DataOutputStream Send	 = null;

    GamePage TwoSquares = null;


    public HomePage(int port){


        setTitle("Two Squares Game");

        setResizable(false);

        setBounds(400,110,550,500);

        Host.setEnabled(false);
        Join.setEnabled(false);

        WelcomeLabel.setFont(font);
        ToLabel.setFont(font);
        SquareLabel.setFont(font);
        GameLabel.setFont(font);

        add(Panel);
        Host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean connect = ConnectToServer(port,true);
                if(!connect)return;

                LoadingScreen LS = new LoadingScreen(Receive,Send,NameField.getText());

                setVisible(false);
            }
        });
        Join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean connect = ConnectToServer(port,false);
                if(!connect)return;

                JoinPage JP = new JoinPage(Receive,Send,NameField.getText());

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
        NameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                NameField.requestFocus(false);
            }
        });

        NameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(NameField.getText() == null || NameField.getText().equals("")){
                    Host.setEnabled(false);
                    Join.setEnabled(false);
                    return;
                }
                Host.setEnabled(true);
                Join.setEnabled(true);
            }
        });
    }

    public boolean ConnectToServer(int port,boolean Host){
        try
        {
            try {
                socket = new Socket(IPField.getText(), Integer.parseInt(PortField.getText()));
            }
            catch (Exception E){
                JOptionPane.showMessageDialog(null,"The Server isn't available try again later...");
                return false;
            }
            System.out.println("Connected");

            Send = new DataOutputStream(socket.getOutputStream());
            Receive = new DataInputStream(socket.getInputStream());

            if(Host) {
                Send.writeUTF("1");
            }
            else if(!Host) {
                Send.writeUTF("0");
            }
            int Check = checkName();
            if(Check == 0) {
                JOptionPane.showMessageDialog(null,"Connection error the game will close");
                return false;
            }
            else if(Check == -1){
                JOptionPane.showMessageDialog(null,"The Name Exists enter another name...");
                return false;
            }
            return true;

        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        return true;
    }

    public int checkName(){
        String ReceiveMessage = null;
        try {
            Send.writeUTF(NameField.getText());
            ReceiveMessage = Receive.readUTF();
        } catch (IOException e) {
            return 0;
        }
        if(ReceiveMessage.equals("1"))
            return 1;
        else {
            return -1;
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
