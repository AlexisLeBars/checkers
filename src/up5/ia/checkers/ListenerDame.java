package up5.ia.checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListenerDame extends MouseAdapter{

	private Damier damier;
	private Dame dame;
	
	public ListenerDame(Dame dame, Damier damier){
		this.damier=damier;
		this.dame=dame;
	}

	/**
	 * DOIT FAIRE BCP DE CHOSES A DERTERMINER
	 */
	public void mousePressed(MouseEvent arg0) {
		damier.afficherCoups(dame);
		// TO DO
	}
}
