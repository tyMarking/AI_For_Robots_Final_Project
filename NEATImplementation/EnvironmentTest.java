package NEATImplementation;
//package NEATImplementation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
class Agent extends JComponent{
	
	//Agent Parameters
	int proxLength = 100;
	double speed = 1;
	double angle = 2* Math.PI;
	int size = 100;
	double x = 100, y = 100;
	double x1, x2, x3, x4, y1, y2, y3, y4, xp0, xp1, yp0, yp1;
	double p0=0,p1=0;
	long currentTime =  System.currentTimeMillis();
	double shortest = -1;
	//Hamster Parameters
	int leftSpeed = 60;
	int rightSpeed = 60;
	
	//Frame Parameters
	int width = 1600;
	int height = 900;
	
	//Hamster Kinimatics Setup
	boolean updateAngle = true;
	double deltaAngle = 0;
	
	public Agent(int x, int y) {
		this.x = x;
		this.y = -y;
		setSize(width, height);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D)g;
		
	     double pi4 = (3.1415 / 4.0);
	     
	     double a1 = angle + pi4;
	     double a2 = angle + 3*pi4;
	     double a3 = angle + 5*pi4;
	     double a4 = angle + 7*pi4;
	
	     x1 = 20*Math.sqrt(2) * Math.cos(a1) + x;
	     x2 = 20*Math.sqrt(2) * Math.cos(a2) + x;
	     x3 = 20*Math.sqrt(2) * Math.cos(a3) + x;       
	     x4 = 20*Math.sqrt(2) * Math.cos(a4) + x;
	     xp0 = 100 * Math.cos(angle) + x1;
	     xp1 = 100 * Math.cos(angle) + x4;
	     
	     y1 = 20*Math.sqrt(2) * Math.sin(a1) - y;
	     y2 = 20*Math.sqrt(2) * Math.sin(a2) - y;
	     y3 = 20*Math.sqrt(2) * Math.sin(a3) - y;
	     y4 = 20*Math.sqrt(2) * Math.sin(a4) - y;

	     yp0 = 100 * Math.sin(angle) - -y1;
	     yp1 = 100 * Math.sin(angle) - -y4;
	     
	     Polygon robot = new Polygon();
	     
	     robot.addPoint((int)x1, (int)y1);
	     robot.addPoint((int)x2, (int)y2);
	     robot.addPoint((int)x3, (int)y3);
	     robot.addPoint((int)x4, (int)y4);
	
	     g.setColor(Color.blue);
	     g2d.fillPolygon(robot);
	     
	     g.setColor(Color.red);
	     g2d.fillOval((int)x1-5, (int)y1-5, 10, 10);
	     g.setColor(Color.cyan);
	     g2d.fillOval((int)x4-5, (int)y4-5, 10, 10);

	     g.setColor(Color.black);
	     g.drawLine((int)x1, (int)y1, (int)xp0, (int)yp0);
	     g.drawLine((int)x4, (int)y4, (int)xp1, (int)yp1);
	     
	    // for(int i = 1; i < 10; i ++) {
	    // 	g2d.fillOval((int)i*100,(int)i*100,10, 10);
	    // 	g2d.fillOval((int)i*100,(int)i*100, 10, 10);
	    // }
	     //System.out.println((int)((20*Math.sqrt(2)-2) * Math.sin(angle) + (int)((x1 + x3)/2.0)-5) + " - " + (int)((20*Math.sqrt(2)-2) * Math.cos(angle) - ((y1 + y3)/2.0)-5));
		
	}
	public Point getProx(int side, double[][] obs){
		Point p = new Point();
		for(int i = 0; i < obs.length; i ++) {
			for(int k = 0 ; k < proxLength; k += 1) {
			     double checkP0X = k * Math.cos(angle) + x1;
			     double checkP0Y = k * Math.sin(angle) + y1;
			     
			     if(checkP0X > obs[i][0] && checkP0X < obs[i][3] && checkP0Y < obs[i][2] && checkP0Y > obs[i][4]) {
			    	
			    	 p.setLocation(checkP0X,checkP0Y);
			    	 return p;
			     }
			}
		}
		
		
		return p;
	}
	public void update(long time) {
		double deltaT = currentTime/1000.0 - time/1000.0;
		
		if(angle > 2*Math.PI) {
			angle -= 2*Math.PI;
		}
		if(angle < 0) {
			angle += 2*Math.PI;
		}
		double newPos = (leftSpeed*deltaT + rightSpeed*deltaT)/2;
		double newAngle = angle + (leftSpeed-rightSpeed)*deltaT/35;
		double newX = x + newPos * Math.sin(angle) * 1.25;
		double newY = y + newPos * Math.cos(angle) * 1.25;
		
		x = newX;
		y = newY;
		angle = newAngle;
		currentTime = time;
		//System.out.println(x + " - " + y);
	}
	
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	public boolean isFilled(ArrayList <Double> list) {
		
		for(int k = 0; k < list.size(); k ++) {
     	if(list.get(k) != -10.0) {
     		return true;
     	}
     }
		return false;
	}
	public boolean isCollided(double[][] obs) {
		return false;
	}
	public double calculateAngle(double x1, double y1, double x2, double y2)
	{
	    double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
	    // Keep angle between 0 and 360
	    angle = angle + Math.ceil( -angle / 360 ) * 360;
	    return angle;
	}
}

class Object extends JComponent{
	int x = 0;
	int y = 0;
	double x1, x2, x3, x4, y1, y2, y3, y4;
	
	int size = 100;
	double facing = 45;
	double speed = 1.5;
	
	int width = 1600;
	int height = 900;
	
	Color c = new Color((int)(Math.random()*200),(int)(Math.random()*200),(int)(Math.random()*200));
	public Object(int xPos, int yPos) {//does work
		System.out.print("test");
		setSize(width, height);
		x = xPos;
		y = yPos;

		x1 = x;
	    x2 = x + size;
	    x3 = x - size/2.0;   
	    x4 = x + size/2.0;
	
	    y1 = y;
	    y2 = y + size;
	     
	    y3 = y - size/2.0;
	    y4 = y + size/2.0;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(c);
     
		g.fillRect(x + (int)(size/2.0), y + (int)(size/2.0), size, size);
	}
	public void update() {//does not get called
		System.out.print("update function");
		//x += Math.sin(Math.toRadians(facing))*speed;
		//y += Math.cos(Math.toRadians(facing))*speed;
	}
	public double[] getPos() {
		System.out.println(x1 + " - " + y1 + " --------- " + x2 + " - " + y2);//does print
		double corners [] = new double[4];
		corners[0] = (double)x1;
		corners[1] = (double)y1;

		corners[2] = (double)x2;
		corners[3] = (double)y2;
		
		return corners;
	}
	public boolean checkCol(Agent a) {
		
		return false;
		
		
	}
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
}
class Maze extends JComponent {
	double widthCells, heightCells;
	double height = 900, width = 1600;
	double cellHeight;
	double cellWidth;
	public Maze(int widthC, int heightC) {
		this.heightCells = heightC;
		this.widthCells = widthC;

		cellHeight = height/ heightCells;
		cellWidth = width / widthCells;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("Working");
     for(int i = 0; i < widthCells; i ++) {
     	for(int k =0; k < heightCells; k ++) {
     		g.fillRect((int)i*(int)cellWidth, k*(int)cellWidth, (int)cellWidth, (int)cellHeight);
     	}
     }
	}
	public void update() {
		repaint();
	}
}
class GUI extends JPanel implements ActionListener {
 private final int DELAY = 0;
 private Timer timer;
 public GUI() {
 	timer = new Timer(DELAY, this);
     timer.start();
     setDoubleBuffered(true);
 }
 
 public Timer getTimer() {
     return timer;
 }
 public void paintComponent(Graphics g) {//does not get called
     super.paintComponent(g);
     System.out.print("paintComponent");
	}
 public void actionPerformed(ActionEvent e) {
     repaint();
     revalidate();
 } 
}
class EnvironmentTest extends JFrame {
	boolean toggle = false;
	static int key;
	boolean left = false, right = false;

	public static GUI panel;

 public EnvironmentTest() {
 	panel = new GUI();
 	addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
             Timer timer = panel.getTimer();
             timer.stop();
         }
     }
     );
 	
     setTitle("Environment Testing");
     setSize(1600,900);
     setLocationRelativeTo(null);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setVisible(true);    
 }
 public static void main(String[] args) {
 	EnvironmentTest envi = new EnvironmentTest();
 	
 	Agent a = new Agent(300,300);
 	Maze m = new Maze(5,5);
 	Object o = new Object(100,100);
 	envi.add(a);
 	a.add(m);
 	a.add(o);
 	
 	int counter = 0;
 	double obs[][] = new double[1][4];
 	for(int i = 0; i < obs.length; i ++) {
 		obs[i] = o.getPos();
 		System.out.println(obs[i][0] + " - " + obs[i][3]);//does print
 	}
 	
 	while(true) {
 		//Repaints GUI, triggering refresh of all jComp's
 		envi.getContentPane().validate();
 		envi.getContentPane().repaint();
 		
 		// Calls agents update function
 		
 		a.update(System.currentTimeMillis());
 		m.update();
     	right(a);
     	


 		counter ++;
 		
 		//System.out.println(counter);
 		/*
 		try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}*/
 	}
 }
 public static void right(Agent a) {
 	a.rightSpeed = -60;
		a.leftSpeed = 60;
 }
 public static void left(Agent a) {
 	a.rightSpeed = 60;
		a.leftSpeed = -60;
 }
 public static void forward(Agent a) {
 	a.rightSpeed = 60;
		a.leftSpeed = 60;
 }
 public static void stop(Agent a) {
 	a.rightSpeed = 0;
		a.leftSpeed = 0;
 }
 public static void print(String input) {
 	System.out.println(input);
 }
} 