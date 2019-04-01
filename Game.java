package p1;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Font;

import java.math.*;

import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class gui2 {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui2 window = new gui2();
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
	public gui2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	JButton btn[][] = new JButton[4][4];
	Color white = Color.white;
	Color red = Color.red;
	Color blue = Color.BLUE;
	boolean player=false;
	boolean b=true;
	
	private void initialize() {
		frame = new JFrame();
//		frame.setBounds(0, 0, 860, 500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		panel.setBackground(Color.black);
		frame.setVisible(true);
		
		
		int height = frame.getHeight();
		int width = frame .getWidth();
		height-=80;
		width-=80;
		height/=4;
		width/=4;
		Font f= new Font("",Font.BOLD,50);
		
		for(int i=0;i<4;++i){
			for (int j=0;j<4;++j){
				int x=(i*4)+j+1;
				btn[i][j]=new JButton(""+x);
				btn[i][j].setFont(f);
				btn[i][j].setBounds((i*width+20),(j*height+20),(i*width+width),(j*height + height));
				btn[i][j].setSize(width-20, height-20);
				btn[i][j].setBackground(white);
				btn[i][j].addActionListener(this::actionPerformed);
				panel.add(btn[i][j]);
			}
		}
		
		
	}

	public void tst(int x , int y){
		int first,second;
		for(int i=0;i<4;++i){
			for(int j=0;j<4;++j){
				first=Math.abs(i-x);
				second=Math.abs(j-y);
				if((first==1 && second ==0 )||( first==0 && second==1) ) continue;
				else{
					btn[i][j].setEnabled(false);
				}
			}
		}
		
	}
	public void tst2(int x , int y){
		for(int i=0;i<4;++i)
			for(int j=0;j<4;++j)
				if(btn[i][j].getBackground()==white) btn[i][j].setEnabled(true);
			
		
		
	}

	public boolean ended(){
		
		for(int i=1;i<3;++i){
			for (int j=1;j<3;++j){
				if(btn[i][j].isEnabled()==true)
					if(btn[i+1][j].isEnabled()==true ||btn[i-1][j].isEnabled()==true || btn[i][j+1].isEnabled()==true || btn[i][j-1].isEnabled()==true)
						return false;
			}
		}
		
		for(int j=1;j<3;++j){
			int i=0;
			if(btn[i][j].isEnabled()==true)
				if(btn[i][j-1].isEnabled()==true||btn[i+1][j].isEnabled()==true||btn[i][j+1].isEnabled()==true)
					return false;
		}

		for(int j=1;j<3;++j){
			int i=3;
			if(btn[i][j].isEnabled()==true)
				if(btn[i-1][j].isEnabled()==true||btn[i][j-1].isEnabled()==true||btn[i][j+1].isEnabled()==true)
					return false;
		}

		for (int i=1;i<2;++i){
			int j=0;
			if(btn[i][j].isEnabled()==true)
					if(btn[i+1][j].isEnabled()==true || btn[i-1][j].isEnabled()==true || btn[i][j+1].isEnabled()==true)
						return false;
		}
		for (int i=1;i<2;++i){
			int j=3;
			if(btn[i][j].isEnabled()==true)
				if(btn[i+1][j].isEnabled()==true || btn[i-1][j].isEnabled()==true || btn[i][j-1].isEnabled()==true)
					return false;
		}
		
		if(btn[0][0].isEnabled()==true)
			if(btn[0][1].isEnabled()==true || btn[1][0].isEnabled()==true)
				return false;
			
		

		if(btn[3][3].isEnabled()==true)
			if(btn[3][2].isEnabled()==true || btn[2][3].isEnabled()==true)		
				return false;	
			
		
		
		if (btn[0][3].isEnabled()==true)
			if(btn[0][2].isEnabled()==true || btn[1][3].isEnabled()==true)
				return false;	
			
		
		
		if (btn[3][0].isEnabled()==true)
			if(btn[2][0].isEnabled()==true || btn[3][1].isEnabled()==true)	
				return false;		
			
		
		return true;
	}
	
	int x,y;
	public void actionPerformed(ActionEvent e) {
		for(int i=0;i<4;++i){
			for(int j=0;j<4;++j){
				if(e.getSource()==btn[i][j]){
					btn[i][j].setBackground((player)?red:blue);
					if(b){
						x=i;
						y=j;
						tst(i,j);
						b=!b;
						btn[i][j].setEnabled(true);
					}
					
					else if(!b){
						if(i==x && j==y){
							btn[i][j].setBackground(white);
							player=!player;
						}
						else{
							btn[i][j].setEnabled(false);
							btn[x][y].setEnabled(false);
						}
						tst2(i,j);
						b=!b;
						player=!player;
					}
				}
			}
		}
		
		if(b && ended()==true){
			int winner = (player)?1:2;
			JOptionPane.showMessageDialog(null,"player "+winner +" won !" );
			frame.setVisible(false);
		}
		
	}
	
}
