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
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
class Agent extends JComponent{
	
	//Agent Parameters
	double speed = 1;
	double angle = 2* Math.PI;
	int size = 100;
	double x = 100, y = 100;
	double x1, x2, x3, x4, y1, y2, y3, y4;
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

        y1 = 20*Math.sqrt(2) * Math.sin(a1) - y;
        y2 = 20*Math.sqrt(2) * Math.sin(a2) - y;
        y3 = 20*Math.sqrt(2) * Math.sin(a3) - y;
        y4 = 20*Math.sqrt(2) * Math.sin(a4) - y;
        
        Polygon robot = new Polygon();
        
        robot.addPoint((int)x1, (int)y1);
        robot.addPoint((int)x2, (int)y2);
        robot.addPoint((int)x3, (int)y3);
        robot.addPoint((int)x4, (int)y4);

        g.setColor(Color.blue);
        g2d.fillPolygon(robot);
        g.setColor(Color.red);
        g2d.fillOval((int)x1-5, (int)y1-5, 10, 10);

        g2d.fillOval((int)x2-5, (int)y2-5, 10, 10);

        for(int i = 1; i < 10; i ++) {
        	g2d.fillOval((int)i*100,(int)i*100,10, 10);
        	g2d.fillOval((int)i*100,(int)i*100, 10, 10);
        }
        //System.out.println((int)((20*Math.sqrt(2)-2) * Math.sin(angle) + (int)((x1 + x3)/2.0)-5) + " - " + (int)((20*Math.sqrt(2)-2) * Math.cos(angle) - ((y1 + y3)/2.0)-5));
	
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
	public void getProx(int side, double[][] obs, double angleG) {

		double angleGot = angleG;
		double px;
		double py;
		
		//angleGot has no value
	    if (angleGot < 0) {
	        angleGot += Math.PI*2;
	    if (side == 0) {
	        px = x1;
	        py = y1;//emitter location
	    }else {
	    	px = x2;
	    	py = y2;
	    }
	    //px = (20*Math.sqrt(2)-2) * Math.sin(pAngle) + (int)((x1 + x3)/2.0)-5; //emiter pos of left sensor
	    //py = (20*Math.sqrt(2)-2) * Math.cos(pAngle) - ((y1 + y3)/2.0)-5; //emiter pos of right sensor
	    
	    ArrayList <Double> intersection = radialIntersect(angleGot, px, py, obs);
	
	    if(isFilled(intersection)){
	        double x_i = intersection.get(0);
	        double y_i = intersection.get(1);
	        System.out.println(x_i + " - " + y_i);
	        if (side == 0) {
	            p0 = Math.sqrt((y_i-py)*(y_i-py) + (x_i-px)*(x_i-px));
	            if (p0 > 120){
	                //p0 = 0;
	            }
	        }else {
	            p1 = Math.sqrt((y_i-py)*(y_i-py) + (x_i-px)*(x_i-px));
	            if (p1 > 120){
	                //p1 = 0;
	            }
	        }
	    } else {
		        if (side == 0) {
		            p0 = 0;
		        }else {
		            p1 = 0;
		        }
	        }
	     }

        //System.out.println(p0 + " - " + p1);
	}
	public ArrayList<Double> radialIntersect (double a_r, double x_e,double y_e, double obs[][]) {//does not run
	    
	    ArrayList <Double> voidList= new ArrayList<Double>();
	    for(int k = 0; k < voidList.size(); k ++) {
    		voidList.set(k, -10.0);
    	}
	    for(int i = 0; i < obs.length; i++) {//for loop does not run

		    System.out.println("\n");
	        double ox1 = obs[i][0];
	        double oy1 = obs[i][1];
	        System.out.println(obs[i][1]);
	        double ox2 = obs[i][2];
	        double oy2 = obs[i][3];
	        
	        double x_i, y_i;
	        ArrayList<Double> p_new = new ArrayList<Double>();
	        ArrayList<Double> p_intersect = new ArrayList<Double>();
	        p_new.add(0.0);
	        p_new.add(0.0);
	        p_new.add(0.0);
	        
	        // first quadron
	        if ((a_r >= 0) && (a_r < 3.1415/2.0)){//does not get called
	        	System.out.print("first q");
	            //#print "radial intersect: ", x_e, y_e
	            if (y_e < oy1) {
	                x_i = x_e + Math.tan(a_r) * (oy1 - y_e);
	                y_i = oy1;
	                if (x_i > ox1 && x_i < ox2) {
	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,1.0); // 1 indicating intersecting a bottom edge of obs
	                }
	            }
	            if (x_e < ox1) {
	                x_i = ox1;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (ox1 - x_e);
	                if (y_i > oy1 && y_i < oy2) {
	                    //p_new = [x_i, y_i, 2] # left edge of obs

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,2.0);
	                }
	            }
	        }
	        // second quadron
	        if ((a_r >= 3.1415/2) && (a_r < 3.1415)){//does not get called
	        	System.out.print(" second q");
	            if (y_e > oy2) {
	                x_i = x_e + Math.tan(a_r) * (oy2 - y_e);
	                y_i = oy2;
	                if (x_i > ox1 && x_i < ox2) {
	                    //p_new = [x_i, y_i, 3] # top edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,3.0);
	                }
	            }
	            if (x_e < ox1) {
	                x_i = ox1;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (ox1 - x_e);
	                if (y_i > oy1 && y_i < oy2) {
	                    //p_new = [x_i, y_i, 2] #left edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,2.0);
	                }
	            }
	        }
	        // third quadron
	        if ((a_r >= 3.1415) && (a_r < 1.5*3.1415)){//does not get called
	        	System.out.print(" third q");
	            if (y_e > oy2) {
	                x_i = x_e + Math.tan(a_r) * (oy2 - y_e);
	                y_i = oy2;
	                if (x_i > ox1 && x_i < ox2) {
	                    //p_new = [x_i, y_i, 3] #top edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,3.0);
	                }
	            }
	            if (x_e > ox2) {
	                x_i = ox2;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (ox2 - x_e);
	                if (y_i > oy1 && y_i < oy2) {
	                    //p_new = [x_i, y_i, 4] # right edge
	                	System.out.println("Output" + x_i);
	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,4.0);
	                }
	            }
	        }
	        // fourth quadron
	        if ((a_r >= 1.5*3.1415) && (a_r < 6.283)){//does not get called

	        	System.out.println(y_e  + " - " + oy1);
	            if (y_e < oy1) {
	                x_i = x_e + Math.tan(a_r) * (oy1 - y_e);
	                y_i = oy1;

		        	System.out.println("Bottom Edge Casted - " + x_i +","+y_i);
	                if (x_i > ox1 && x_i < ox2) {
	                    //p_new = [x_i, y_i, 1] # bottom edge

	                	System.out.println("Output" + x_i + " - " + y_i);
	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,1.0);
	                }
	            }
	            if (x_e > ox2) {
	                x_i = ox2;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (ox2 - x_e);
	                if (y_i > oy1 && y_i < oy2) {
	                    //p_new = [x_i, y_i, 4] # right edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,4.0);
	                }
	            }
	        }
	        if(isFilled(p_new)){//does not get called
	            double dist = Math.abs(p_new.get(0)-x_e) + Math.abs(p_new.get(1)-y_e);
	        	System.out.println("New prox's Reorded - Distance: " + dist);
	            if(shortest != -1){
	                if (dist < shortest){
	                    shortest = dist;
	                    p_intersect = p_new;
	                    
	                    System.out.println("Intersect: " + p_intersect.get(0));
	                }
	            } else {
	            	System.out.println("Shortest is Set");
	                shortest = dist;
	                p_intersect = p_new;
	            }
	            for(int k = 0; k < p_new.size(); k ++) {
	        		p_new.set(k, -10.0);
	        	}
	        }
	        
	        System.out.print("P_Intersect Is Recorded: " + isFilled(p_intersect));
		    if(isFilled(p_intersect)){
		    	System.out.println("WORKING1");
		        return p_intersect;
		    } else {

		    	//System.out.println("WORKING");
		        return voidList;
		    }
		    
	    }
	    return voidList;
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
        	a.getProx(0,obs, a.angle);
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