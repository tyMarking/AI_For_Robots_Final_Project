package NEAT;

import java.util.ArrayList;

public class Network {

	private ArrayList<Node> nodes = new ArrayList<Node>();
	private int i;
	private int outputCount = 0;
	private int inputCount = 0;
	private double[] final_output_list = null;

	public void addNode(int ID, int Layer)
	{
		Node node = new Node();
		node.ID = ID; //ID is global
		//node.Layer = Layer; //Indicator for forward and back-propagation
		nodes.add(node);
	}

	public Network constructNetwork(Genome genome)
	{
		int i;

		inputCount = genome.getInputCount();
		outputCount = genome.getOutputCount();

		for(i=0;i<genome.getNodeGeneSize();i++)
		{
			this.addNode(genome.getNodeGeneElement(i).getID(), genome.getNodeGeneElement(i).getLayer());
			if(i >= genome.getNodeGeneSize() - genome.getOutputCount())
			{
				nodes.get(i).ifOutput = true;
			}
		}
		for(i=0;i<genome.getConnectionGenesSize();i++)
		{
			if(genome.getConnectionGeneElement(i).ifActive())
			{
				this.addConnection(genome.getConnectionGeneElement(i).getOut_ID(), genome.getConnectionGeneElement(i).getIn_ID(), genome.getConnectionGeneElement(i).getWeight());
			}
		}

		for(i=0;i<inputCount;i++)
		{
			if(i<inputCount)
			{
				this.getNodeElement(i).ifInput = true;
			}
		}

		return this;
	}

	public int getInputCount()
	{
		return inputCount;
	}
	public void setInputCount(int value) 
	{
		inputCount = value;
	}

	public double[] getFinalOutputList()
	{
		return final_output_list;
	}
	public void formatFinalOutputList(double[] array)
	{
		final_output_list = array;
	}
	public void setFinalOutputListElement(int index, double value)
	{
		final_output_list[index] = value;
	}
	public double getFinalOutputListElement(int index)
	{
		return final_output_list[index];
	}

	public int getNodeSize()
	{
		return nodes.size();
	}
	public Node getNodeElement(int index)
	{
		return nodes.get(index);
	}

	public int getOutputCount() 
	{
		return outputCount;
	}
	public void setOutputCount(int max)
	{
		outputCount = max;

	}

	public void addConnection(int ID_sender, int ID_receiver, double weight)
	{
		ID_sender--; //Node ID starts from 1
		ID_receiver--;

		if(ID_sender != -1 && ID_receiver != -1)
		{
			nodes.get(ID_receiver).addInput(nodes.get(ID_sender));
			nodes.get(ID_sender).addOutput(nodes.get(ID_receiver));
			nodes.get(ID_sender).addWeight(weight);
		}else {
			System.out.println("Error in adding Connection\nEither specified node does not exist");
		}

	}


	public void removeConnection(int ID_sender, int ID_receiver) 
	{
		ID_sender--; //Node ID starts from 1
		ID_receiver--;

		for(i=0;i<nodes.get(ID_receiver).getInputSize();i++)
		{
			if(nodes.get(ID_receiver).getInputElement(i).ID == nodes.get(ID_sender).ID)
			{
				nodes.get(ID_receiver).removeInput(i);
				nodes.get(ID_sender).removeOutput(i);
				nodes.get(ID_receiver).removeWeight(i);
				return;
			}
		}
		System.out.println("Failed to Remove Connection...");
	}

	public void ForwardProp(double... inputs) //Assuming that nodes list is stored in consecutive order by ID, hence also the Layers
	{

		int i;
		int j;
		int k;

		if(inputs.length == inputCount)
		{
			for(i=0;i<inputs.length;i++)
			{
				nodes.get(i).setNodeOutput(inputs[i]);
			}

		}else {

			System.out.println("All inputs not defined (NULL)");
		}

		/*int i;
		double[] memoi = null;
		final_output_list = new double[outputCount];



		for(i=0;i<outputCount;i++)
		{
			memoi = nodes.get(inputCount+i).Summation(this, nodes.get(inputCount+i), memoi);
			final_output_list[i] = memoi[inputCount+i];
		}
		nodes.get(0).evaluateRecursion(this, memoi);*/

		//Order to Forward Propagate

		ArrayList<Node> qeue = new ArrayList<Node>();
		int currentSize = 0;
		
		for(i=0;i<inputCount;i++)
		{
			qeue.add(this.getNodeElement(i));
		}

		for(i=0;i<qeue.size();)
		{
			currentSize = qeue.size();


			for(j=0;j<currentSize;j++)
			{
				if(!qeue.get(j).ifInput)
				{
					qeue.get(j).activate();
				}

				if(!qeue.get(j).hasBeen())
				{
					for(k=0;k<qeue.get(j).getOutputSize();k++)
					{
						double sum = qeue.get(j).getOutputElement(k).getSum() + (qeue.get(j).getNodeOutput() * qeue.get(j).getWeightElement(k));
						qeue.get(j).getOutputElement(k).setSum(sum);
						//System.out.println("Node "+qeue.get(j).ID+" has value "+qeue.get(j).getNodeOutput()+" WEIGHT "+qeue.get(j).getWeightElement(k)+"Total output: "+(qeue.get(j).getNodeOutput() * qeue.get(j).getWeightElement(k))+" Passing to Node "+qeue.get(j).getOutputElement(k).ID);
						if(!qeue.get(j).getOutputElement(k).hasBeen())
						{
							qeue.add(qeue.get(j).getOutputElement(k));
						}
					}
				}

				qeue.get(j).sethasBeen(true);
			}

			for(j=0;j<currentSize;j++)
			{
				qeue.remove(0);
			}

		}


		for(i=0;i<this.getNodeSize();i++)
		{
			this.getNodeElement(i).sethasBeen(false);
		}
		formatFinalOutputList(output());
	}



	public double[] output() //Assuming that nodes list is stored in consecutive order by ID, hence also the Layers

	{
		double[] outputs = new double[outputCount];

		for(i=nodes.size()-outputCount;i<nodes.size();i++)
		{
			outputs[nodes.size()-i-1] = nodes.get(i).getNodeOutput(); //We are filling outputs in backward order i.e. output[3] --> output[2] --> output[1]
		}

		return outputs;
	}

	public int[] searchID(int ID_1, int ID_2)
	{
		int[] indexes = new int[2];

		for(i=0;i<nodes.size();i++)
		{
			if(indexes[0] != -1 && indexes[1] != -1)
			{
				return indexes;
			}
			else if(nodes.get(i).ID == ID_1)
			{
				indexes[0] = i;
			}
			else if(nodes.get(i).ID == ID_2)
			{
				indexes[1] = i;
			}
		}

		return indexes;
	}





}
