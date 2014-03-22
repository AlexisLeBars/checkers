package up5.ia.checkers;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;

import javax.swing.JPanel;


public class Case extends JPanel {
	
	private static final long serialVersionUID = -1839026893240660968L;
	
	private Couleur couleur;
	private boolean intermediaire;
	private boolean finale;
	private boolean selectionnee;

	public Case(Couleur couleur){
		setLayout(new GridLayout(1,1));
		this.couleur=couleur;
		initCouleur();
	}

	public Couleur getCouleur() {
		return couleur;
	}

	public boolean isIntermediaire() {
		return intermediaire;
	}

	public void setIntermediaire(boolean intermediaire) {
		this.intermediaire = intermediaire;
		initCouleur();
	}
	
	public boolean isFinale(){
		return finale;
	}
	
	public void setFinale(Boolean finale){
		this.finale=finale;
		initCouleur();
	}
	
	public boolean isSelectionnee(){
		return selectionnee;
	}
	
	public void setSelectionnee(Boolean selectionnee){
		this.selectionnee = selectionnee;
	}
	
	/**
	 * Réinitialise l'aspect d'une case selon ses attributs intermediaire, finale, selectionnee
	 */
	private void initCouleur(){
		switch(couleur){
			case BLANC :	
				setBackground(Color.WHITE);
				setForeground(new Color(200, 200, 200));
				break;
			case NOIR :
				setBackground(Color.GRAY);
				setForeground(new Color(20, 20, 20));
				break;
		}

		if(intermediaire){
			setBackground(Color.BLUE);
			setForeground(Color.LIGHT_GRAY);
		}
		if(finale){
			// TO DO
		}
	}
	
	/**
	 * ....
	 * @param g ....
	 */
	@Override
	public void paintComponent(Graphics g){
		Paint paint;
		Graphics2D g2d;
		if (g instanceof Graphics2D) {
			g2d = (Graphics2D) g;
		}
		else {
			System.out.println("Error");
			return;
		}
		paint = new GradientPaint(0,0, getBackground(), getWidth(), getHeight(), getForeground());
		g2d.setPaint(paint);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
