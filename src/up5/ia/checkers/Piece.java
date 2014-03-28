package up5.ia.checkers;

import javax.swing.JPanel;

public abstract class Piece extends JPanel{

	private static final long serialVersionUID = 6726708245333190461L;
	private int position;

	protected Couleur couleur;
	protected boolean supprimee;
	
	public Piece(final int position){
		this.position=position;
		this.supprimee=false;
	}
	
	public Couleur getCouleur(){
		return couleur;
	}
	
	public int getPosition(){
		return this.position;
	}
	
	public void setPosition(int position){
		this.position = position;
	}

	public void setSupprimee(final boolean supprimee){
		this.supprimee=supprimee;
		// TO DO
		// modification de l'apparence d'une piece
	}

}
