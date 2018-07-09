package NEATImplementation;


import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import NEAT.Genome;
import NEAT.NEAT_Toolchain;
import NEAT.Organism;
import NEAT.Population;
import NEATImplementation.Cheese;
import NEATImplementation.Fire;
import NEATImplementation.Sprite;

public class Board extends JComponent implements ActionListener, KeyListener{

    NEAT_Toolchain nTool = new NEAT_Toolchain();

    int population = 100;

    boolean toggle = false;

    int x = 500;
    int y = 400;
    int timeSpeed = 1;

    long tStart;
    long tEnd;
    long tp;
    long eatC = 0;
    long eatF = 0;
    Cheese[] cheese = new Cheese[60];
    Fire[] fire = new Fire[30];
    Sprite[] sprite = new Sprite[0];
    int fitness;
    File file = new File("NEAT.txt");
    File file1 = new File("NEATProgress.txt");

    ArrayList<Long> cheeses = new ArrayList<Long>();
    ArrayList<Long> fires = new ArrayList<Long>();
    ArrayList<Double> fitnesses = new ArrayList<Double>();

    int dimensionH = 1280;
    int dimensionV = 800;

    double oldOut = 0;
    double foodTime = 0.1;
    double generationT = 0.5;
    FileWriter writer;
    FileWriter writer1;

    private final Timer timer = new Timer(1000, this);

    static Board bord;

    int i;
    int j;
    int k;
    //MainEval main;

    Random rand = new Random();
    public Board(){
        //main = new MainEval();
        System.out.println("Mark1");
        sprite = new Sprite[nTool.getTotalPopulation()];
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        for(i=0;i<cheese.length;i++)
        {
            cheese[i] = new Cheese();
            PosCheese(i);
        }
        for(i=0;i<fire.length;i++)
        {
            fire[i] = new Fire();
            PosFire(i);
        }
        System.out.println("Mark2");
        nTool.generateInitialPopulation(3,2, population);
        System.out.println("Mark3");
        sprite = new Sprite[population];
        System.out.println("Generating Initial Population");
        int count = 0;
        for(i=0;i<Population.getPopulationSize();i++)
        {
            for(j=0;j<Population.getPopulationElement(i).getSpeciesSize();j++)
            {
                sprite[count] = new Sprite();
                sprite[count].sprite = Population.getPopulationElement(i).getSpeciesElement(j);
                PosSprite(count);
                count++;
            }
        }
        System.out.println("Initial Population Success");

        try {
            file.createNewFile();
            writer = new FileWriter(file);
            file1.createNewFile();
            writer1 = new FileWriter(file1);
            // creates a FileWriter Object

            // Writes the content to the file
            //Train();

        }catch(IOException e){

        }



        foodTime = 0.1;
        //generationT = 1;
        tStart = System.currentTimeMillis();
        tp = System.currentTimeMillis();
        Timer t = new Timer(1, this);
        t.setDelay(timeSpeed);
        t.start();


    }
    public static void main(String[] args)
    {
        System.out.println("Initializing Frame");
        JFrame window = new JFrame();
        System.out.println("Frame Complete");
        bord = new Board();
        window.add(bord);
        window.pack();
        //window.setPreferredSize(new Dimension(bord.dimensionH, bord.dimensionV));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try {
                    bord.writer.flush();
                    bord.writer.close();
                    bord.writer1.flush();
                    bord.writer1.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });


    }
    public void PosCheese(int i)
    {
        cheese[i].setX(rand.nextInt(dimensionH));
        cheese[i].setY(rand.nextInt(dimensionV));
    }
    public void PosFire(int i)
    {
        fire[i].setX(rand.nextInt(dimensionH));
        fire[i].setY(rand.nextInt(dimensionV));
    }
    public void PosSprite(int position)
    {
        sprite[position].posX = rand.nextInt(dimensionH);
        sprite[position].posY = rand.nextInt(dimensionV);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(dimensionH, dimensionV);
    }
    @Override

    protected void paintComponent(Graphics g)
    {

        for(i=0;i<cheese.length;i++)
        {
            g.setColor(Color.ORANGE);
            g.fillRect(cheese[i].x, cheese[i].y, 8, 8);

        }
        for(i=0;i<fire.length;i++)
        {
            g.setColor(Color.RED);
            g.fillRect(fire[i].x, fire[i].y, 8, 8);
        }
        for(i=0;i<sprite.length;i++)
        {
            if(i<=2)
            {
                g.setColor(Color.MAGENTA);
                g.fillOval((int)Math.round(sprite[i].posX), (int)Math.round(sprite[i].posY), 10, 15);
            }else{
                g.setColor(Color.BLACK);
                g.fillOval((int)Math.round(sprite[i].posX), (int)Math.round(sprite[i].posY), 10, 15);
            }
        }
        g.setColor(Color.BLACK);
        if(toggle)
        {
            g.drawString("Observation MODE: ", 50, 60);
        }else {
            g.drawString("Generation: " + nTool.getGeneration(), 50, 60);
        }
    }


    public Sprite format(Sprite sprite, Organism organism)
    {
        sprite = new Sprite();
        sprite.sprite = organism;
        return sprite;
    }

    public double normalize(double val, double min, double max)
    {
        return (val-min) / (max-min);
    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_C)
        {
            toggle = true;
        }
        if(e.getKeyCode()==KeyEvent.VK_V)
        {
            toggle = false;
        }
    }

    public void keyReleased(KeyEvent e)
    {

    }

    public void keyTyped(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_C)
        {
            System.out.println("Toggle");
            toggle = true;
        }
        if(e.getKeyCode()==KeyEvent.VK_V)
        {
            System.out.println("Toggle1");
            toggle = false;
        }
    }

    public void func()
    {


        int iteration = 10;
        if(toggle)
        {
            //generationT += 10;
            foodTime = 0.1;
            iteration = 1;
        }else {
            iteration = 10;
            generationT = 10;
            foodTime = 0.001;
        }
        if(nTool.getGeneration() > 500)
        {
            //iteration = 1;
            //generationT = 10;
            //foodTime = 0.1;

            try {
                writer.write("\n\nCheeses through Generation:\n");
                for(Long val : cheeses)
                {
                    writer.write(String.valueOf(val)+"\n");
                }
                writer.write("\n\nFires through Generation:\n");
                for(Long val : fires)
                {
                    writer.write(String.valueOf(val)+"\n");
                }

                writer.write("\n\nFitnesses through Generation:\n");
                for(Double val : fitnesses)
                {
                    writer.write(String.valueOf(val)+"\n");
                }

                writer.flush();
                writer.close();
                writer1.flush();
                writer1.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            System.exit(1);
        }

        for(int count = 0; count < iteration; count++) {
            if(!toggle) {
                tEnd = System.currentTimeMillis();
            }
            for (i = 0; i < sprite.length; i++) {
                double max = 100000;
                int closestCheese = 0;
                int closestFire = 0;
                double distance;
                for (j = 0; j < cheese.length; j++) {

                    distance = Math.sqrt(Math.pow(cheese[j].x - sprite[i].posX, 2)+Math.pow(cheese[j].y - sprite[i].posY,2));

                    if (distance < max) {
                        max = distance;
                        closestCheese = j;
                    }
                }

                double maxf = 100000;
                for (j = 0; j < fire.length; j++) {
                    distance = Math.sqrt(Math.pow(fire[j].x - sprite[i].posX, 2)+Math.pow(fire[j].y - sprite[i].posY,2));
                    if (distance < maxf) {
                        maxf = distance;
                        closestFire = j;
                    }
                }

                double distanceCheese = Math.sqrt(Math.pow(cheese[closestCheese].x - sprite[i].posX, 2)+Math.pow(cheese[closestCheese].y - sprite[i].posY,2));
                double distanceFire = Math.sqrt(Math.pow(fire[closestFire].x - sprite[i].posX, 2)+Math.pow(fire[closestFire].y - sprite[i].posY,2));
                double smellSense = 0;
                if(distanceFire <= distanceCheese) {
                    smellSense -= 1-normalize(Math.abs(distanceCheese - distanceFire), 0, distanceCheese);
                    double angle = (Math.toDegrees(Math.atan2(fire[closestFire].x - sprite[i].posX, fire[closestFire].y - sprite[i].posY)));
                    if(angle<0)
                    {
                        angle = normalize(angle,-360, 0);
                    }else{
                        angle = normalize(angle,0, 360);
                    }
                    sprite[i].sprite.getNetwork().ForwardProp(smellSense, angle ,1);
                }else{
                    smellSense += 1-normalize(Math.abs(distanceCheese - distanceFire), 0, distanceFire);
                    double angle = (Math.toDegrees(Math.atan2(cheese[closestCheese].x - sprite[i].posX, cheese[closestCheese].y - sprite[i].posY)));
                    if(angle<0)
                    {
                        angle = normalize(angle,-360, 0);
                    }else{
                        angle = normalize(angle,0, 360);
                    }
                    sprite[i].sprite.getNetwork().ForwardProp(smellSense, angle ,-1);
                }


			/*if(maxf < max)
			{
				sprite[i].back.input[0][0] = maxf/100;
				sprite[i].back.input[0][1] = Math.toDegrees(Math.atan2(fire[closestm].x - sprite[i].posX, fire[closestm].y - sprite[i].posY))/100;
			}
			sprite[i].back.input[0][2] = 1;
			//sprite[i].back.input[0][4] = 0;
			sprite[i].back.Start();*/
            }
            for (i = 0; i < sprite.length; i++) {
                //System.out.println(sprite[i].sprite.getNetwork().getFinalOutputList().length);
                double rotforce = sprite[i].sprite.getNetwork().getFinalOutputListElement(0) - sprite[i].sprite.getNetwork().getFinalOutputListElement(1);
                double speed = sprite[i].sprite.getNetwork().getFinalOutputListElement(0) + sprite[i].sprite.getNetwork().getFinalOutputListElement(1);
                //sprite[i].fitness /= speed;
                oldOut = (sprite[i].sprite.getNetwork().getFinalOutputListElement(0) + sprite[i].sprite.getNetwork().getFinalOutputListElement(1)) / 2;
			/*if(speed > 50)
			{
				speed = 30;
			}*/
                rotforce = Math.max(-180, Math.min(180, rotforce));
                sprite[i].rot += rotforce;

                double lookx = -Math.sin(sprite[i].rot);
                double looky = Math.cos(sprite[i].rot);
                //I'm switching sin and cos into the lookx and y
                sprite[i].posX += lookx * speed;
                sprite[i].posY += looky * speed;
                if (sprite[i].posX >= dimensionH) {
                    sprite[i].posX = 2;
                }
                if (sprite[i].posX <= 1) {
                    sprite[i].posX = dimensionH - 2;
                }
                if (sprite[i].posY >= dimensionV) {
                    sprite[i].posY = 2;
                }
                if (sprite[i].posY <= 1) {
                    sprite[i].posY = dimensionV - 2;
                }
            }

            if(!toggle) {
                if ((tEnd - tp) / 1000 > foodTime) {
                    for (i = 0; i < sprite.length; i++) {
                        sprite[i].sprite.fitness -= 3;
                    }
                    tp = System.currentTimeMillis();
                }
            }

                for (i = 0; i < sprite.length; i++) {

                    for (j = 0; j < cheese.length; j++) {
                        double imsi = Math.sqrt(((cheese[j].x - sprite[i].posX) * (cheese[j].x - sprite[i].posX)) + ((cheese[j].y - sprite[i].posY) * (cheese[j].y - sprite[i].posY)));
                        if (imsi <= 10) {

                            PosCheese(j);
                            if(!toggle) {
                                ++eatC;
                                sprite[i].sprite.fitness += 60;
                            }
                        }
                    }

                    for (j = 0; j < fire.length; j++) {
                        double imsi = Math.sqrt(((fire[j].x - sprite[i].posX) * (fire[j].x - sprite[i].posX)) + ((fire[j].y - sprite[i].posY) * (fire[j].y - sprite[i].posY)));
                        if (imsi <= 10) {
                            PosFire(j);
                            if(!toggle) {
                                ++eatF;
                                sprite[i].sprite.fitness = -80;
                            }
                        }
                    }
			/*if(sprite[i].fitness <=0)
			{
				sprite[i] = format(sprite[i]);
				PosSprite(i);
			}*/
                }

        }
        //main.Selection(sprite);
        if((tEnd - tStart)/1000 > generationT)
        {
			/*Sprite[] spr = new Sprite[(int)(sprite.length*0.9)];
			for(i=0;i<(int)sprite.length*0.9;i++)
			{
				spr[i] = sprite[i];
			}
			sprite = spr;*/
            for(i=0;i<sprite.length;i++)
            {
                if(sprite[i].sprite.fitness < 0)
                {
                    sprite[i].sprite.fitness = 0;
                }
            }
            nTool.EvaluateGeneration();

            int count = 0;
            for(i=0;i<Population.getPopulationSize();i++)
            {
                for(j=0;j<Population.getPopulationElement(i).getSpeciesSize();j++)
                {
                    sprite[count] = new Sprite();
                    sprite[count].sprite = Population.getPopulationElement(i).getSpeciesElement(j);
                    PosSprite(count);
                    count++;
                }
            }

            for(i=0;i<sprite.length;i++)
            {
                sprite[i].fitness = 100;
                PosSprite(i);
            }
            nTool.incrementGeneration();
			/*if(cheese.length-main.generation > 1)
			{
				cheese = new Cheese[cheese.length-main.generation];
			}else{
				cheese = new Cheese[1];
			}*/
            for(i=0;i<cheese.length;i++)
            {
                cheese[i] = new Cheese();
                PosCheese(i);
            }
            tStart = System.currentTimeMillis();
            //fitness=(int)main.fitSum;
            //sprite = main.ranking(sprite);
            try {
                writer.write("Generation "+nTool.getGeneration()+":\n");
                writer.write("Cheese eaten: "+eatC+"\n");
                writer.write("Fire eaten: "+eatF+"\n\n");



            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("Cannot Print!");
                e1.printStackTrace();
            }
            for(i=0;i<sprite.length;i++)
            {
                sprite[i].sprite.fitness = 0;
            }

            System.out.println("Generation "+nTool.getGeneration()+":\n");
            System.out.println("Cheese: "+eatC);
            System.out.println("Fire: "+eatF);
            System.out.println("Fitness: "+Population.getTotalFitness()+"\n\n\n");
            cheeses.add(eatC);
            fires.add(eatF);
            fitnesses.add(Population.getTotalFitness());

            eatC = 0;
            eatF = 0;
        }


		/*if(sprite[0].fitness > 1+main.generation && sprite[1].fitness > 1+main.generation)
		{
			sprite = main.Selection(sprite);
			for(i=0;i<sprite.length;i++)
			{
				PopsSprite(i);
			}
			main.generation++;
		}*/

        repaint();

    }
    public void actionPerformed(ActionEvent e)
    {
        func();
    }

}