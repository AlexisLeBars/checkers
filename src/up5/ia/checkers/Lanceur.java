package up5.ia.checkers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.UIManager;

public class Lanceur {
	public static JFrame f;
	public static JLabel tour;
	public static JLabel modeText;
	public static JLabel fin;
	public static JLabel nivIAText;
	public static JLabel coupures;
	public static JLabel gain;
	public static ButtonGroup nivIA;
	public static JRadioButton facile;
	public static JRadioButton moyen;
	public static JRadioButton difficile;
	public static JCheckBox data;
	public static JButton mode;
	public static JPanel panel;
	public static JPanel infos;
	public static JPanel infos2;
	
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e){}
		f = new JFrame();
		f.setLayout(new BorderLayout());
		f.setTitle("Jeu De Dames");
		f.setMinimumSize(new Dimension(700,700));
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	int height = (int) ((Component) e.getSource()).getSize().getHeight();
                int width = (int) ((Component) e.getSource()).getSize().getWidth();
                int taille = (height<width)?height:width;
                Lanceur.f.setSize(taille,taille);
                Lanceur.f.revalidate();
                
            	//Lanceur.f.setSize(new Dimension(Lanceur.f.getSize().height, Lanceur.f.getSize().height));
            }
        });
		
		final Damier damier = new Damier();
		damier.setCoupsPossibles(damier.calculerCoupsPossibles(damier.getDamier(),Couleur.BLANC));

		panel=new JPanel(new BorderLayout());
		infos=new JPanel(new FlowLayout(150));
		infos2=new JPanel(new FlowLayout(150));
		
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
		nivIAText=new JLabel("Niveau IA: ");
		facile=new JRadioButton("Facile");
		facile.setSelected(true);
		facile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Damier.niveauIA=1;
				System.out.println(Damier.niveauIA);
			}
		});
		moyen=new JRadioButton("Moyen");
		moyen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Damier.niveauIA=2;
				System.out.println(Damier.niveauIA);
			}
		});
		difficile=new JRadioButton("Difficile");
		difficile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Damier.niveauIA=3;
				System.out.println(Damier.niveauIA);
			}
		});
		nivIA=new ButtonGroup();
		nivIA.add(facile);
		nivIA.add(moyen);
		nivIA.add(difficile);
		coupures=new JLabel();
		data=new JCheckBox("infos");
		data.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Lanceur.data.isSelected())
					Lanceur.infos2.setVisible(true);
				else
					Lanceur.infos2.setVisible(false);
			}
		});
		gain=new JLabel();
		
		infos.setBorder(BorderFactory.createTitledBorder("Game Info"));
		infos.add(tour);
		infos.add(new JSeparator());
		infos.add(modeText);
		infos.add(mode);
		infos.add(new JSeparator());
		infos.add(nivIAText);
		infos.add(facile);
		infos.add(moyen);
		infos.add(difficile);
		infos.add(new JSeparator());
		infos.add(data);
		infos.add(new JSeparator());
		infos.add(fin);
		infos.add(new JSeparator());
		infos2.setBorder(BorderFactory.createTitledBorder("Game Data"));
		infos2.add(coupures);
		infos2.add(new JSeparator());
		infos2.add(gain);
		infos2.setVisible(false);
		panel.add(infos2,BorderLayout.SOUTH);
		panel.add(infos,BorderLayout.NORTH);
		panel.add(new JSeparator());
		panel.add(damier,BorderLayout.CENTER);

		f.getContentPane().add(panel);
		f.setVisible(true);
		f.pack();
	}
}
