package p1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class gui3 {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui3 window = new gui3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	

	/**
	 * Create the application.
	 */
	public gui3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	JButton btn[][]= new JButton [3][3];
	Color white = Color.white;
	Color red = Color.red;
	Color blue = Color.BLUE;
	boolean player=true;
	int n1,n2;
	boolean next=true,notNext=true;
	
	private void initialize() {
		frame = new JFrame();
//		frame.setBounds(100, 100, 450, 300);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		panel.setBackground(Color.black);
		frame.setVisible(true);
		
		int height = frame.getHeight();
		int width = frame .getWidth();
		height-=80;
		width-=80;
		height/=3;
		width/=3;
		Font f= new Font("",Font.BOLD,50);
		
		for(int i=0;i<3;++i){
			for(int j=0;j<3;++j){
				int x=(i*3)+j+1;
				btn[i][j]=new JButton(""+x);
				btn[i][j].setFont(f);
				btn[i][j].setBounds((j*width+20),(i*height+20),(j*height + height),(i*width+width));
				btn[i][j].setSize( width-20,height-20);
				btn[i][j].setBackground(white);
				btn[i][j].addActionListener(this::actionPerformed);
				panel.add(btn[i][j]);
				
				
			}
		}
		
		
		
	}
	
	boolean ended(){
		for(int i=0;i<3;++i)
			if(btn[i][0].getText().equals(btn[i][1].getText()) && btn[i][2].getText().equals(btn[i][0].getText()))
				return true;

		for(int i=0;i<3;++i)
			if(btn[0][i].getText().equals(btn[1][i].getText()) && btn[2][i].getText().equals(btn[0][i].getText()))
				return true;
		
		if(btn[0][0].getText().equals(btn[1][1].getText()) && btn[2][2].getText().equals(btn[0][0].getText()))
			return true;

		if(btn[0][2].getText().equals(btn[1][1].getText()) && btn[1][1].getText().equals(btn[2][0].getText()))
			return true;
		
		return false;
	}
	
	Font p1= new Font("X",Font.BOLD,50);
	Font p2= new Font("O",Font.BOLD,50);
	int counter=0;
	String res;
	int x,y;
	
	
	public void P1(){
		String n=(player)?"X":"O";
		for(int i=0;i<3;++i){
			for(int j=0;j<3;++j){
				if(btn[i][j].getText().equals(n)) btn[i][j].setEnabled(true);
				else btn[i][j].setEnabled(false);
			}
		}
	}
	
	public void Next(){
		for(int i=0;i<3;++i){
			for(int j=0;j<3;++j){
				if(btn[i][j].getText().equals("X") || btn[i][j].getText().equals("O") ){
					btn[i][j].setEnabled(false);
				}
				else{
					btn[i][j].setEnabled(true);
				}
			}
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {
		next=true;
		for(int i=0;i<3;++i){
			for(int j=0;j<3;++j){
				
				if(counter>=6){
					if(notNext==true){
						for(int x=0;x<3;++x){
							for(int y=0;y<3;++y){
								if(e.getSource()==btn[x][y]){
									n1=x;
									n2=y;
								}
							}
							next=false;
							notNext=false;
							Next();
							btn[n1][n2].setEnabled(true);
						}
					}
					
					if(next){
						for(int x=0;x<3;++x){
							for(int y=0;y<3;++y){
								if(e.getSource()==btn[x][y]){
									btn[n1][n2].setEnabled(true);
									String btngan=btn[n1][n2].getText();
									btn[n1][n2].setText(""+((n1*3)+n2+1));
									btn[n1][n2].setForeground(null);
									btn[x][y].setText(btngan);
									btn[x][y].setForeground(btngan.equals("X")?blue:red);
									btn[x][y].setEnabled(false);
									if(x==n1 && y==n2) player=!player;
								}
							}		
						}
						
						player = !player;
						P1();
						notNext=true;
						if(ended()){
							int winner = (player)?2:1;
							JOptionPane.showMessageDialog(null,"player "+winner +" won !" );
							frame.setVisible(false);
						}
						return;
					}
				}
				
				
				else if (counter<6){
					if(e.getSource()==btn[i][j]){
							btn[i][j].setText((player)?"X":"O");
							btn[i][j].setForeground((btn[i][j].getText().equals("X")?blue:red));
							btn[i][j].setEnabled(false);
							player=!player;
							counter++;
						if(counter ==6){
							P1();
							return ;
						}
					}
				}
				
			}
		}
		
		if(ended()){
			int winner = (player)?2:1;
			JOptionPane.showMessageDialog(null,"player "+winner +" won !" );
			frame.setVisible(false);
		}
	}

}
