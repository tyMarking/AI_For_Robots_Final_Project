package NEAT;

import java.util.ArrayList;

public class Genome {
	private ArrayList<NodeGene> nodeGenes = new ArrayList<NodeGene>();
	private ArrayList<ConnectionGene> connectionGenes = new ArrayList<ConnectionGene>();
	public static ArrayList<ConnectionGene> innovationList = new ArrayList<ConnectionGene>();
	private int outputCount = 0;
	private int inputCount = 0;
	
	public NodeGene getNodeGeneElement(int index)
	{
		return nodeGenes.get(index);
	}
	public ArrayList<NodeGene> getNodeGeneList()
	{
		return nodeGenes;
	}
	
	public int getOutputCount()
	{
		return outputCount;
	}
	public void setOutputCount(int value)
	{
		outputCount = value;
	}
	
	public int getInputCount()
	{
		return inputCount;
	}
	public void setInputCount(int value)
	{
		inputCount = value;
	}
	
	public ConnectionGene getConnectionGeneElement(int index)
	{
		return connectionGenes.get(index);
	}
	
	public int getNodeGeneSize()
	{
		return nodeGenes.size();
	}
	public int getConnectionGenesSize()
	{
		return connectionGenes.size();
	}
	
	public void addNodeGene(NodeGene gene)
	{
		if(getNodeGeneSize() > 0)
		{
			gene.setID(nodeGenes.get(getNodeGeneSize()-1).getID()+1);
		}else {
			gene.setID(1);
		}
		nodeGenes.add(gene);
	}
	public void checkConnectionOverlap(ConnectionGene gene)
	{
		int i;
		for(i=0;i<this.getConnectionGenesSize();i++)
		{
			if(this.getConnectionGeneElement(i).getIn_ID() == gene.getIn_ID() && this.getConnectionGeneElement(i).getOut_ID() == gene.getOut_ID())
			{
				if(!this.getConnectionGeneElement(i).ifActive())
				{
					this.addConnectionGene(gene);
					return;
				}else {
					return;
				}
			}
		}
		this.addConnectionGene(gene);
	}
	
	public void addConnectionGene(ConnectionGene gene)
	{
		connectionGenes.add(gene);
		innovationList.add(gene);
	}
	
	public void removeNodeGene(int index)
	{
		if(index < nodeGenes.size() && index >= 0)
		{
			nodeGenes.remove(index);
		}else {
			System.out.println("Error! Invalid index of removeNodeGene");
		}
	}
	public void removeConnectionGene(int index)
	{
		if(index < connectionGenes.size() && index >= 0)
		{
			connectionGenes.remove(index);
		}else {
			System.out.println("Error! Invalid index of removeConnectionGene");
		}
	}
}
