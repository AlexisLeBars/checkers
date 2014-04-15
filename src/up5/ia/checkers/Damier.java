package up5.ia.checkers;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
	
	public static int niveauIA;
	public int vi;

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
		niveauIA=1;
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

	public int[][] getDamier(){
		return this.damier;
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

	private void creerPion(final Couleur couleur, final int position){

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

	private void creerDame(final Couleur couleur,final int position){

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
	}

	private void disposerPions(){
		for(int position=1; position<21; position++){
			creerPion(this.damier,Couleur.NOIR, position);
			creerPion(Couleur.NOIR, position);
		}
		for(int position=31; position<51; position++){
			creerPion(this.damier,Couleur.BLANC, position);
			creerPion(Couleur.BLANC, position);
		}
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

	private void calculerCoupsPossibles(final int[][] damier, final int position, final Coup coup,ArrayList<Coup> coupsPossibles){
		// Puis-je prendre ?
		boolean prisePossible = false;
		ArrayList<Integer> prisesPossibles = null;
		prisesPossibles = positionsPossibles(damier,position,DIAG_HG,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveau paramétres
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
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup,coupsPossibles);
			}
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_HD,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveau paramétres
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
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup,coupsPossibles);
			}
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_BD,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveau paramètres
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
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup,coupsPossibles);
			}
		}
		prisesPossibles = positionsPossibles(damier,position,DIAG_BG,prisePattern);
		if( !prisesPossibles.isEmpty() ){
			prisePossible=true;
			// pour chaque prise possible, la fonction se rappelle elle même avec de nouveau paramètres
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
				calculerCoupsPossibles(cloneDamier,destination,cloneCoup,coupsPossibles);
			}
		}
		// je ne peux pas prendre, et je me suis déja déplacé
		if( !prisePossible && (coup.getPositionFinale() != 0 || !coup.getPositionsPiecesSupprimees().isEmpty()) ){
			coup.setPositionFinale(position);
			synchronized(coupsPossibles){
				coupsPossibles.add(coup);
			}
		}
		// je ne peux pas prendre, et je ne me suis pas déja déplacé, puis-je me déplacer ?
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
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
						synchronized(coupsPossibles){
							coupsPossibles.add(cloneCoup);
						}
					}
				}
			}
			// je ne peux pas prendre, je ne me suis pas déja dé½placé, je ne peux pas me déplacer -> aucun coup possible
		}
	}

	private class ThreadCalculerCoupsPossible implements Runnable{
		private int positionPiece;
		private ArrayList<Coup> coupsPossibles;
		private int [][] damier;
		public ThreadCalculerCoupsPossible(int[][] damier, int positionPiece,ArrayList<Coup> coupsPossibles){
			super();
			this.damier=damier;
			this.positionPiece=positionPiece;
			this.coupsPossibles = coupsPossibles;
		}

		@Override
		public void run() {
			calculerCoupsPossibles(damier, positionPiece,new Coup(positionPiece),this.coupsPossibles);
		}
	}

	public ArrayList<Coup> calculerCoupsPossibles(final int[][] damier, final Couleur couleur){
		ArrayList<Thread> threads = new ArrayList<Thread>();
		ArrayList<Integer> positions = new ArrayList<Integer>();
		ArrayList<Coup> coupsPossibles = new ArrayList<Coup>();

		int length = damier.length;
		int position = 0;
		for(int ligne=0 ; ligne<length; ligne++){
			for(int colonne=0; colonne<length;colonne++){
				if(!((colonne%2==0 && ligne%2==0) || (colonne%2!=0 && ligne%2!=0)))
					++position;
				if(couleur == Couleur.BLANC){
					if(damier[ligne][colonne] == PION_BLANC || damier[ligne][colonne] == DAME_BLANC)
						positions.add(position);
				}
				else{
					if(damier[ligne][colonne] == PION_NOIR || damier[ligne][colonne] == DAME_NOIR)
						positions.add(position);
				}
			}
		}

		for(int positionPiece : positions){
			Thread thread = new Thread(new ThreadCalculerCoupsPossible(damier,positionPiece,coupsPossibles));
			threads.add(thread);
			thread.start();
		}

		for(Thread thread : threads){
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		supprimerCoupsInterdits(coupsPossibles);
		return coupsPossibles;
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

	public void executionCoup(int[][] damier, final Coup coup){
		int source = coup.getPositionInitiale();
		int destination = coup.getPositionFinale();

		deplacerPiece(damier,source,destination);
		for(int positionsPiecesSupprimees : coup.getPositionsPiecesSupprimees())
			supprimerPiece(damier,positionsPiecesSupprimees);

		Point coordonneeFinale = positionToCoordonnees(destination);
		if( traitAux==Couleur.BLANC && coordonneeFinale.getX()==0  ){
			supprimerPiece(damier,destination);
			creerDame(damier,traitAux,destination);
		}
		if( traitAux==Couleur.NOIR && coordonneeFinale.getX()==(TAILLE-1) ){
			supprimerPiece(damier,destination);
			creerDame(damier,traitAux,destination);
		}
	}

	public void executionCoup(final Coup coup){

// Execution du coup demandé UI
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
// Execution du coup demandé damier[][]
		executionCoup(this.damier,coup);

// Réinitialisation des cases et des pièces + vidage coupsPossibles
		reinitEtatCases();
		reinitEtatPieces(traitAux);
		coupsPossibles.clear();
		
// Changement de joueur
		traitAux = (traitAux==Couleur.BLANC)?Couleur.NOIR:Couleur.BLANC;
		Lanceur.tour.setText(traitAux==Couleur.BLANC?"Tour : Blanc":"Tour : Noir");
	
	// Si le nouveau joueur est l'IA
		if(Lanceur.mode.getText()=="IA ON" && this.traitAux == Couleur.NOIR){
			Noeud racine = new Noeud(null,0,damier);
			int gain=0;
			switch(niveauIA){
			case 1:
				minMax(racine, traitAux, 3, true);
				gain = racine.getGain();
				break;
			case 2:
				minMax(racine,traitAux,4,true);
				gain=racine.getGain();
				break;
			case 3:
				gain = AlphaBeta(racine, this.traitAux, 0, 6, true, -3000, 3000);
				Lanceur.coupures.setText("Coupures A-B: "+vi);
				break;

			}
			//alphaBeta(racine,this.traitAux,4,true);
			System.out.println("Voies non parcourures: "+vi);
			vi=0;
			Lanceur.gain.setText("Gain IA: "+gain);
			System.out.println("gain final: "+gain);
			Coup coupIA = null;
			for(Noeud fils : racine.getFils()){
				//System.out.println("gain: "+fils.getGain());
				if(fils.getGain() == gain)
					coupIA = fils.getCoup();
			}
			if(coupIA != null)
				executionCoup(coupIA);
			else
				Lanceur.fin.setText("Partie finie, vainceur Blanc ! ");
		}
	// Si le nouveau joueur est humain

		// Calcul des coupsPossibles pour le nouveau joueur
		coupsPossibles = calculerCoupsPossibles(damier,traitAux);
		
		// Si pas de coupsPossibles pour le nouveau joueur, fin de la partie
		if(traitAux==Couleur.NOIR && coupsPossibles.isEmpty())
			Lanceur.fin.setText("Partie finie, vainceur Blanc ! ");
		else if(traitAux==Couleur.BLANC && coupsPossibles.isEmpty())
			Lanceur.fin.setText("Partie finie, vainceur Noir ! ");
	}


	private void minMax(Noeud racine, final Couleur trait, final int profondeurMax, boolean max){
 		if(racine.getProfondeur() < profondeurMax){
 			int[][] damier = racine.getEtat();
 			ArrayList<Coup> coupsPossibles = calculerCoupsPossibles(damier,trait);
 			for(Coup coup : coupsPossibles){
 				int[][] clone = damier.clone();
 				for(int i=0;i<clone.length;i++)
 					clone[i]=clone[i].clone();
 				executionCoup(clone,coup);
 				Noeud fils = new Noeud(racine,racine.getProfondeur()+1,clone);
 				racine.addFils(fils);
 				int evaluation = evaluationCoup(damier,coup);
 				if(max){
 					fils.setGain(racine.getGain()+evaluation);
 				}
 				else{
 					fils.setGain(racine.getGain()-evaluation);
 				}
 				fils.setCoup(coup);
 				Couleur traitAux = (trait==Couleur.BLANC)?Couleur.NOIR:Couleur.BLANC;
 				minMax(fils,traitAux,profondeurMax,!max);
 			}
 			
 			
 			ArrayList<Noeud> fils = racine.getFils();
 			if(max && !fils.isEmpty()){
 				int gmax = fils.get(0).getGain();
 				for(Noeud f : fils){
 					int gain = f.getGain();
 					gmax = ( gmax<gain )?gain:gmax ;
 				}
 				racine.setGain(gmax);
 			}
 			else if(!max && !fils.isEmpty()){
 				int gmin = fils.get(0).getGain();
 				for(Noeud f : fils){
 					int gain = f.getGain();
 					gmin = ( gmin<gain )?gmin:gain ;
 				}
 				racine.setGain(gmin);
 			}
 		}
 	}
	
	private int evaluationCoup(int[][] damier, Coup coup){
		int gain = 0;
		int positionInitiale = coup.getPositionInitiale();
		int positionFinale = coup.getPositionFinale();
		ArrayList<Integer> positionsPiecesSupprimees = coup.getPositionsPiecesSupprimees();
		
		// Pieces mangees ?
		for(Integer position : positionsPiecesSupprimees){
			Point coordonnees = positionToCoordonnees(position);
			int piece = damier[(int)coordonnees.getX()][(int)coordonnees.getY()];
			if(piece == DAME_NOIR || piece == DAME_BLANC)
				gain += 4;
			else
				gain +=2;
		}

		// Creation de Dame ?		
		if(this.niveauIA>=2){
			Point coordonnees = positionToCoordonnees(positionInitiale);
			int piece = damier[(int)coordonnees.getX()][(int)coordonnees.getY()];
			if(piece == PION_BLANC && positionFinale <= 5)
					gain+=4;
			else if(piece == PION_NOIR && positionFinale >= 46)
					gain+=4;
		}
		
		//Position finale ?
		if(this.niveauIA>=3){
			int position = positionFinale%10;
			if( (position == 5 || position == 6) && positionFinale != 5 && positionFinale != 46)
				gain +=1;
		}
			
		return gain;
	}
	//alpha beta version simple
	private void alphaBeta(Noeud racine, final Couleur trait, final int profondeurMax, boolean max){
		if(racine.getProfondeur() < profondeurMax){
			int[][] damier = racine.getEtat();
			ArrayList<Coup> coupsPossibles = calculerCoupsPossibles(damier,trait);
			for(Coup coup : coupsPossibles){
				int[][] clone = damier.clone();
				for(int i=0;i<clone.length;i++)
					clone[i]=clone[i].clone();//prends beaucoup trop de temps
				executionCoup(clone,coup);
				Noeud fils = new Noeud(racine,racine.getProfondeur()+1,clone);
				//int evaluation = evaluationCoup(damier,coup);
				if(max){
                    fils.setGain(racine.getGain()+evaluationCoup(damier, coup));
                    if(coupsPossibles.indexOf(coup)==coupsPossibles.size()-1)
                        racine.setGain(-300);//valeur neutre
                }
                else{
                    fils.setGain(racine.getGain()-evaluationCoup(damier, coup));
                    if(coupsPossibles.indexOf(coup)==coupsPossibles.size()-1)
                        racine.setGain(300);//valeur neutre
                }
				racine.addFils(fils);
				fils.setCoup(coup);
				Couleur traitAux = (trait==Couleur.BLANC)?Couleur.NOIR:Couleur.BLANC;
				alphaBeta(fils,traitAux,profondeurMax,!max);

				//coupure beta
				if(max){
					if(racine.getGain()<fils.getGain()){
						racine.setGain(fils.getGain());
					}
					if(racine.getProfondeur()!=0 && (racine.getParent().getGain()<fils.getGain())){
						racine.setGain(fils.getGain());
						vi++;
						break;
					}
				}
				//coupure alpha
				else if(!max){
					if(racine.getGain()>fils.getGain()){
						racine.setGain(fils.getGain());
					}
					if(racine.getProfondeur()!=0 && (racine.getParent().getGain()>fils.getGain())){
						racine.setGain(fils.getGain());
						vi++;
						break;
					}
				}
			}
			//set gain courant pere
			if(racine.getProfondeur() != 0){
				if(!max && racine.getParent().getGain()<racine.getGain()){
					racine.getParent().setGain(racine.getGain());
				}
				else if(max && racine.getParent().getGain()>racine.getGain()){
					racine.getParent().setGain(racine.getGain());
				}
			}
		}
	}
	//alpha beta version negamax
	private int AlphaBeta(Noeud racine, final Couleur trait,int prof, final int profondeurMax, boolean max,int alpha,int beta) {
		int val=0,best=0;
		int[][] damier = racine.getEtat();
		ArrayList<Coup> coupsPossibles = calculerCoupsPossibles(damier,trait);
		if(prof==profondeurMax){
		       return racine.getGain();
			 }
		else{
			best=-3000;
			for(Coup coup : coupsPossibles){
				int[][] clone = damier.clone();
				for(int i=0;i<clone.length;i++)
					clone[i]=clone[i].clone();//prends beaucoup trop de temps
				executionCoup(clone,coup);
				Noeud fils = new Noeud(racine,racine.getProfondeur()+1,clone);
				racine.addFils(fils);
				fils.setCoup(coup);
				if(max)
					fils.setGain(racine.getGain()+evaluationCoup(damier, coup));
				else
					fils.setGain(racine.getGain()-evaluationCoup(damier, coup));
				Couleur traitAux = (trait==Couleur.BLANC)?Couleur.NOIR:Couleur.BLANC;
				val=-AlphaBeta(fils,traitAux,prof+1,profondeurMax,!max,-beta,-alpha);
				if( val > best){
	               best = val;
	               if( best > alpha ){
	                      alpha = best;
	                      fils.setGain(alpha);
	                   if( alpha >= beta){//coupure
	                	   vi++;
	                       return best;
	                   }
	               }
				}
			}
			racine.setGain(best);
			return best;
		}
	}
}