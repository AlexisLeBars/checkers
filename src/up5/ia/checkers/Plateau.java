package up5.ia.checkers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;


public class Plateau extends JPanel {

	private static final long serialVersionUID = 6726708245444190460L;

	public Plateau(final int taille){
		super(new GridLayout(taille, taille));

		int position = 0;
		for(int ligne=0; ligne<taille; ligne++){
			for(int colonne=0; colonne<taille; colonne++){
				if((colonne%2==0 && ligne%2==0) || (colonne%2!=0 && ligne%2!=0))
					ajouterCase(Couleur.BLANC, 0);
				else
					ajouterCase(Couleur.NOIR, ++position);
			}
		}

		this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	int height = (int) ((Component) e.getSource()).getSize().getHeight();
                int width = (int) ((Component) e.getSource()).getSize().getWidth();
                int taille = (height<width)?height:width;
                setSize(taille,taille);
                Plateau.this.validate();
            }
        });
	}

	private void ajouterCase(final Couleur couleur, final int position){
		Case case1 = new Case(couleur,position);
		if(couleur == Couleur.NOIR)
			case1.addMouseListener(new ListenerCase(case1, this));
		add(case1,BorderLayout.CENTER);
	}

	
}
