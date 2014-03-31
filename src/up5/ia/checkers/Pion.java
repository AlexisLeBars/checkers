package up5.ia.checkers;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

public class Pion extends Piece {

	private static final long serialVersionUID = 1436178861615738480L;

	public Pion(final Couleur couleur, final int position) {
		super(position);
		this.couleur = couleur;
		setOpaque(false);
		switch (couleur) {
			case BLANC :
				setForeground(Color.WHITE);
				setBackground(new Color(220, 220, 220));
				break;
			case NOIR :
				setForeground(new Color(70, 70, 70));
				setBackground(new Color(200, 200, 200));
				break;
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
		RenderingHints qualityHints=new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(qualityHints);
		
		if(this.supprimee){
			paint = new GradientPaint(0,0, this.couleur==Couleur.NOIR?Color.RED:Color.WHITE, getWidth(), getHeight(), this.couleur==Couleur.NOIR?Color.DARK_GRAY:Color.RED);
			g2d.setPaint(paint);
			g.fillOval(5, 5, getWidth()-10, getHeight()-10);
		}
		else{
			paint = new GradientPaint(0,0, getBackground(), getWidth(), getHeight(), getForeground());
			g2d.setPaint(paint);
			g.fillOval(5, 5, getWidth()-10, getHeight()-10);
		}
	}

}
