import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GamePage extends JFrame{
    private JButton button9;
    private JButton button13;
    private JButton button5;
    private JButton button1;
    private JButton button2;
    private JButton button6;
    private JButton button10;
    private JButton button14;
    private JButton button3;
    private JButton button7;
    private JButton button11;
    private JButton button15;
    private JButton button4;
    private JButton button8;
    private JButton button12;
    private JButton button16;
    private JPanel Panel;
    private JLabel Player;
    private JLabel NameLabel;

    private Color MainColor = Color.white;
    private Color NotChoice = Color.pink;
    private Font font = new Font("",Font.BOLD,50);
    private Color Player1Color = Color.BLUE;
    private Color Player2Color = Color.red;

    private DataInputStream Receive = null;
    private DataOutputStream Send = null;
    private int RECEIVED = 0;

    private int FirstChoice = 0;

    private String Player1Name;
    private String Player2Name;

    public static JButton[][] btn = null;

    public GamePage(DataInputStream R , DataOutputStream S, String Player1,String Player2){
        Receive = R;
        Send = S;
        Player1Name = Player1;
        Player2Name = Player2;

        AddButtons();

        NameLabel.setText("Your Name : " + Player1);

        for(int i = 0 ; i < 4; ++i) {
            for(int j = 0 ; j < 4 ; ++j) {
                btn[i][j].setBackground(MainColor);
                btn[i][j].setText(i*4 + j + 1 + "");
                btn[i][j].setForeground(Color.black);
                btn[i][j].setFont(font);
                btn[i][j].addActionListener( this::actionPerformed);
            }
        }

        setBounds(350,110,500,500);
        setResizable(false);

        add(Panel);

        Player.setText("Player 1 : " + Player1Name);
        setVisible(true);

        Player.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(Player.getText().equals("Player 1 : " + Player1Name))return;
                ReceiveSquares();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        for(int i = 0 ; i < 4 ; ++i){
            for(int j = 0 ; j < 4 ; ++j){
                if(btn[i][j] == e.getSource()){
                    try {
                        int SENDED = (i * 4) + j + 1;
                        Send.writeUTF(SENDED + "");
                        try {
                            String temp = Receive.readUTF();
                            if(temp.equals("error")){
                                JOptionPane.showMessageDialog(null,"Connection error the game will close ^_^");
                                stop();
                                System.exit(0);
                            }
                        } catch (Exception k){
                            JOptionPane.showMessageDialog(null,"Connection error the game will close ^_^");
                            stop();
                            System.exit(0);
                        }
                    }
                    catch (IOException l){
                        JOptionPane.showMessageDialog(null,"Connection error the game will close ^_^");
                        stop();
                        System.exit(0);
                    }

                    if(btn[i][j].getBackground() != MainColor){
                        btn[i][j].setBackground(MainColor);
                        FirstChoice = 0;
                        DEnableAll(true);
                    }
                    else if(FirstChoice == 0){
                        FirstChoice = i*4 + j + 1;
                        btn[i][j].setBackground(Player1Color);
                        SetEnable(i,j);
                    }
                    else{
                        int SecondChoice = i * 4 + j + 1;
                        btn[i][j].setBackground(Player1Color);
                        DisableButton(FirstChoice);
                        DisableButton(SecondChoice);

                        if(CheckValidAndEndGame()){
                            try {
                                Send.writeUTF("17");
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null,"Connection error the game will close ^_^");
                                stop();
                                System.exit(0);
                            }
                            JOptionPane.showMessageDialog(null,"The Winner is : " + Player1Name);
                            stop();
                            System.exit(0);
                        }
                        try {
                            Send.writeUTF("-1");
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null,"Connection error the game will close ^_^");
                            stop();
                            System.exit(0);
                        }
                        FirstChoice = 0;
                        UpdatePlayer(true);
                    }
                }
            }
        }
    }
    public void DEnableAll(boolean Bool){
        for(int i = 0 ; i < 4 ; ++i){
            for(int j = 0 ; j < 4 ; ++j){
                if(btn[i][j].getBackground() == MainColor){
                    btn[i][j].setEnabled(Bool);
                }
            }
        }
    }

    public boolean CheckValidAndEndGame(){
        boolean GameEnded = true;
        for(int i = 0 ; i < 4 ; ++i){
            for(int j = 0 ; j < 4 ; ++j){
                if(btn[i][j].getBackground() != MainColor)continue;
                boolean NotValid = true;

                for(int k = j-1 ; k <= j+1 ; ++k){
                    if(k < 0 || k > 3) continue;
                    if(btn[i][k].getBackground() == MainColor && k != j) {
                        NotValid = false;
                    }
                }

                for(int k = i-1 ; k <= i+1 ; ++k){
                    if(k < 0 || k > 3) continue;
                    if(btn[k][j].getBackground() == MainColor && k != i) {
                        NotValid = false;
                    }
                }

                if(NotValid) {
                    btn[i][j].setBackground(NotChoice);
                    btn[i][j].setEnabled(false);
                }
                if(btn[i][j].getBackground() == MainColor) GameEnded = false;
            }
        }
        return GameEnded;
    }

    public void ReceiveSquares(){
        Thread ReciveThread;
        ReciveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DEnableAll(false);
                String temp = "";
                while (true){
                    try {
                        temp = Receive.readUTF();
                        if(temp.equals("error")){
                            JOptionPane.showMessageDialog(null,"Connection error the game will close ^_^");
                            stop();
                            System.exit(0);
                        }
                    } catch (Exception e){
                        JOptionPane.showMessageDialog(null,"Connection error the game will close ^_^");
                        stop();
                        System.exit(0);
                        break;
                    }
                    if(temp.equals("sent"))continue;
                    RECEIVED = Integer.parseInt(temp);
                    try {
                        Send.writeUTF("arrived");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(RECEIVED == -1)break;
                    else if(RECEIVED == 17){
                        CheckValidAndEndGame();
                        JOptionPane.showMessageDialog(null,"the Winner is : " + Player2Name);
                        System.exit(0);
                    }
                    int i = (RECEIVED - 1)/4;
                    int j = (RECEIVED - 1)%4;
                    if(btn[i][j].getBackground() != MainColor)
                        btn[i][j].setBackground(MainColor);
                    else
                        btn[i][j].setBackground(Player2Color);
                }
                RECEIVED = 0;
                CheckValidAndEndGame();
                DEnableAll(true);
                UpdatePlayer(false);
            }

        });
        ReciveThread.setDaemon(true);
        ReciveThread.start();
    }

    public synchronized void stop(){
        try {
            Receive.close();
            Send.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpdatePlayer(boolean Player1){
        Player.setText((Player1)?"Player 2 : " + Player2Name : "Player 1 : " + Player1Name);
    }

    public void SetEnable(int row, int col){
        for(int i = 0 ; i < 4 ; ++i){
            for(int j = 0 ; j < 4 ; ++j){
                if((((i == row && j == col-1)
                        || (i == row && j == col +1)
                        || (i == row-1 && j == col)
                        || (i == row+1 && j == col))
                        && btn[i][j].getBackground() == MainColor)
                        || (i == row && j == col)){

                    btn[i][j].setEnabled(true);
                }
                else btn[i][j].setEnabled(false);
            }
        }
    }

    public void DisableButton(int num){
        int i = (num -1)/4;
        int j = (num - 1)%4;
        btn[i][j].setEnabled(false);
    }

    public void AddButtons(){
        btn = new JButton[4][4];

        btn[0][0] = button1;
        btn[0][1] = button2;
        btn[0][2] = button3;
        btn[0][3] = button4;

        btn[1][0] = button5;
        btn[1][1] = button6;
        btn[1][2] = button7;
        btn[1][3] = button8;

        btn[2][0] = button9;
        btn[2][1] = button10;
        btn[2][2] = button11;
        btn[2][3] = button12;

        btn[3][0] = button13;
        btn[3][1] = button14;
        btn[3][2] = button15;
        btn[3][3] = button16;
    }
}
