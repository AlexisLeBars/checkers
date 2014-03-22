package up5.ia.checkers;

import java.awt.GridLayout;

import javax.swing.JPanel;


public class Plateau extends JPanel {

	private static final long serialVersionUID = 6726708245444190460L;

	public Plateau(int taille){
		setLayout(new GridLayout(taille, taille));

		for(int i=0; i<taille; i++){
			for(int j=0; j<taille; j++){
				if((j%2==0 && i%2==0) || (j%2!=0 && i%2!=0)){
					ajouterCase(Couleur.BLANC);
				}
				else{
					ajouterCase(Couleur.NOIR);
				}
			}
		}
	}

	private void ajouterCase(Couleur couleur){
		Case case1 = new Case(couleur);
		case1.addMouseListener(new ListenerCase(case1, this));
		add(case1);
	}

}
