package NEATImplementation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
class Agent extends JComponent{
	
	//Agent Parameters
	double speed = 1;
	double angle = 0;
	int size = 100;
	double x = 100, y = -100;
	long currentTime =  System.currentTimeMillis();

	//Hamster Parameters
	int leftSpeed = 60;
	int rightSpeed = 60;
	
	//Frame Parameters
	int width = 1600;
	int height = 900;
	
	//Hamster Kinimatics Setup
	boolean updateAngle = true;
	double deltaAngle = 0;
	
	public Agent() {
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

        double x1 = 20*Math.sqrt(2) * Math.cos(a1) + x;
        double x2 = 20*Math.sqrt(2) * Math.cos(a2) + x;
        double x3 = 20*Math.sqrt(2) * Math.cos(a3) + x;       
        double x4 = 20*Math.sqrt(2) * Math.cos(a4) + x;

        double y1 = 20*Math.sqrt(2) * Math.sin(a1) - y;
        double y2 = 20*Math.sqrt(2) * Math.sin(a2) - y;
        double y3 = 20*Math.sqrt(2) * Math.sin(a3) - y;
        double y4 = 20*Math.sqrt(2) * Math.sin(a4) - y;
        
        Polygon robot = new Polygon();
        
        robot.addPoint((int)x4, (int)y4);
        robot.addPoint((int)x3, (int)y3);
        robot.addPoint((int)x2, (int)y2);
        robot.addPoint((int)x1, (int)y1);
        
        g2d.draw(robot);
	}
	public void update(long time) {
		double deltaT = currentTime/1000.0 - time/1000.0;
		
		if(angle > Math.PI) {
			angle -= 2*Math.PI;
		}
		
		double newPos = (leftSpeed*deltaT + rightSpeed*deltaT)/2;
		double newAngle = angle + (leftSpeed-rightSpeed)*deltaT/35;
		double newX = x + newPos * Math.sin(angle) * 1.25;
		double newY = y + newPos * Math.cos(angle) * 1.25;
		
		x = newX;
		y = newY;
		angle = newAngle;
		System.out.println(x + " - " +  y);
		currentTime = time;
	}
	
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
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
	
	int size = 100;
	double facing = 45;
	double speed = 1.5;
	
	int width = 1600;
	int height = 900;
	
	Color c = new Color((int)(Math.random()*200),(int)(Math.random()*200),(int)(Math.random()*200));
	public Object(int xPos, int yPos) {
		setSize(width, height);
		x = xPos;
		y = yPos;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(c);
		g.fillOval(x + (int)(size/2.0), y + (int)(size/2.0), size, size);
	}
	public void update() {
		//x += Math.sin(Math.toRadians(facing))*speed;
		//y += Math.cos(Math.toRadians(facing))*speed;
	}
	public boolean checkCol(Agent a) {
		if(distance(x,y,a.x,a.y) < size) {
			return true;
		}
		return false;
	}
	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
	}
    public void actionPerformed(ActionEvent e) {
        repaint();
        revalidate();
    } 
}
class Environment extends JFrame {
	boolean toggle = false;
	static int key;
	boolean left = false, right = false;

	public static GUI panel;

    public Environment() {
    	panel = new GUI();
    	addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Timer timer = panel.getTimer();
                timer.stop();
            }
        }
        );
    	
        setTitle("Environment");
        setSize(1600,900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);    
    }
    public static void main(String[] args) {
    	Environment envi = new Environment();
    	
    	Agent a = new Agent();
    	Object o = new Object(100,100);
    	envi.add(a);
    	a.add(o);
    	int counter = 0;
    	
    	while(true) {
    		//Repaints GUI, triggering refresh of all jComp's
    		envi.getContentPane().validate();
    		envi.getContentPane().repaint();
    		
    		// Calls agents update function
    		a.rightSpeed = 30;
    		a.leftSpeed = 50;
    		a.update(System.currentTimeMillis());
    		
    		
    		counter += 1;
    		try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
    	}
    }
    public static void print(String input) {
    	System.out.println(input);
    }
}
    