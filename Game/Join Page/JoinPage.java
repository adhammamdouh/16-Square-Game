import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class JoinPage extends JFrame {
    private DataInputStream Receive = null;
    private DataOutputStream Send = null;
    private String Player1Name = null;

    private GamePage TwoSquares = null;

    private JPanel panel;
    private JList List;
    private JButton joinButton;
    private JTextField textField1;
    private JScrollPane ListView;
    private JButton refreshButton;
    private Vector<String> HostersList;
    private String Hoster = null;
    public JoinPage(DataInputStream R , DataOutputStream S, String Name) {
        Receive = R;
        Send = S;
        Player1Name = Name;
        HostersList = new Vector<>();

        this.setBounds(500, 200, 410, 400);

        Thread ReadingHostersThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ReadHosters();
            }
        });
        ReadingHostersThread.start();

        joinButton.setEnabled(false);
        add(panel);
        this.setVisible(true);
        List.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Hoster = List.getSelectedValue().toString();
                joinButton.setEnabled(true);
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Send.writeUTF(Hoster);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Connection error!! we will close the game");
                    System.exit(0);
                }
                String Available = null;
                try {
                    Available = Receive.readUTF();
                    System.out.println(Available);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Connection error the game will close");
                    System.exit(0);
                }
                if(Available.equals("start")){
                    try {
                        String Player2Name = Receive.readUTF();
                        TwoSquares = new GamePage(Receive,Send,Player1Name,Player2Name);
                        TwoSquares.UpdatePlayer(true);
                        setVisible(false);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else {
                    try {
                        Available = Receive.readUTF();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println(Available);
                    JOptionPane.showMessageDialog(null,"error: can't connect to the host");
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Send.writeUTF("refresh");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Connection error!! we will close the game");
                    System.exit(0);
                }
                ReadHosters();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });
    }

    public void ReadHosters(){
        try {
            List.clearSelection();
        }
        catch (Exception E){
        }
        List.removeAll();
        String HosterName = null;
        HostersList.clear();
        while (true){
            try {
                HosterName = Receive.readUTF();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"Connection error!! we will close the game");
                System.exit(0);
            }
            System.out.println(HosterName);
            if(HosterName.equals("end"))break;
            HostersList.add(HosterName);
        }
        List.setListData(HostersList);
    }
}
