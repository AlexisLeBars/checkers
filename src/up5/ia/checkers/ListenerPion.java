package up5.ia.checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ListenerPion extends MouseAdapter {
	
	private Damier damier;
	private Pion pion;
	
	public ListenerPion(Pion pion, Damier damier){
		this.damier=damier;
		this.pion=pion;
	}

	/**
	 * DOIT FAIRE BCP DE CHOSES A DERTERMINER
	 */
	public void mousePressed(MouseEvent arg0) {
		damier.afficherCoups(pion);
		// TO DO
	}

}
