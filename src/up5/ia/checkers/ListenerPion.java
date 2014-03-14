package up5.ia.checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ListenerPion extends MouseAdapter {
	
	private Plateau plateau;
	private Pion pion;
	
	public ListenerPion(Pion pion, Plateau plateau){
		this.plateau=plateau;
		this.pion=pion;
	}

	public void mousePressed(MouseEvent arg0) {
		plateau.afficherPossibilites(pion);
	}

}
