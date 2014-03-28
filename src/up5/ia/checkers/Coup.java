package up5.ia.checkers;

import java.util.ArrayList;

public class Coup implements Cloneable{

	private int positionPieceDeplacee;
	private ArrayList<Integer> positionsPiecesSupprimees;
	private int positionCaseFinale;

	Coup(final int positionPieceDeplacee){
		this.positionPieceDeplacee=positionPieceDeplacee;
		this.positionsPiecesSupprimees=new ArrayList<Integer>();
		this.positionCaseFinale=0;
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
	
	public void setPositionCaseFinale(final Integer positionCaseFinale){
		this.positionCaseFinale = positionCaseFinale;
	}
	
	public ArrayList<Integer> getPositionsPiecesSupprimees(){
		return this.positionsPiecesSupprimees;
	}
	
	public int getPositionCaseFinale(){
		return this.positionCaseFinale;
	}

	public int getPositionPieceDeplacee(){
		return this.positionPieceDeplacee;
	}

	public void addPositionPieceSupprimee(final int positionPiece) {
		positionsPiecesSupprimees.add(positionPiece);
	}
}
