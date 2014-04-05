package up5.ia.checkers;

import java.util.ArrayList;

public class Noeud {

	@SuppressWarnings("unused")
	private Noeud parent;
	private ArrayList<Noeud> fils;
	private int[][] etat;
	private int profondeur;
	private Coup coup;
	private int gain;
	
	Noeud(Noeud parent,int profondeur,int[][] etat){
		this.parent = parent;
		this.etat = etat;
		this.profondeur = profondeur;
		this.fils = new ArrayList<Noeud>();
		this.coup = null;
		this.gain=0;
	}

	public void addFils(Noeud fils){
		this.fils.add(fils);
	}
	
	public void setCoup(Coup coup){
		this.coup =  coup;
	}
	
	public Coup getCoup(){
		return this.coup;
	}
	
	public ArrayList<Noeud> getFils(){
		return this.fils;
	}
	
	public int setGain(int gain){
		return this.gain = gain;
	}
	
	public int getGain(){
		return this.gain;
	}
	
	public int[][] getEtat(){
		return this.etat;
	}
	
	public int getProfondeur(){
		return this.profondeur;
	}
}