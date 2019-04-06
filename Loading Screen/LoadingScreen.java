import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class LoadingScreen extends JFrame {
    private DataInputStream Receive = null;
    private DataOutputStream Send = null;
    private String PlayerName = null;

    private GamePage TwoSquares = null;

    private JLabel LoadingGIF;
    private JButton cancelButton;
    private JPanel Panel;
    private JLabel LoadingLabel;

    public LoadingScreen(DataInputStream R , DataOutputStream S, String Name){
        Receive = R;
        Send = S;
        PlayerName =  Name;
        setTitle("Loading ...");

        ImageIcon ii = new ImageIcon(this.getClass().getResource(
                "BackGroundGIF.gif"));
        LoadingGIF.setIcon(ii);

        Thread LoadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String Player2Name = null;
                try {
                    while (true) {
                        String ReceivedMessage;
                        ReceivedMessage = Receive.readUTF();
                        if(ReceivedMessage.equals("start"))break;
                        Send.writeUTF("Ack");
                    }
                    Player2Name = Receive.readUTF();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,"Connection error!! the game will close");
                    System.exit(0);
                }
                TwoSquares = new GamePage(Receive,Send,PlayerName,Player2Name);
                setVisible(false);
            }
        });
        LoadingThread.setDaemon(true);
        LoadingThread.start();

        setBounds(600,200,180,280);
        add(Panel);
        //setUndecorated(true);
        setVisible(true);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });
    }
}
