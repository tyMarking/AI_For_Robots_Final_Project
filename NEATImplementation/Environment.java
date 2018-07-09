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
	double angle = 0;
	int size = 100;
	double x = 100, y = 100;
	double x1, x2, x3, x4, y1, y2, y3, y4;
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

        x1 =20*Math.sqrt(2) * Math.cos(a1) + x;
        x2 =20*Math.sqrt(2) * Math.cos(a2) + x;
        x3 =20*Math.sqrt(2) * Math.cos(a3) + x;       
        x4 =20*Math.sqrt(2) * Math.cos(a4) + x;

        y1 = 20*Math.sqrt(2) * Math.sin(a1) - y;
        y2 = 20*Math.sqrt(2) * Math.sin(a2) - y;
        y3 = 20*Math.sqrt(2) * Math.sin(a3) - y;
        y4 = 20*Math.sqrt(2) * Math.sin(a4) - y;
        
        Polygon robot = new Polygon();
        
        robot.addPoint((int)x1, (int)y1);
        robot.addPoint((int)x2, (int)y2);
        robot.addPoint((int)x3, (int)y3);
        robot.addPoint((int)x4, (int)y4);
        
        g2d.fillPolygon(robot);
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
		currentTime = time;
		//System.out.println(x + " - " + y);
	}
	public void getProx(int side, double[][] obs, int angleG) {
		int angleGot = angleG;
		double pAngle;
		double px;
		double py;
		boolean p0Vision, p1Vision;
		double p0=0,p1=0;
		
	    if (angleGot < 0) {
	        angleGot += 6.283;
	    if (side == 0) {
	        pAngle = angleGot - 3.1415/4.5; //emitter location
	    }else {
	    	pAngle = angleGot + 3.1415/4.5;//emitter location
	    }
	    px = (20*Math.sqrt(2)-2) * Math.sin(pAngle) + x; //emiter pos of left sensor
	    py = (20*Math.sqrt(2)-2) * Math.cos(pAngle) + height - y; //emiter pos of right sensor
	    
	    ArrayList <Double> intersection = radialIntersect(angleGot, px, py, obs);
	
	    if(checkFalse(intersection)){
	        double x_i = intersection.get(0);
	        double y_i = intersection.get(1);
	        System.out.println(x_i + " - " + y_i);
	        if (side == 0) {
	            p0 = Math.sqrt((y_i-py)*(y_i-py) + (x_i-px)*(x_i-px));
	            if (p0 > 120){
	                p0 = 0;
	            }
	        }else {
	            p1 = Math.sqrt((y_i-py)*(y_i-py) + (x_i-px)*(x_i-px));
	            if (p1 > 120){
	                p1 = 0;
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
	public ArrayList<Double> radialIntersect (double a_r, double x_e,double y_e, double obs[][]) {
	    double shortest = -1;

	    ArrayList <Double> voidList= new ArrayList<Double>();
	    for(int k = 0; k < voidList.size(); k ++) {
    		voidList.set(k, -10.0);
    	}
	    for(int i = 0; i < obs.length; i ++) {
	        x1 = obs[i][0];
	        y1 = obs[i][1];
	        x2 = obs[i][2];
	        y2 = obs[i][3];
	        
	        double x_i, y_i;
	        ArrayList<Double> p_new = new ArrayList<Double>();
	        ArrayList<Double> p_intersect = new ArrayList<Double>();
	        p_new.add(0.0);
	        p_new.add(0.0);
	        p_new.add(0.0);
	        
	        // first quadron
	        if ((a_r >= 0) && (a_r < 3.1415/2.0)){
	            //#print "radial intersect: ", x_e, y_e
	            if (y_e < y1) {
	                x_i = x_e + Math.tan(a_r) * (y1 - y_e);
	                y_i = y1;
	                if (x_i > x1 && x_i < x2) {
	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,1.0); // 1 indicating intersecting a bottom edge of obs
	                }
	            }
	            if (x_e < x1) {
	                x_i = x1;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (x1 - x_e);
	                if (y_i > y1 && y_i < y2) {
	                    //p_new = [x_i, y_i, 2] # left edge of obs

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,2.0);
	                }
	            }
	        }
	        // second quadron
	        if ((a_r >= 3.1415/2) && (a_r < 3.1415)){
	            if (y_e > y2) {
	                x_i = x_e + Math.tan(a_r) * (y2 - y_e);
	                y_i = y2;
	                if (x_i > x1 && x_i < x2) {
	                    //p_new = [x_i, y_i, 3] # top edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,3.0);
	                }
	            }
	            if (x_e < x1) {
	                x_i = x1;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (x1 - x_e);
	                if (y_i > y1 && y_i < y2) {
	                    //p_new = [x_i, y_i, 2] #left edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,2.0);
	                }
	            }
	        }
	        // third quadron
	        if ((a_r >= 3.1415) && (a_r < 1.5*3.1415)){
	            if (y_e > y2) {
	                x_i = x_e + Math.tan(a_r) * (y2 - y_e);
	                y_i = y2;
	                if (x_i > x1 && x_i < x2) {
	                    //p_new = [x_i, y_i, 3] #top edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,3.0);
	                }
	            }
	            if (x_e > x2) {
	                x_i = x2;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (x2 - x_e);
	                if (y_i > y1 && y_i < y2) {
	                    //p_new = [x_i, y_i, 4] # right edge
	                	System.out.println("Output" + x_i);

	                	System.out.println(Math.random());
	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,4.0);
	                }
	            }
	        }
	        // fourth quadron
	        if ((a_r >= 1.5*3.1415) && (a_r < 6.283)){
	            if (y_e < y1) {
	                x_i = x_e + Math.tan(a_r) * (y1 - y_e);
	                y_i = y1;
	                if (x_i > x1 && x_i < x2) {
	                    //p_new = [x_i, y_i, 1] # bottom edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,1.0);
	                }
	            }
	            if (x_e > x2) {
	                x_i = x2;
	                y_i = y_e + Math.tan(3.1415/2 - a_r) * (x2 - x_e);
	                if (y_i > y1 && y_i < y2) {
	                    //p_new = [x_i, y_i, 4] # right edge

	                    p_new.set(0,x_i);
	                    p_new.set(1,y_i);
	                    p_new.set(2,4.0);
	                }
	            }
	        }
	        if(checkFalse(p_new)){
	            double dist = Math.abs(p_new.get(0)-x_e) + Math.abs(p_new.get(1)-y_e);
	            if(shortest != -1){
	                if (dist < shortest){
	                    shortest = dist;
	                    p_intersect = p_new;
	                    System.out.println(p_intersect.get(0));
	                }
	            } else {
	                shortest = dist;
	                p_intersect = p_new;
	            }
	            for(int k = 0; k < p_new.size(); k ++) {
	        		p_new.set(k, -10.0);
	        	}
	        }
		    if(checkFalse(p_intersect)){
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
	public boolean checkFalse(ArrayList <Double> list) {
		
		for(int k = 0; k < list.size(); k ++) {
        	if(list.get(k) != -10.0) {
        		return false;
        	}
        }
		return true;
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
	public Object(int xPos, int yPos) {
		setSize(width, height);
		x = xPos;
		y = yPos;

		x1 = x - size/2.0;
        x2 = x + size/2.0;
        x3 = x - size/2.0;   
        x4 = x + size/2.0;

        y1 = y - size/2.0;
        y2 = y + size/2.0;
        y3 = y - size/2.0;
        y4 = y + size/2.0;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(c);
        
		g.fillRect(x + (int)(size/2.0), y + (int)(size/2.0), size, size);
	}
	public void update() {
		//x += Math.sin(Math.toRadians(facing))*speed;
		//y += Math.cos(Math.toRadians(facing))*speed;
	}
	public double[] getPos() {
		System.out.println(x1 + " - " + y1 + " --------- " + x2 + " - " + y2);
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
    		System.out.println(obs[i][0] + " - " + obs[i][3]);
    	}
    	
    	while(true) {
    		//Repaints GUI, triggering refresh of all jComp's
    		envi.getContentPane().validate();
    		envi.getContentPane().repaint();
    		
    		// Calls agents update function
    		
    		a.update(System.currentTimeMillis());
    		m.update();
        	a.getProx(0,obs, (int)a.angle);
        	stop(a);
        	
    		/*if(counter < 20000) {
    			forward(a);
    		}
    		else if(counter < 40000) {
    			rightTurn(a);
    		} else if(counter < 60000) {
    			forward(a);
    		} else if(counter < 80000) {
        		leftTurn(a);
    		} else {
    			counter = 0;
    		}
    		counter ++;
    		
    		System.out.println(counter);
    		
    		try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}*/
    	}
    }
    public static void rightTurn(Agent a) {
    	a.rightSpeed = -60;
		a.leftSpeed = 60;
    }
    public static void leftTurn(Agent a) {
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