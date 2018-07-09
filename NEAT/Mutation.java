package NEAT;

import java.util.ArrayList;

public class Mutation {
	
	private int i;
	private int high;
	private int low;
	
	public void addNode(Genome genome) //Data types(except for primitive data types) always pass by reference, thus original genome is altered even without the return statement. 
	{
		//double random = (high-low) * Math.random() + low low<=x<high
		
		high = genome.getConnectionGenesSize()-1;
		low = 0;
		int connectionIndex = randomInteger(high, low);  //(+1) since x<high, want to make it x<=high
		System.out.println("Connection Index: "+connectionIndex);
		
		int nodeID_in = genome.getConnectionGeneElement(connectionIndex).getIn_ID();
		int nodeID_out = genome.getConnectionGeneElement(connectionIndex).getOut_ID();
		
		//genome.getNodeGeneElement(nodeID_in).incrementLayer();
		//pushLayers(nodeID_in, genome); //in is receiver, and searches for connection where in is the sender
		genome.getConnectionGeneElement(connectionIndex).setActive(false);
		
		NodeGene node = new NodeGene();
		//node.setID(genome.getNodeGeneElement(genome.getNodeGeneSize()-1).getID()+1); //(+1) for consecutive nodeID
		//node.setLayer(genome.getNodeGeneElement(nodeID_out).getLayer()+1); //Receiver is next layer to that of sending node
		
		genome.addNodeGene(node);
		
		System.out.println("Added Node \n\n");
		
		ConnectionGene gene1 = new ConnectionGene(node.getID(), nodeID_out, 1.0d, true, 0);
		checkInnovationOverlaps(genome,gene1);
		ConnectionGene gene2 = new ConnectionGene(nodeID_in, node.getID(), randomDouble(1.0, -1.0), true, 0);
		checkInnovationOverlaps(genome,gene1);
		
		genome.addConnectionGene(gene1);
		System.out.println("Added Connection\n\nIN: "+gene1.getIn_ID()+"\nOUT: "+gene1.getOut_ID()+"\nWEIGHT: "+gene1.getWeight());
		genome.addConnectionGene(gene2);
		System.out.println("Added Connection\n\nIN: "+gene2.getIn_ID()+"\nOUT: "+gene2.getOut_ID()+"\nWEIGHT: "+gene2.getWeight());
		
	}
	
	public void addConnection(Genome genome)
	{
		ArrayList<Integer> availableElement = new ArrayList<Integer>();
		
		
		high = genome.getNodeGeneSize()-1;
		low = 1;
		int sender_ID = randomInteger(high, low);
		
		boolean[] overlapConnection = new boolean[genome.getNodeGeneSize()];
		
		for(i=0;i<genome.getConnectionGenesSize();i++)
		{
			if((sender_ID == genome.getConnectionGeneElement(i).getOut_ID()))
			{
				overlapConnection[genome.getConnectionGeneElement(i).getIn_ID()-1] = true;
			}
		}
		for(i=0;i<overlapConnection.length;i++)
		{
			if(!overlapConnection[i])
			{
				availableElement.add(i+1);
			}
		}
		
		high = availableElement.size()-1;
		low = 0;
		
		if(high > 0)
		{
			ConnectionGene gene = new ConnectionGene(availableElement.get(randomInteger(high, low)), sender_ID, randomDouble(1.0, -1.0), true, 0);
			checkInnovationOverlaps(genome, gene);
			genome.addConnectionGene(gene);
			System.out.println("Added Connection\n\nIN: "+gene.getIn_ID()+"\nOUT: "+gene.getOut_ID()+"\nWEIGHT: "+gene.getWeight());
			
		}else {
			System.out.println("No Available Connection... Adding Node");
			addNode(genome);
		}
	}
	
	public static void checkInnovationOverlaps(Genome genome, ConnectionGene gene_connection)
	{
		int i; //In case this function runs within another for loop
		
		for(i=0;i<Genome.innovationList.size();i++)
		{
			if(gene_connection != null)
			{
				if(Genome.innovationList.get(i).getIn_ID() == gene_connection.getIn_ID() && Genome.innovationList.get(i).getOut_ID() == gene_connection.getOut_ID())
				{
					gene_connection.setInnovation(Genome.innovationList.get(i).getInnovation());
					return;
				}
			}else {
				System.out.println("Error! ConnectionGene is null!");
				return;
			}
		}
		
		gene_connection.setInnovation(ConnectionGene.incrementGlobalInnovation());
		Genome.innovationList.add(gene_connection);
		return;
	}
	
	public void pushLayers(int sender_ID, Genome genome)
	{
		for(i=0;i<genome.getConnectionGenesSize();i++)
		{
			if(genome.getConnectionGeneElement(i).getOut_ID()==sender_ID)
			{
				genome.getNodeGeneElement(genome.getConnectionGeneElement(i).getIn_ID()).incrementLayer(); //Assuming Node ID is set in consecutive chronological order
				sender_ID = genome.getConnectionGeneElement(i).getIn_ID();
			}
		}
	}
	
	public int randomInteger(int high, int low)
	{
		return (int)(Math.random()*((high+1)-low))+low;  //(+1) since x<high, want to make it x<=high
	}
	
	public double randomDouble(double high, double low)
	{
		return ((high)-low) * Math.random() + low; //x<high
	}
}
