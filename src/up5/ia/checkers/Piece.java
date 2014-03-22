package up5.ia.checkers;

import javax.swing.JPanel;

public abstract class Piece extends JPanel{

	private static final long serialVersionUID = 6726708245333190461L;
	
	protected Couleur couleur;
	
	public Couleur getCouleur() {
		return couleur;
	}
}
