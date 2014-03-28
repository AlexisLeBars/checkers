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

		if(case1.isFinale()){
			Coup coup = ((Damier) this.plateau).getCoupEnCours();
			coup.setPositionFinale(case1.getPosition());
			if( ((Damier) this.plateau).isCoupValide(coup) ){
				((Damier) this.plateau).executionCoup(coup);
				((Damier) this.plateau).setCoupEnCours(null);
			}
			else{
				((Damier) this.plateau).setCoupEnCours(null);
				((Damier) this.plateau).reinitEtatCases();
				((Damier) this.plateau).reinitEtatPieces(((Damier) this.plateau).getTrait());
			}
		}
	}
}
