package up5.ia.checkers;

import java.util.ArrayList;

public class Coup implements Cloneable{

	private int positionInitiale;
	private ArrayList<Integer> positionsPiecesSupprimees;
	private int positionFinale;

	Coup(final int positionInitiale){
		this.positionInitiale=positionInitiale;
		this.positionsPiecesSupprimees=new ArrayList<Integer>();
		this.positionFinale=0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Coup clone() throws CloneNotSupportedException {
		Coup copie = (Coup) super.clone();
		copie.setPositionsPiecesSupprimees( (ArrayList<Integer>) this.positionsPiecesSupprimees.clone());
		return copie;
	}
	
	public void setPositionsPiecesSupprimees(final ArrayList<Integer> positionsPiecesSupprimees){
		this.positionsPiecesSupprimees = positionsPiecesSupprimees;
	}
	
	public void setPositionFinale(final Integer positionFinale){
		this.positionFinale = positionFinale;
	}
	
	public ArrayList<Integer> getPositionsPiecesSupprimees(){
		return this.positionsPiecesSupprimees;
	}
	
	public int getPositionFinale(){
		return this.positionFinale;
	}

	public int getPositionInitiale(){
		return this.positionInitiale;
	}

	public void addPositionPieceSupprimee(final int position) {
		positionsPiecesSupprimees.add(position);
	}
}
