package up5.ia.checkers;

import java.util.ArrayList;

public class Coup {

	private Pion pionDeplace;
	private ArrayList<Case> casesIntermediaires;
	private ArrayList<Pion> pionsSupprimes;
	private Case caseFinale;
	
	Coup(Pion pionDeplace, ArrayList<Case> casesIntermediaires, ArrayList<Pion> pionsSupprimes, Case caseFinale){
		this.pionDeplace=pionDeplace;
		this.casesIntermediaires=casesIntermediaires;
		this.pionsSupprimes=pionsSupprimes;
		this.caseFinale=caseFinale;
	}
}
