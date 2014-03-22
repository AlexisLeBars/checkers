package up5.ia.checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ListenerCase extends MouseAdapter{
	
	private Case case1;
	private Plateau plateau;

	public ListenerCase(Case case1, Plateau plateau) {
		this.case1 = case1;
		this.plateau = plateau;
	}

	/**
	 * DOIT FAIRE BCP DE CHOSES A DERTERMINER
	 */
	public void mousePressed(MouseEvent arg0) {
		if(case1.isIntermediaire()){
			// TO DO
		}
		if(case1.isFinale()){
			// TO DO
		}
	}
}
