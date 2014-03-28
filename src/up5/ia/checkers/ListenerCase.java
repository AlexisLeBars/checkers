package up5.ia.checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ListenerCase extends MouseAdapter{
	
	private Case case1;
	private Plateau plateau;

	public ListenerCase(final Case case1, final Plateau plateau) {
		this.case1 = case1;
		this.plateau = plateau;
	}

	public void mousePressed(MouseEvent arg0) {
	
		if( case1.isFinale() ){
			Coup coupValide =  ((Damier) this.plateau).getCoupValide( ((Damier) this.plateau).getPositionPieceActive(), case1.getPosition() );
			((Damier) this.plateau).executionCoup(coupValide);
		}
		else{
			((Damier) this.plateau).reinitEtatCases();
			((Damier) this.plateau).reinitEtatPieces(((Damier) this.plateau).getTrait());
		}
		((Damier) this.plateau).setPositionPieceActive(0);
	}
}
