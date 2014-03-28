package up5.ia.checkers;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Lanceur {
	
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e){}
		JFrame f = new JFrame();
		f.setSize(600, 600);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Damier damier = new Damier();
		// Commenté car pas encore implémenté
		//damier.setCoupsPossibles(damier.calculerCoupsPossibles(Couleur.BLANC));
		
	//lignes de test
		Coup coup1 = new Coup(31);
		coup1.setPositionFinale(26);
		Coup coup2 = new Coup(31);
		coup2.setPositionFinale(27);
	
		ArrayList<Coup> coupsPossibles = new ArrayList<Coup>();
		coupsPossibles.add(coup1);
		coupsPossibles.add(coup2);
		
		damier.setCoupsPossibles(coupsPossibles);
	// lignes de test
		
		f.add(damier);
		f.setVisible(true);
	}
}
