package NEAT;

import java.util.ArrayList;

public class Species {
	private ArrayList<Organism> species = new ArrayList<Organism>();
	
	public int getSpeciesSize()
	{
		return species.size();
	}
	public Organism getSpeciesElement(int index)
	{
		return species.get(index);
	}
	public void setSpeciesElement(int index, Organism replace)
	{
		species.set(index, replace);
	}
	
	public void addSpecies(Organism organism)
	{
		species.add(organism);
	}
	public void removeSpecies(int index)
	{
		species.remove(index);
	}
}
