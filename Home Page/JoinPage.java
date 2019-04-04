package p1;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

public class ay5ra extends JFrame {
    JButton joinbtn;
    JList t;

    public ay5ra() {
        String Languages [] = {"Server 1","Server 2","Server 3","Server 1","Server 2","Server 3","Server 1","Server 2","Server 3","Server 1","Server 2","Server 3"};
        this.setBounds(500, 200, 400, 400);
        joinbtn = new JButton("Join");
        joinbtn.setFocusPainted(false);
        joinbtn.setContentAreaFilled(false);
        JPanel panel = new JPanel(new GridBagLayout());
        joinbtn.addActionListener(this::actionPerformed);
        t = new JList(Languages);
        JScrollPane list = new JScrollPane(t);
        panel.setLayout(null);
        setTitle("Join Page");

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
//        joinbtn.setBorder(border);
        JLabel s = new JLabel("Please select server to join");
        s.setBounds(110,20,160,20);
//        list.setBorder(border);
        list.setBounds(5,50,385,230);
        joinbtn.setBounds(300,300,80,40);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        panel.add(s);
        panel.add(list);
        panel.add(joinbtn);

        this.add(panel);
        this.setVisible(true);
        

    }
    public static void main(String [] args){
        ay5ra s = new ay5ra();


    }

    public void actionPerformed(ActionEvent e){

        }
}