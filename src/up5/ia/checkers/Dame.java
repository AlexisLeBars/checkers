package up5.ia.checkers;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class Dame extends Piece {

	private static final long serialVersionUID = 1436178861615738480L;

	public Dame(final Couleur couleur, final int position) {
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
	
	// TODO A modifier pour que la dame ne ressemble pas à un pion
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
		g.fillOval(5, 5, getWidth()-10, getHeight()-10);
	}
}
