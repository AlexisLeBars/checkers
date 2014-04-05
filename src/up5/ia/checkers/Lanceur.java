package up5.ia.checkers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

public class Lanceur {
	public static JLabel tour;
	public static JLabel modeText;
	public static JLabel fin;
	public static JButton mode;
	public static JPanel panel;
	public static JPanel infos;
	
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e){}
		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.setTitle("Jeu De Dames");
		f.setMinimumSize(new Dimension(600,600));
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Damier damier = new Damier();
		damier.setCoupsPossibles(damier.calculerCoupsPossibles(damier.getDamier(),Couleur.BLANC));

		panel=new JPanel(new BorderLayout());
		infos=new JPanel(new FlowLayout(150));
		tour =new JLabel();
		tour.setText("Tour : Blanc");
		modeText=new JLabel();
		modeText.setText("Game Mode: ");
		mode=new JButton("IA ON");
		mode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(mode.getText()=="IA ON")
					mode.setText("IA OFF");
				else
					mode.setText("IA ON");
			}
		});
		fin=new JLabel("Partie en cours");
		infos.setBorder(BorderFactory.createTitledBorder("Game Info"));
		infos.add(tour);
		infos.add(new JSeparator());
		infos.add(modeText);
		infos.add(mode);
		infos.add(new JSeparator());
		infos.add(fin);
		panel.add(infos,BorderLayout.NORTH);
		panel.add(new JSeparator());
		panel.add(damier,BorderLayout.CENTER);

		f.getContentPane().add(panel);
		f.setVisible(true);
		f.pack();
	}
}
