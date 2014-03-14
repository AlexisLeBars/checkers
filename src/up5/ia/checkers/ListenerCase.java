package up5.ia.checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ListenerCase extends MouseAdapter{
	
	private Case case1;
	private Plateau plateau;

	
	public ListenerCase(Case case1, Plateau plateau) {
		super();
		this.case1 = case1;
		this.plateau = plateau;
	}

	public void mousePressed(MouseEvent arg0) {
		if(case1.isSelectionnee()){
			plateau.deplacer(case1);
		}
	}
}
