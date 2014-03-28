package up5.ia.checkers;

import java.awt.GridLayout;

import javax.swing.JPanel;


public class Plateau extends JPanel {

	private static final long serialVersionUID = 6726708245444190460L;

	public Plateau(final int taille){
		setLayout(new GridLayout(taille, taille));

		int position = 0;
		for(int ligne=0; ligne<taille; ligne++){
			for(int colonne=0; colonne<taille; colonne++){
				if((colonne%2==0 && ligne%2==0) || (colonne%2!=0 && ligne%2!=0))
					ajouterCase(Couleur.BLANC, 0);
				else
					ajouterCase(Couleur.NOIR, ++position);
			}
		}
	}

	private void ajouterCase(final Couleur couleur, final int position){
		Case case1 = new Case(couleur,position);
		case1.addMouseListener(new ListenerCase(case1, this));
		add(case1);
	}

}
