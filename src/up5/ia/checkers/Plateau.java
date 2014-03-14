package up5.ia.checkers;

import java.awt.GridLayout;
import javax.swing.JPanel;


public class Plateau extends JPanel {

	private static final long serialVersionUID = 6726708245444190460L;

	private static final int TAILLE=9;

	private Case caseActive;

	private boolean tourNoir;

	public Plateau(int taille){
		tourNoir=false;
		setLayout(new GridLayout(TAILLE, TAILLE));
		for(int i=0; i<TAILLE; i++){
			for(int j=0; j<TAILLE; j++){
				if((j%2==0 && i%2==0) || (j%2!=0 && i%2!=0)){
					ajouterCase(Couleur.NOIR);
				}
				else{
					ajouterCase(Couleur.BLANC);
				}
			}
		}
		init();
	}

	private void ajouterCase(Couleur couleur){
		Case case1 = new Case(couleur);
		case1.addMouseListener(new ListenerCase(case1, this));
		add(case1);
	}

	private Pion creerPion(Couleur couleur, boolean monte){
		Pion pion = new Pion(couleur, monte);
		pion.addMouseListener(new ListenerPion(pion, this));
		return pion;
	}

	private void init(){
		for(int j=0; j<TAILLE*3; j+=2){
			getCase(j).add(creerPion(Couleur.NOIR, false));
			getCase(TAILLE*TAILLE-j-1).add(creerPion(Couleur.BLANC, true));
		}
	}

	public Case getCase(int i, int j){
		return (Case) getComponent(j+i*TAILLE);
	}

	public Case getCase(int i){
		return (Case) getComponent(i);
	}

	public void afficherPossibilites(Pion p){
		if((p.getCouleur().equals(Couleur.NOIR) && tourNoir) || (p.getCouleur().equals(Couleur.BLANC) && !tourNoir)){
			int i=0;
			int j=0;
			for(int k=0; k<TAILLE*TAILLE; k++){
				getCase(k).setSelectionnee(false);
				if(getCase(k).getComponentCount()!=0 && getCase(k).getComponent(0).equals(p)){
					caseActive=getCase(k);
					i=k/TAILLE;
					j=k%TAILLE;

				}
			}
			selectionnerCases(i, j, p.getCouleur());
		}
	}

	public void selectionnerCases(int i, int j, Couleur couleur){
		Pion pion = (Pion)(getCase(i, j).getComponent(0));
		if(pion.isMonte()){
			if(i-1>=0 && j-1>=0 && getCase(i-1, j-1).getComponentCount()==0){
				getCase(i-1, j-1).setSelectionnee(true);
			}
			else if(i-2>=0 && j-2>=0 && getCase(i-2, j-2).getComponentCount()==0 && !((Pion)(getCase(i-1, j-1).getComponent(0))).getCouleur().equals(couleur)){
				getCase(i-2, j-2).setSelectionnee(true);
			}
			if(i-1>=0 && j+1<TAILLE && getCase(i-1, j+1).getComponentCount()==0){
				getCase(i-1, j+1).setSelectionnee(true);
			}
			else if(i-2>=0 && j+2<TAILLE && getCase(i-2, j+2).getComponentCount()==0 && !((Pion)(getCase(i-1, j+1).getComponent(0))).getCouleur().equals(couleur)){
				getCase(i-2, j+2).setSelectionnee(true);
			}
		}
		else{
			if(i+1<TAILLE && j+1<TAILLE && getCase(i+1, j+1).getComponentCount()==0){
				getCase(i+1, j+1).setSelectionnee(true);
			}
			else if(i+2<TAILLE && j+2<TAILLE && getCase(i+2, j+2).getComponentCount()==0 && !((Pion)(getCase(i+1, j+1).getComponent(0))).getCouleur().equals(couleur)){
				getCase(i+2, j+2).setSelectionnee(true);
			}
			if(i+1<TAILLE && j-1>=0 && getCase(i+1, j-1).getComponentCount()==0){
				getCase(i+1, j-1).setSelectionnee(true);
			}
			else if(i+2<TAILLE && j-2>=0 && getCase(i+2, j-2).getComponentCount()==0 && !((Pion)(getCase(i+1, j-1).getComponent(0))).getCouleur().equals(couleur)){
				getCase(i+2, j-2).setSelectionnee(true);
			}
			
		}
	}

	public void deplacer(Case case1){
		case1.add(caseActive.getComponent(0));
		for(int k=0; k<TAILLE*TAILLE; k++){
			getCase(k).setSelectionnee(false);
		}
		if(Math.abs(getLigne(case1)-getLigne(caseActive))==2){
			int i = (getLigne(case1)+getLigne(caseActive))/2;
			int j = (getColonne(case1)+getColonne(caseActive))/2;
			getCase(i, j).removeAll();
			getCase(i, j).validate();
			getCase(i, j).repaint();
		}
		tourNoir=!tourNoir;
		caseActive.removeAll();
		caseActive.repaint();
		caseActive=null;
		case1.repaint();
		if(getLigne(case1)==0){
			Pion p=(Pion)(case1.getComponent(0));
			p.setMonte(false);
		}
		if(getLigne(case1)==TAILLE-1){
			Pion p=(Pion)(case1.getComponent(0));
			p.setMonte(true);
		}
	}

	private int getLigne(Case case1){
		int res=0;
		for(int i=0; i<TAILLE*TAILLE; i+=2){
			if(getCase(i).equals(case1)){
				res=i/TAILLE;
			}
		}
		return res;
	}

	private int getColonne(Case case1){
		int res=0;
		for(int i=0; i<TAILLE*TAILLE; i+=2){
			if(getCase(i).equals(case1)){
				res=i%TAILLE;
			}
		}
		return res;
	}
	
	


}
