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
	private final String deplacementRegex = "^["+PION_BLANC+"-"+DAME_NOIR+"]"+VIDE;
	private final Pattern prisePattern = Pattern.compile(priseRegex);
	private final Pattern deplacementPattern = Pattern.compile(deplacementRegex);

	private ArrayList<Piece> piecesBlancs;
	private ArrayList<Piece> piecesNoirs;

	private ArrayList<Coup> coupsPossibles;
	private int positionPieceActive;

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
		positionPieceActive = 0;
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

	public void setPositionPieceActive(final int positionPieceActive){
		this.positionPieceActive=positionPieceActive;
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

	public int getPositionPieceActive(){
		return this.positionPieceActive;
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

	public Pion creerPion(final Couleur couleur, final int position){
		
		creerPion(damier, couleur, position);
		
		Pion pion = new Pion(couleur, position);
		pion.addMouseListener(new ListenerPiece(pion, this));
		Case case1 = getCase(position);
		case1.add(pion);
		case1.validate();
		case1.repaint();

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
		case1.validate();
		case1.repaint();

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
		if(destination != source){
			int sourceX = (int) positionToCoordonnees(source).getX();
			int sourceY = (int) positionToCoordonnees(source).getY();

			int destinationX = (int) positionToCoordonnees(destination).getX();
			int destinationY = (int) positionToCoordonnees(destination).getY();
			
			damier[destinationX][destinationY]=damier[sourceX][sourceY];
			damier[sourceX][sourceY] = VIDE;
		}
	}

	private void deplacerPiece(final int source,final int destination){
		if(destination != source){
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
	}
	
	public int positionSuivante(final int position, final Point direction){
		if(positionToCoordonnees(position).getX()%2 == 0){
			if(direction == DIAG_HG)
				return position-5;
			else if(direction == DIAG_HD)
				return position-4;
			else if(direction == DIAG_BD)
				return position+6;
			else
				return position+5;
		}
		else{
			if(direction == DIAG_HG)
				return position-6;
			else if(direction == DIAG_HD)
				return position-5;
			else if(direction == DIAG_BD)
				return position+5;
			else
				return position+4;
		}
	}
	
	private ArrayList<Integer> positionsPossibles(final int[][] damier,final int position, final Point direction, final Pattern pattern){
		Point coordonnees = positionToCoordonnees(position);
		int ligne = (int) coordonnees.getX();
		int colonne = (int) coordonnees.getY();

		int directionLigne = (int) direction.getX();
		int directionColonne = (int) direction.getY();

		StringBuilder diagonale = new StringBuilder( String.valueOf(damier[ligne][colonne]) );
		try{
			for(int i=1;true;i++)
				diagonale.append(damier[ligne+i*directionLigne][colonne+i*directionColonne]);
		}
		catch(Exception e){
		}

		ArrayList<Integer> positionsPossibles=new ArrayList<Integer>();
		ArrayList<Integer> positions=new ArrayList<Integer>();

		if( pattern.matcher(diagonale).lookingAt() ){
			int positionPrecedente = position;
			for(int i=0;i<diagonale.length()-1;i++){
				positionPrecedente = positionSuivante(positionPrecedente, direction);
				positions.add(positionPrecedente);
			}
			int i = 1;
			if(pattern == prisePattern){
				//mange
				while( diagonale.charAt(i) == '0')
					i++;
				i++;
				positionsPossibles.add(positions.get(i-1));
				if( damier[ligne][colonne] == DAME_BLANC ||  damier[ligne][colonne] == DAME_NOIR){
					while(i<diagonale.length() && diagonale.charAt(i) == '0' ){
						positionsPossibles.add(positions.get(i-1));
						i++;
					}
				}
			}
			else{
				//déplacement simple
				positionsPossibles.add(positions.get(0));
				if( damier[ligne][colonne] == DAME_BLANC ||  damier[ligne][colonne] == DAME_NOIR){
					i=2;
					while(i<diagonale.length() && diagonale.charAt(i) == '0' ){
						positionsPossibles.add(positions.get(i-1));
						i++;
					}
				}
			}
		}
		return positionsPossibles;
	}

	private void calculerCoupsPossibles(final int[][] damier, final int position, final Coup coup){
	// Puis-je prendre ?
		boolean prisePossible = false;
		ArrayList<Integer> prisesPossibles = null;
		prisesPossibles = positionsPossibles(damier,position,DIAG_HG,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveux paramètres
			for(int destination : prisesPossibles){
				int[][] cloneDamier = damier.clone();
				for(int i=0; i<cloneDamier.length; i++)
					cloneDamier[i] = cloneDamier[i].clone();
				Coup cloneCoup = null;
				try{
					cloneCoup = coup.clone();
				}
				catch(Exception e){
				}
				int positionPiecePrise = positionSuivante(prisesPossibles.get(0),DIAG_BD);
				cloneCoup.addPositionPieceSupprimee(positionPiecePrise);
				marquerPieceSupprimee(cloneDamier,positionPiecePrise);
				deplacerPiece(cloneDamier,position,destination);
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup);
			}
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_HD,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveux paramètres
			for(int destination : prisesPossibles){
				int[][] cloneDamier = damier.clone();
				for(int i=0; i<cloneDamier.length; i++)
					cloneDamier[i] = cloneDamier[i].clone();
				Coup cloneCoup = null;
				try{
					cloneCoup = coup.clone();
				}
				catch(Exception e){
				}
				int positionPiecePrise = positionSuivante(prisesPossibles.get(0),DIAG_BG);
				cloneCoup.addPositionPieceSupprimee(positionPiecePrise);
				marquerPieceSupprimee(cloneDamier,positionPiecePrise);
				deplacerPiece(cloneDamier,position,destination);
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup);
			}
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_BD,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveux paramètres
			for(int destination : prisesPossibles){
				int[][] cloneDamier = damier.clone();
				for(int i=0; i<cloneDamier.length; i++)
					cloneDamier[i] = cloneDamier[i].clone();
				Coup cloneCoup = null;
				try{
					cloneCoup = coup.clone();
				}
				catch(Exception e){
				}
				int positionPiecePrise = positionSuivante(prisesPossibles.get(0),DIAG_HG);
				cloneCoup.addPositionPieceSupprimee(positionPiecePrise);
				marquerPieceSupprimee(cloneDamier,positionPiecePrise);
				deplacerPiece(cloneDamier,position,destination);
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup);
			}
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_BG,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveux paramètres
			for(int destination : prisesPossibles){
				int[][] cloneDamier = damier.clone();
				for(int i=0; i<cloneDamier.length; i++)
					cloneDamier[i] = cloneDamier[i].clone();
				Coup cloneCoup = null;
				try{
					cloneCoup = coup.clone();
				}
				catch(Exception e){
				}
				int positionPiecePrise = positionSuivante(prisesPossibles.get(0),DIAG_HD);
				cloneCoup.addPositionPieceSupprimee(positionPiecePrise);
				marquerPieceSupprimee(cloneDamier,positionPiecePrise);
				deplacerPiece(cloneDamier,position,destination);
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup);
			}
		}
	// je ne peux pas prendre, et je me suis déjà déplacé
		if( !prisePossible && (coup.getPositionFinale() != 0 || !coup.getPositionsPiecesSupprimees().isEmpty()) ){
			coup.setPositionFinale(position);
			this.coupsPossibles.add(coup);
		}
	// je ne peux pas prendre, et je ne me suis pas déjà déplacé, puis-je me déplacer ?
		else if( !prisePossible ){
			ArrayList<Integer> deplacementsPossibles = null;
			Point coordonnees = positionToCoordonnees(position);
			int piece = damier[(int) coordonnees.getX()][(int) coordonnees.getY()];
			if(piece == PION_BLANC){
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_HG,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
				}
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_HD,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
				}
			}
			else if(piece == PION_NOIR){
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_BD,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
				}
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_BG,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
				}
			}
			else if(piece == DAME_BLANC || piece == DAME_NOIR){
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_HG,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
				}
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_HD,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
				}
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_BD,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
				}
				deplacementsPossibles = positionsPossibles(damier,position,DIAG_BG,deplacementPattern);
				if( !deplacementsPossibles.isEmpty() ){
					// pour tous les déplacements possibles, j'ajoute un coup dans les coupsPossibles
					for(int destination : deplacementsPossibles){
						int[][] cloneDamier = damier.clone();
						for(int i=0; i<cloneDamier.length; i++)
							cloneDamier[i] = cloneDamier[i].clone();
						Coup cloneCoup = null;
						try{
							cloneCoup = coup.clone();
						}
						catch(Exception e){
						}
						deplacerPiece(cloneDamier,position,destination);
						cloneCoup.setPositionFinale(destination);
						this.coupsPossibles.add(cloneCoup);
					}
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
	    int max=0;
	    int nbPiecesPrises=0;
	    
	    for(Coup coup : coups){
	        nbPiecesPrises = coup.getPositionsPiecesSupprimees().size();
	        if(max<nbPiecesPrises)
	            max = nbPiecesPrises;
	    }
	    ArrayList<Coup> coupsASupprimer = new ArrayList<Coup>();
	    
	    for(Coup coup : coups){
	        if(coup.getPositionsPiecesSupprimees().size() < max)
	            coupsASupprimer.add(coup);
	    }
	    
	    coups.removeAll(coupsASupprimer);
	}

	public void afficherCoups(final Piece piece){
	
		reinitEtatCases();
		reinitEtatPieces( (piece.getCouleur()==Couleur.BLANC)?Couleur.NOIR:Couleur.BLANC);
	
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

	public Coup getCoupValide(final int positionInitiale, final  int positionFinale){
		for(Coup coupPossible : coupsPossibles){
			if( (coupPossible.getPositionInitiale() == positionInitiale) && (coupPossible.getPositionFinale() == positionFinale) )
				return coupPossible;
		}
		return null;
	}

	public void executionCoup(final Coup coup){
		int source = coup.getPositionInitiale();
		int destination = coup.getPositionFinale();
		
		deplacerPiece(source,destination);
		for(int positionsPiecesSupprimees : coup.getPositionsPiecesSupprimees())
			supprimerPiece(positionsPiecesSupprimees);
		
		Point coordonneeFinale = positionToCoordonnees(destination);
		if( traitAux==Couleur.BLANC && coordonneeFinale.getX()==0  ){
			supprimerPiece(destination);
			creerDame(traitAux,destination);
		}
		if( traitAux==Couleur.NOIR && coordonneeFinale.getX()==(TAILLE-1) ){
			supprimerPiece(destination);
			creerDame(traitAux,destination);
		}
	
		this.setPositionPieceActive(0);
		reinitEtatCases();
		traitAux = (traitAux==Couleur.BLANC)?Couleur.NOIR:Couleur.BLANC;
		if(traitAux==Couleur.BLANC){
			Lanceur.tour.setText("Tour : Blanc");
		}
		else{
			Lanceur.tour.setText("Tour : Noir");
		}
		reinitEtatPieces(traitAux);
		coupsPossibles.clear();
		calculerCoupsPossibles(traitAux);
		if(coupsPossibles.isEmpty() && traitAux==Couleur.NOIR){
			Lanceur.fin.setText("Partie finie, vainceur Blanc ! ");
		}
		else if(coupsPossibles.isEmpty() && traitAux==Couleur.BLANC)
			Lanceur.fin.setText("Partie finie, vainceur Noir ! ");
	}
}