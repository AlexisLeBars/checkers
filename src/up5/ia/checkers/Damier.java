package up5.ia.checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Damier extends Plateau {
	
	private static final long serialVersionUID = 6726708245444190461L;

	private static final int TAILLE=10;
	private int[][] damier;
	public static final int VIDE=0;
	public static final int PION_BLANC=1;
	public static final int PION_NOIR=2;
	public static final int DAME_BLANC=3;
	public static final int DAME_NOIR=4;
	public static final int PIECE_PRISE=5;
	public static final Point DIAG_HG = new Point(-1,-1);
	public static final Point DIAG_HD = new Point(-1,1);
	public static final Point DIAG_BG = new Point(1,-1);
	public static final Point DIAG_BD = new Point(1,1);
	
	private final String priseRegex = "(^"+PION_BLANC+"["+PION_NOIR+""+DAME_NOIR+"]"+VIDE+")|(^"+PION_NOIR+"["+PION_BLANC+""+DAME_BLANC+"]"+VIDE+")|(^"+DAME_BLANC+""+VIDE+"*["+PION_NOIR+""+DAME_NOIR+"]"+VIDE+")|(^"+DAME_NOIR+""+VIDE+"*["+PION_BLANC+""+DAME_BLANC+"]"+VIDE+")";
	private final String deplacementRegex = "^[1-4][^0]^["+PION_BLANC+"-"+DAME_NOIR+"][^"+VIDE+"]";

	private ArrayList<Piece> piecesBlancs;
	private ArrayList<Piece> piecesNoirs;

	private ArrayList<Coup> coupsPossibles;
	private Coup coupEnCours;

	private Couleur traitAux;

	public Damier(){
		super(TAILLE);
		damier=new int[TAILLE][TAILLE];
		for(int[] ligne : damier)
			Arrays.fill(ligne,VIDE);
		traitAux=Couleur.BLANC;
		piecesBlancs = new ArrayList<Piece>();
		piecesNoirs = new ArrayList<Piece>();
		coupsPossibles = new ArrayList<Coup>();
		coupEnCours = null;
		disposerPions();
	}

	public Point positionToCoordonnees(final int position){
		int ligne = (position-1)/5;
		int colonne=0;
		switch(position%10){
			case 0 : colonne = 8; break;
			case 1 : colonne = 1; break;
			case 2 : colonne = 3; break;
			case 3 : colonne = 5; break;
			case 4 : colonne = 7; break;
			case 5 : colonne = 9; break;
			case 6 : colonne = 0; break;
			case 7 : colonne = 2; break;
			case 8 : colonne = 4; break;
			case 9 : colonne = 6; break;	
		}
		return new Point(ligne,colonne);
	}
	
	public void setCoupsPossibles(final ArrayList<Coup> coupsPossibles){
		this.coupsPossibles = coupsPossibles;
	}

	public void setCoupEnCours(final Coup coup){
		this.coupEnCours=coup;
	}

	public Case getCase(final int position){
		return (Case) getComponent( (int) (positionToCoordonnees(position).getY()+positionToCoordonnees(position).getX()*TAILLE) );
	}
	
	public Piece getPiece(final int position){
		return (Piece) ( (Case) getComponent( (int) (positionToCoordonnees(position).getY()+positionToCoordonnees(position).getX()*TAILLE) )).getComponent(0);
	}
	
	public Couleur getTrait(){
		return this.traitAux;
	}

	public Coup getCoupEnCours(){
		return this.coupEnCours;
	}

	private void creerPion(int[][] damier, final Couleur couleur, final int position){
		switch(couleur){
			case BLANC :
				damier[(int) positionToCoordonnees(position).getX()][(int) positionToCoordonnees(position).getY()] = PION_BLANC;
				break;
			case NOIR :
				damier[(int) positionToCoordonnees(position).getX()][(int) positionToCoordonnees(position).getY()] = PION_NOIR;
				break;
		}
	}

	private Pion creerPion(final Couleur couleur, final int position){
		
		creerPion(damier, couleur, position);
		
		Pion pion = new Pion(couleur, position);
		pion.addMouseListener(new ListenerPiece(pion, this));
		Case case1 = getCase(position);
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

	private void creerDame(int[][] damier, final Couleur couleur, final int position){
		switch(couleur){
			case BLANC :
				damier[(int) positionToCoordonnees(position).getX()][(int) positionToCoordonnees(position).getY()] = DAME_BLANC;
				break;
			case NOIR :
				damier[(int) positionToCoordonnees(position).getX()][(int) positionToCoordonnees(position).getY()] = DAME_NOIR;
				break;
		}
	}

	private Dame creerDame(final Couleur couleur,final int position){
		
		creerDame(damier, couleur, position);
		
		Dame dame = new Dame(couleur, position);
		dame.addMouseListener(new ListenerPiece(dame, this));
		Case case1 = getCase(position);
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

	private void disposerPions(){
		for(int position=1; position<21; position++)
			creerPion(Couleur.NOIR, position);
		for(int position=31; position<51; position++)
			creerPion(Couleur.BLANC, position);
	}
	
	private void marquerPieceSupprimee(int[][] damier ,final int position){
		damier[(int) positionToCoordonnees(position).getX()][(int) positionToCoordonnees(position).getY()] = PIECE_PRISE;
	}

	private void supprimerPiece(int[][] damier, final int position){
		damier[(int) positionToCoordonnees(position).getX()][(int) positionToCoordonnees(position).getY()] = VIDE;
	}

	private void supprimerPiece(final int position){

		int piece = damier[(int) positionToCoordonnees(position).getX()][(int) positionToCoordonnees(position).getY()] = VIDE;
		if(piece == PION_BLANC || piece == DAME_BLANC)
			piecesBlancs.remove(piece);
		else if(piece == PION_NOIR || piece == DAME_NOIR)
			piecesNoirs.remove(piece);

		supprimerPiece(damier, position);

		Case case1 = getCase(position);
		case1.removeAll();
		case1.validate();
		case1.repaint();
	}

	private void deplacerPiece(int[][] damier, final int source,final int destination){
		int sourceX = (int) positionToCoordonnees(source).getX();
		int sourceY = (int) positionToCoordonnees(source).getY();
		int piece = damier[sourceX][sourceY];
		int destinationX = (int) positionToCoordonnees(source).getX();
		int destinationY = (int) positionToCoordonnees(source).getY();
		
		damier[sourceX][sourceY] = VIDE;
		switch(piece){
			case PION_BLANC :
				damier[destinationX][destinationY] = PION_BLANC; break;
			case DAME_BLANC :
				damier[destinationX][destinationY] = DAME_BLANC; break;
			case PION_NOIR :
				damier[destinationX][destinationY] = PION_NOIR; break;
			case DAME_NOIR :
				damier[destinationX][destinationY] = DAME_NOIR; break;
		}
	}

	private void deplacerPiece(final int source,final int destination){
	
		deplacerPiece(damier, source, destination);
		
		Piece piece = getPiece(source);
		Case provenanceCase = getCase(source);
		Case destinationCase = getCase(destination);
		destinationCase.add(piece);
		piece.setPosition(destination);
		provenanceCase.removeAll();
		provenanceCase.validate();
		provenanceCase.repaint();
		destinationCase.validate();
		destinationCase.repaint();
	}
	
	private ArrayList<Integer> positionsPossibles(final int[][] damier,final int position, final Point direction, final String regex){
		Point coordonnees = positionToCoordonnees(position);
		int ligne = (int) coordonnees.getX();
		int colonne = (int) coordonnees.getY();
		
		int directionLigne = (int) direction.getX();
		int directionColonne = (int) direction.getY();
		
		StringBuilder diagonale = new StringBuilder( String.valueOf(damier[ligne][colonne]) );
		try{
			for(int i=1;true;i++){
				diagonale.append(damier[ligne+i*directionLigne][colonne+i*directionColonne]);
			}
		}
		catch(Exception e){
		}
// TODO
		if( Pattern.matches(regex, diagonale) ){
			
		}
		return new ArrayList<Integer>();
	}

// A COMPLETER
	private void calculerCoupsPossibles(final int[][] damier, final int position, final Coup coup){
	// Puis-je prendre ?
		boolean prisePossible = false;
		ArrayList<Integer> prisesPossibles = null;
		prisesPossibles = positionsPossibles(damier,position,DIAG_HG,priseRegex);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour toutes les prises possibles on rappelle la fonction
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_HD,priseRegex);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour toutes les prises possibles on rappelle la fonction
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_BD,priseRegex);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour toutes les prises possibles on rappelle la fonction
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_BG,priseRegex);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour toutes les prises possibles on rappelle la fonction
		}
	// je ne peux pas prendre, et je me suis déjà déplacé
		if( !prisePossible && (coup.getPositionFinale() != 0 || !coup.getPositionsPiecesSupprimees().isEmpty()) ){
			Coup clone = null;
			try{
				clone = (Coup) coup.clone();
			}
			catch(Exception e){
			}
			clone.setPositionFinale(position);
			this.coupsPossibles.add(clone);
		}
	// je ne peux pas prendre, et je ne me suis pas déjà déplacé, puis-je me déplacer ?
		else if( !prisePossible ){
			ArrayList<Integer> deplacementsPossibles = null;
			Point coordonnees = positionToCoordonnees(position);
			int piece = damier[(int) coordonnees.getX()][(int) coordonnees.getY()];
			if(piece == PION_BLANC || piece==DAME_BLANC){
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_HG,deplacementRegex);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
				}
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_HD,deplacementRegex);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
				}
			}
			else if(piece == PION_NOIR || piece==DAME_NOIR){
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_BD,deplacementRegex);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
				}
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_BG,deplacementRegex);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
				}
			}	
			// je ne peux pas prendre, je ne me suis pas déjà déplacé, je ne peux pas me déplacer -> aucun coup possible
		}
	}

	public void calculerCoupsPossibles(final Couleur couleur){
		ArrayList<Piece> pieces = (couleur == Couleur.BLANC)?piecesBlancs:piecesNoirs;
		for(Piece piece : pieces)
			calculerCoupsPossibles(damier, piece.getPosition(),new Coup(piece.getPosition()));
		supprimerCoupsInterdits(coupsPossibles);
	}

	public void supprimerCoupsInterdits(ArrayList<Coup> coups){
		// TO DO
	}

	public void afficherCoups(final Piece piece){
	
		reinitEtatCases();
		reinitEtatPieces(piece.getCouleur());
	
		if(piece.getCouleur() == traitAux){
			for(Coup coup : coupsPossibles){
				if( coup.getPositionInitiale() == piece.getPosition() ){
					for(int positionPieceSupprimee : coup.getPositionsPiecesSupprimees() )
						getPiece(positionPieceSupprimee).setSupprimee(true);
					(getCase(coup.getPositionFinale())).setFinale(true);
				}
			}
		}
	}

	public void reinitEtatCases(){
		for(int i=0; i<TAILLE*TAILLE; i++)
			((Case) getComponent(i)).setEtatParDefault();
	}
	
	public void reinitEtatPieces(final Couleur traitAux){
		switch(traitAux){
			case BLANC :
				for(Piece pieceReinit: piecesBlancs)
					pieceReinit.setSupprimee(false);
				break;
			case NOIR :
				for(Piece pieceReinit: piecesNoirs)
					pieceReinit.setSupprimee(false);
				break;
		}
	}

	public boolean isCoupValide(final Coup coup){
		for(Coup coupPossible : coupsPossibles){
			if( (coupPossible.getPositionInitiale() == coup.getPositionInitiale()) && (coupPossible.getPositionFinale() == coup.getPositionFinale()) )
				return true;
		}
		return false;
	}

	public void executionCoup(final Coup coup){
		int source = coup.getPositionInitiale();
		int destination = coup.getPositionFinale();
		
		deplacerPiece(source,destination);
		for(int positionsPiecesSupprimees : coup.getPositionsPiecesSupprimees())
			supprimerPiece(positionsPiecesSupprimees);
		
		Point coordonneeFinale = positionToCoordonnees(destination);
		if( coordonneeFinale.getY()==0  && traitAux==Couleur.BLANC ){
			supprimerPiece(destination);
			creerDame(traitAux,destination);
		}
		else if( coordonneeFinale.getY()==(TAILLE-1)  && traitAux==Couleur.NOIR ){
			supprimerPiece(destination);
			creerDame(traitAux,destination);
		}
	
		reinitEtatCases();
		reinitEtatPieces(traitAux);
	
		traitAux = (traitAux==Couleur.BLANC)?Couleur.NOIR:Couleur.BLANC;
		calculerCoupsPossibles(traitAux);
	}
}