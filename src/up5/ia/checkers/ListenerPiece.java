package up5.ia.checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ListenerPiece extends MouseAdapter {
	
	private Damier damier;
	private Piece piece;
	
	public ListenerPiece(final Piece piece, final Damier damier){
		this.damier=damier;
		this.piece=piece;
	}

	public void mousePressed(MouseEvent arg0) {
		damier.setPositionPieceActive( piece.getPosition() );
		damier.afficherCoups(piece);
	}

}
