package map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class Map extends JFrame {
	//constants
	static final int MAPSIZE = 20;
	static final int NUMSEED = MAPSIZE/2;  //seeds for starting continents
	static final int EMPTY = 0;
	static final int LAND = 1;
	static final int WATER = 2;
	static final int OCEAN = 3;

	//instance (global) variables
	int [][] numArray = new int [MAPSIZE][MAPSIZE];
	DrawingPanel gridPanel;
	int gpWidth, gpLength;
	int mx=0;
	int my=0;
	

	public static void main(String[] args){	
		new Map();
	}

	Map(){
		//makeRandomLand();
		//createWater();
		makeContinents();		
		createAndShowGUI();
	}

	void makeRandomLand() {
		for (int i=0; i<numArray.length; i++){
			for (int j=0; j<numArray[i].length; j++) {
				numArray[i][j] = (int)(Math.random() + 0.5);		// 0.5 --- 1.4999   INT=0 or 1				
			}
		}	
		//console output
		for(int i=0;i<numArray.length; i++){
			for(int j=0; j<numArray[i].length; j++)
				System.out.print(numArray[i][j]+" "); 
			System.out.println();
		}
	}

	void createWater(int x, int y){
		if (numArray[x][y] == LAND) return;
		if (numArray[x][y] == EMPTY) numArray[x][y] = WATER;
//		if (numArray[x+1][y] == EMPTY) numArray[x+1][y] = WATER;
		if (x < MAPSIZE-1 && numArray[x+1][y] == EMPTY) createWater(x+1,y);
		if (x>0 && numArray[x-1][y]== EMPTY) createWater(x-1,y);
		if (y < MAPSIZE-1 && numArray[x][y+1] == EMPTY) createWater(x,y+1);
		if (y > 0 && numArray[x][y-1] == EMPTY) createWater(x,y-1);
		if (x == 0) createOcean(x,y);
		if (x == MAPSIZE-1)createOcean(x,y);
		if (y == 0) createOcean(x,y);
		if (y == MAPSIZE-1)createOcean(x,y);
	}

	void createOcean(int x, int y) {
		if (numArray[x][y] == LAND) return;
		if (numArray[x][y] == EMPTY || numArray[x][y] == WATER) numArray[x][y] = OCEAN;
		if (x < MAPSIZE-1 && (numArray[x+1][y] == EMPTY || numArray[x+1][y] == WATER)) createOcean(x+1,y);
		if (x>0 && (numArray[x-1][y]== EMPTY || numArray[x-1][y]== WATER)) createOcean(x-1,y);
		if (y < MAPSIZE-1 && (numArray[x][y+1] == EMPTY || numArray[x][y+1] == WATER)) createOcean(x,y+1);
		if (y > 0 && (numArray[x][y-1] == EMPTY || numArray[x][y-1] == WATER)) createOcean(x,y-1);
	}

	void makeContinents() {
		int stop = 0;
		for (int l=0; l<numArray.length; l++){
			for (int k=0; k<numArray.length; k++){
				numArray[l][k] = 0;
			}
		}
		//make a few random starting points for land
		for (int k=0; k < NUMSEED; k++){
			int i = (int)(Math.random() * MAPSIZE);
			int j = (int)(Math.random() * MAPSIZE);
			numArray[i][j]= LAND;
		}
		//
//		while(5<MAPSIZE*MAPSIZE/2){
		while(stop < MAPSIZE*MAPSIZE/2){

			int i = (int)(Math.random() * MAPSIZE);
			int j = (int)(Math.random() * MAPSIZE);

			if (numArray[i][j] == LAND) continue;

			if (i < MAPSIZE-1 && j< MAPSIZE-1 && i>0 && j>0 && (numArray[i+1][j]==LAND 
					|| numArray[i-1][j]==LAND || numArray[i][j+1]==LAND || numArray[i][j-1] == LAND)) {
				numArray[i][j] = LAND;
				stop++;
			}
		}
	}

	void createAndShowGUI() {
		JFrame window = new JFrame("Map and Continent");
		window.setSize(497,490);
		window.setTitle("Map and Continent");
		window.setLocationRelativeTo(null);
		//window.setBounds(30, 30, 450, 450);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gridPanel = new DrawingPanel();
		gridPanel.addMouseMotionListener(new MyMML());
		gridPanel.addMouseListener(new MyML());
		JButton btn1 = new JButton();
		btn1.setText("Make random land");
		btn1.addActionListener(new MyL());
		JButton btn2 = new JButton();
		btn2.setText("Make random continent");
		btn2.addActionListener(new My());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(25,50));
		buttonPanel.add(btn1);
		buttonPanel.add(btn2);
		window.add(buttonPanel, BorderLayout.NORTH);
		
		window.add(gridPanel, BorderLayout.CENTER);
		//window.add(buttonPanel);
		window.setResizable(true);
		window.setVisible(true);
	}


	class DrawingPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g); //clear screen first

			//sizes of Jpanel
			gpWidth = getSize().width;
			gpLength = getSize().height;
			int numrow = gpWidth/MAPSIZE;
			int numcolumn = gpLength / MAPSIZE;

			//horizontal
			for (int i = 0; i < MAPSIZE+1; i++){
				g.drawLine(0, i * numcolumn, MAPSIZE*numrow, i * numcolumn);
			}
			//vertical
			for (int i = 0; i < MAPSIZE+1; i++){
				g.drawLine(i * numrow, 0, i * numrow, MAPSIZE*numcolumn);
			}

			for (int i=0; i<numArray.length; i++){
				for (int j=0; j<numArray[i].length; j++) {
					if(numArray[i][j] == EMPTY) g.setColor(Color.WHITE.darker());
					if(numArray[i][j] == LAND) g.setColor(Color.GREEN);
					if(numArray[i][j] == WATER) g.setColor(new Color(50,130,255));
					if(numArray[i][j] == OCEAN) g.setColor(Color.BLUE);

					g.fillRect(i * numrow +1, j * numcolumn +1, numrow-1, numcolumn-1);
//					g.fillOval(i * numrow + numrow/2, j * numcolumn + numrow/2, numrow-1, numcolumn-1);
				}
			}

			//g.setColor(Color.BLUE);
			//g.drawString("mx="+mx + " my=" + my, 100,50);
			//g.drawString("width="+width + " numrow=" + numrow, 100,80);
		}
	}
	class MyMML implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
//			mx = e.getX();
//			my = e.getY();
//			if  (mx <5 && my < 5) makeRandomLand();
//			gridPanel.repaint();
		}
	}
	class MyML implements MouseListener {

		@Override
		public void mousePressed (MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			mx = x / (int)((gpWidth/MAPSIZE) + 0.5); 
			my = y / (int)((gpLength/MAPSIZE) + 0.5);

//			numArray[mx][my] = WATER;
			createWater(mx,my);
			gridPanel.repaint();			

		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}
	class MyL implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			makeRandomLand();
			gridPanel.repaint();	
		}
	}
	class My implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			makeContinents();
			gridPanel.repaint();	
		}
	}
}