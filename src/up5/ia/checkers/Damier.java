package up5.ia.checkers;

import java.util.ArrayList;

public class Damier extends Plateau {
	
	private static final long serialVersionUID = 6726708245444190461L;

	private static final int TAILLE=10;
	private static final int NB_LIGNES=4; // nb de ligne de pieces d'une couleur lors de la disposition des pieces
	private byte[][] damier;
	
	private ArrayList<Piece> piecesNoirs;
	private ArrayList<Piece> piecesBlancs;
	private ArrayList<Coup> coupsBlancPossibles;
	private ArrayList<Coup> coupsNoirPossibles;
	private Coup coupEnCours;

	private boolean tourNoir;

	public Damier(){
		super(TAILLE);
damier=new byte[TAILLE][TAILLE];
for(int i=0; i<((TAILLE-1)/2); i++){
	for(int j=1-i%2; j<TAILLE; j+=2){
		damier[i][j] = 2; // pion noir
		damier[i+TAILLE-NB_LIGNES][j] = 1; //pion blanc
	}
}
		tourNoir=false;
		piecesNoirs = new ArrayList<Piece>();
		piecesBlancs = new ArrayList<Piece>();
		coupsBlancPossibles = new ArrayList<Coup>();
		coupsNoirPossibles = new ArrayList<Coup>();
		disposerPions();
	}

	/**
	 * Crée un pion de la couleur spécifiée et sur la case du damier spécifiée et l'ajoute à la liste des pièces de la couleur spécifiée
	 * @param couleur est la couleur du pion
	 * @param case1 est la case où sera placé le pion
	 * @return retourne le pion ainsi créée
	 */
	private Pion creerPion(Couleur couleur, Case case1){
		Pion pion = new Pion(couleur);
		pion.addMouseListener(new ListenerPion(pion, this));
		case1.add(pion);
		switch(couleur){
			case BLANC :
				piecesBlancs.add(pion);
				break;
			case NOIR :
				piecesNoirs.add(pion);
				break;
		}
		return pion;
	}
	/**
	 * Crée une dame de la couleur spécifiée et sur la case du damier spécifiée et l'ajoute à la liste des pièces de la couleur spécifiée
	 * @param couleur est la couleur de la dame
	 * @param case1 est la case où sera placée la dame
	 * @return retourne la dame ainsi créée
	 */
	private Dame creerDame(Couleur couleur, Case case1){
		Dame dame = new Dame(couleur);
		dame.addMouseListener(new ListenerDame(dame, this));
		case1.add(dame);
		switch(couleur){
		case BLANC :
			piecesBlancs.add(dame);
			break;
		case NOIR :
			piecesNoirs.add(dame);
			break;
		}
		return dame;
	}
	
	/**
	 * Supprime une piece sur le damier et dans la liste des pièces
	 * @param piece est la pièce à supprimer
	 */
	private void supprimerPiece(Piece piece){
		switch(piece.getCouleur()){
			case BLANC :
				piecesBlancs.remove(piece);
				break;
			case NOIR :
				piecesNoirs.remove(piece);
				break;
		}
		Case case1 = (Case) piece.getParent();
		case1.removeAll();
		case1.validate();
		case1.repaint();
	}
	
	/**
	 * Déplace une pièce sur la case destination spécifiée
	 * @param piece est la pièce à déplacer
	 * @param destination est la case sur laquelle la pièce est déplacée
	 */
	private void deplacerPiece(Piece piece,Case destination){
		Case provenance = (Case) piece.getParent();
		provenance.removeAll();
		provenance.validate();
		provenance.repaint();
		destination.add(piece);
		destination.validate();
		destination.repaint();
	}

	/**
	 * Dispose les pions pour commencer un nouvelle partie de Dames. Chaque pion est ajouté selon sa couleur dans la liste des pièces correspondante.
	 */
	private void disposerPions(){
		for(int i=0; i<((TAILLE-1)/2); i++){
			for(int j=1-i%2; j<TAILLE; j+=2){
				creerPion(Couleur.NOIR,getCase(i,j));
				creerPion(Couleur.BLANC,getCase(i+TAILLE-NB_LIGNES,j));
			}
		}
	}

	public Case getCase(int i, int j){
		return (Case) getComponent(j+i*TAILLE);
	}

	public Case getCase(int i){
		return (Case) getComponent(i);
	}

	/**
	 * Liste tous les coups possibles pour le joueur de la couleur spécifiée
	 * @param couleur est la couleur du joueur
	 */
	public void calculerCoupsPossible(Couleur couleur){
		// TO DO
	}
	
	/**
	 * Affiche les coups possible pour une pièce donnée en modifiant l'apparence de certaines cases 
	 * @param piece est la pièce dont les coups possibles sont à afficher
	 */
	public void afficherCoups(Piece piece){
		// TO DO
	}
}
