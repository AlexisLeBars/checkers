package up5.ia.checkers;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;

import javax.swing.JPanel;


public class Case extends JPanel implements Cloneable{
	
	private static final long serialVersionUID = -1839026893240660968L;
	
	private int position;
	private Couleur couleur;
	private boolean finale;

	public Case(final Couleur couleur,final int position){
		this.position = position;
		setLayout(new GridLayout(1,1));
		this.couleur=couleur;
		initCouleur();
	}

	public Case clone() throws CloneNotSupportedException {
		return (Case) super.clone();
	}

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

		if(finale){
			setBackground(Color.RED);
			setForeground(Color.LIGHT_GRAY);
		}
	}

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

	public boolean isFinale(){
		return finale;
	}

	public void setFinale(final Boolean finale){
		this.finale=finale;
		initCouleur();
	}

	public void setEtatParDefault(){
		setFinale(false);
	}

	public Couleur getCouleur() {
		return couleur;
	}
	
	public int getPosition(){
		return position;
	}
}
