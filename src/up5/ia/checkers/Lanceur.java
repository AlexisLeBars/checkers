package up5.ia.checkers;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Lanceur {
	
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e){}
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.setTitle("Jeu De Dames");
		f.setMinimumSize(new Dimension(600,600));
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Damier damier = new Damier();
		
		damier.calculerCoupsPossibles(Couleur.BLANC);
		f.add(damier,BorderLayout.CENTER);
		f.setVisible(true);
		f.pack();
	}
}
