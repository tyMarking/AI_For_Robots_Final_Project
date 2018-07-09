package NEAT;

public class TestForwardProp {
	Genome testGenome = new Genome();
	
	public static void main(String[] args)
	{
		int i;
		TestForwardProp test = new TestForwardProp();
		test.testGenome.setInputCount(3);
		test.testGenome.setOutputCount(1);
		
		for(i=0;i<test.testGenome.getInputCount()+test.testGenome.getOutputCount();i++)
		{
			test.testGenome.addNodeGene(new NodeGene());
		}
		
		for(i=0;i<5;i++)
		{
			test.testGenome.addNodeGene(new NodeGene());
		}
		
		
		test.addConnection(5, 1, 0.1, true, test);
		test.addConnection(1, 5, 0.1, true, test);
		test.addConnection(6, 1, 0.2, true, test);
		test.addConnection(4, 1, 0.3, true, test);
		test.addConnection(5, 1, 0.1, true, test);
		test.addConnection(6, 2, 0.1, true, test);
		test.addConnection(8, 2, 0.1, true, test);
		test.addConnection(7, 2, 0.1, true, test);
		test.addConnection(8, 3, 0.1, true, test);
		test.addConnection(8, 4, 0.1, true, test);
		test.addConnection(6, 5, 0.1, true, test);
		test.addConnection(4, 5, 0.1, true, test);
		test.addConnection(7, 6, 0.1, true, test);
		test.addConnection(9, 6, 0.1, true, test);
		test.addConnection(8, 7, 0.1, true, test);
		test.addConnection(4, 7, 0.1, true, test);
		test.addConnection(9, 8, 0.1, true, test);
		test.addConnection(4, 8, 0.1, true, test);
		test.addConnection(6, 8, 0.1, true, test);
		test.addConnection(5, 8, 0.1, true, test);
		test.addConnection(7, 9, 0.1, true, test);
		test.addConnection(4, 9, 0.1, true, test);
		
		
		
		
		/*
		for(i=1;i<4;i++)
		{
			ConnectionGene cg = new ConnectionGene(4, i, 0.5, true, ConnectionGene.incrementGlobalInnovation());
			test.testGenome.addConnectionGene(cg);
		}

		
		//Network network = new Network();
		//network.constructNetwork(test.testGenome);
		//network.ForwardProp(1.0,2.0,3.0);
		
		
		//System.out.println(network.getFinalOutputListElement(0));
		
		
		
		Mutation mutation = new Mutation();
		
		mutation.addNode(test.testGenome);
		
		ConnectionGene geneC = new ConnectionGene(5,5,0.12,true,ConnectionGene.incrementGlobalInnovation());
		test.testGenome.addConnectionGene(geneC);
		
		mutation.addConnection(test.testGenome);
		
		System.out.println("This is network1 \n\n\n");
		
		for(i=0;i<4;i++)
		{
			mutation.addNode(test.testGenome);
			mutation.addConnection(test.testGenome);
			mutation.addConnection(test.testGenome);
		}
		*/
		Network network1 = new Network();
		network1.constructNetwork(test.testGenome);


		network1.ForwardProp(1.0,2.0,3.0);
		
		//network.ForwardProp(1.0,2.0,3.0);

		//System.out.println(network.getFinalOutputListElement(0));
		
		
		//System.out.println(network1.getFinalOutputListElement(0));
		
	
	}
	
	public void addConnection(int in, int out, double weight, boolean flag, TestForwardProp test)
	{
		ConnectionGene cg = new ConnectionGene(in, out, weight, flag, ConnectionGene.incrementGlobalInnovation());
		test.testGenome.checkConnectionOverlap(cg);
	}
	
}