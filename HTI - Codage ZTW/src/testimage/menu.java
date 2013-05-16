package testimage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import compression.CodageZTW;

/**
 * <p>
 * Title: TestImage
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("unused")
public class menu extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4371450938936625837L;

	ImageIO imIO = new ImageIO();
	String pathOri = "";
	String nomOri = "";
	JTextField oriName = new JTextField(10);
	
	String nomBitstream = "";
	String pathBitstream = "";

	int[] oriHisto = new int[256];
	int[] modifHisto = new int[256];
	int[] erreurHisto = new int[256];
	double oriEntrop = 0;
	double modifEntrop = 0;
	double erreurEntrop = 0;
	Histogramme histoGraphOri = new Histogramme(oriHisto, "originale",
			Color.blue, oriEntrop);
	Histogramme histoGraphModif = new Histogramme(modifHisto, "modifiee",
			Color.red, modifEntrop);
	Histogramme histoGraphErreur = new Histogramme(erreurHisto,
			"Erreur prediction", Color.green, erreurEntrop);

	private Image imageOri;
	private BufferedImage bufIm;
	private BufferedImage modifIm;
	private BufferedImage erreurIm;
	double[][] erreur;
			
	CadreImage oriCadre = new CadreImage(bufIm, "Image originale");
	CadreImage modifCadre = new CadreImage(modifIm, "Image modifiee");
	CadreImage erreurCadre = new CadreImage(modifIm, "Image transformee");
	TopControl parent;
	int Fw;
	int Fh;

	TitledBorder titledBorder1;
	TitledBorder titledBorder2;

	JPanel jPanelTop = new JPanel();
	JPanel jPanelFiles = new JPanel();
	JPanel jPanelMid = new JPanel();
	JPanel jPanelTransform = new JPanel();
	JPanel jPanelCodage = new JPanel();

	JButton jButtonOpenImage = new JButton();
	JButton jButtonOpenBitstream = new JButton();
	JButton jButtonQuit = new JButton();
	JButton jButHisto = new JButton();
	JButton jButtonTransformee = new JButton();
	JButton jButtonTransInverse = new JButton();
	JButton jButtonCodage = new JButton();
	JButton jButtonDecodage = new JButton();
	JButton BAfficheOri = new JButton();
	JButton BAffichErreur = new JButton();
	JButton BAffichModif = new JButton();
	JButton JButModifFen = new JButton();

	JTextField erreurName = new JTextField(10);
	JTextField Tvalue = new JTextField();
	JTextField modifName = new JTextField(10);
	JTextField fieldHeight = new JTextField(4);
	JTextField fieldWidth = new JTextField(4);

	JLabel jLabelTauxComp = new JLabel();
	JLabel jLabelTextLine1 = new JLabel();
	JLabel jLabelTextLine2 = new JLabel();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel6 = new JLabel();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel7 = new JLabel();
	JLabel jLabel8 = new JLabel();
	JLabel jLabelNivResolution = new JLabel();
	JLabel jLabelTransformee = new JLabel();
	JLabel jLabelHeight = new JLabel();
	JLabel jLabelWidth = new JLabel();

	JComboBox jComboTransformee = new JComboBox();
	JComboBox jComboNivResolution = new JComboBox();
	JComboBox jComboTauxCompression = new JComboBox();

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	GridBagLayout gridBagLayout3 = new GridBagLayout();
	GridLayout gridLayout1 = new GridLayout();

	JPanel hBoxTop1 = new JPanel();
	JPanel hBoxTop2 = new JPanel();
	Box hBoxBot = Box.createVerticalBox();
	Box vBoxCodage = Box.createHorizontalBox();
	Box vBoxTransformee = Box.createHorizontalBox();	
	Box vBoxFiles = Box.createVerticalBox();
	Box vBox = Box.createVerticalBox();
	
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout4 = new BorderLayout();

	// Fenetrage jtoto;
	// JComboBox jComboW = new JComboBox();
	// JLabel jLabel10 = new JLabel();

	public menu(TopControl parent, int w, int h) {
		try {

			this.parent = parent;
			this.Fw = w;
			this.Fh = h;
			jbInit();
			initCombo();
			PlaceFenetres.PlaceFenetre(1.3 / 2.0, 1.3 / 2.0, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {

		oriCadre.setName("oriCadre");
		modifCadre.setName("modifCadre");
		erreurCadre.setName("erreurCadre");
		
		this.setDefaultCloseOperation(3);
		this.setTitle("Compression et codage d'image");
		
		
		oriName.setEnabled(false);
		oriName.setMaximumSize(oriName.getPreferredSize());
		oriName.setDisabledTextColor(Color.black);
		erreurName.setDisabledTextColor(Color.black);
		erreurName.setMaximumSize(erreurName.getPreferredSize());
		erreurName.setEnabled(false);
		modifName.setEnabled(false);
		modifName.setDisabledTextColor(Color.black);
		modifName.setText("");
		modifName.setMaximumSize(modifName.getPreferredSize());

		jButHisto.setText("Afficher Histogramme");
		jButHisto.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButHisto_actionPerformed(e);
			}
		});
		histoGraphModif.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		histoGraphOri.getContentPane().setBackground(SystemColor.control);
		
		jLabel4.setText("Image originale : ");
		jLabel7.setRequestFocusEnabled(true);
		jLabel7.setVerifyInputWhenFocusTarget(true);
		jLabel7.setText("Image transformee : ");
		jLabel8.setText("Image reconstituee : ");
		
		BAfficheOri.setText("Afficher");
		BAfficheOri.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BAfficheOri_actionPerformed(e);
			}
		});
		BAffichErreur.setText("Afficher");
		BAffichErreur.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BAffichErreur_actionPerformed(e);
			}
		});
		BAffichModif.setText("Afficher");
		BAffichModif.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BAffichModif_actionPerformed(e);
			}
		});
		JButModifFen.setText("Modifier");
		JButModifFen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JButModifFen_actionPerformed(e);
			}
		});
	
		jComboTransformee.setMaximumSize(new Dimension(300, 50));
		jComboNivResolution.setMaximumSize(new Dimension(70, 50));
		jComboTauxCompression.setMaximumSize(new Dimension(70, 50));
		
		jLabel6.setText("Transformee :");
		jLabelNivResolution.setText("Niveaux de resolution :");
		jLabelTransformee.setText("Type de transformee :");
		jLabelTauxComp.setText("Taux de compression : ");
		jLabelNivResolution.setMaximumSize(jLabelNivResolution.getPreferredSize());
		jLabelTransformee.setMaximumSize(jLabelTransformee.getPreferredSize());
		jLabelTauxComp.setMaximumSize(jLabelTauxComp.getPreferredSize());
		jLabelHeight.setText("H = ");
		jLabelWidth.setText("W = ");
		jLabelTextLine1.setText("___________________________________________________________________________________");
		jLabelTextLine2.setText("___________________________________________________________________________________");
		
		jButtonQuit.setText("Quitter");
		jButtonOpenImage.setText("Ouvrir image...");
		jButtonOpenBitstream.setText("Ouvrir fichier code...");
		jButtonTransformee.setText("Appliquer transformee");
		jButtonTransInverse.setText("Appliquer inverse");
		jButtonCodage.setText("Appliquer le codage");
		jButtonDecodage.setText("Appliquer le decodage");
		jButtonOpenImage.setMaximumSize(jButtonOpenImage.getPreferredSize());
		jButtonOpenBitstream.setMaximumSize(jButtonOpenImage.getPreferredSize());
		jButHisto.setMaximumSize(jButHisto.getPreferredSize());
		jButtonCodage.setMaximumSize(jButtonCodage.getPreferredSize());
		jButtonQuit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}});
		jButtonTransformee.addActionListener(
			new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonTransformee_actionPerformed(e);
			}});
		jButtonCodage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonCodage_actionPerformed(e);
			}});
		jButtonOpenImage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton1_actionPerformed(e);
			}});
		jButtonOpenBitstream.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonOpenBitstream_actionPerformed(e);
			}});
		jButtonTransInverse.addActionListener(
				new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jButtonTransInverse_actionPerformed(e);
				}});
		jButtonDecodage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonDecodage_actionPerformed(e);
				}});
		
		fieldHeight.setEnabled(true);
		fieldWidth.setEnabled(true);
		fieldHeight.setMaximumSize(fieldHeight.getPreferredSize());
		fieldWidth.setMaximumSize(fieldWidth.getPreferredSize());
		
		/**
		 * Partie files
		 */
		Box hBoxF1 = Box.createHorizontalBox();
		Box hBoxF2 = Box.createHorizontalBox();
		Box hBoxF3 = Box.createHorizontalBox();
		hBoxF1.add(jLabel4, null);
		hBoxF1.add(oriName, null);
		hBoxF1.add(BAfficheOri, null);
		hBoxF2.add(jLabel7, null);
		hBoxF2.add(erreurName, null);
		hBoxF2.add(BAffichErreur, null);
		hBoxF3.add(jLabel8, null);
		hBoxF3.add(modifName, null);
		hBoxF3.add(BAffichModif, null);
		Box hBoxS1 = Box.createHorizontalBox();
		
		hBoxS1.add(Box.createVerticalStrut(10));
		hBoxS1.add(jLabelHeight);
		hBoxS1.add(fieldHeight);
		hBoxS1.add(jLabelWidth);
		hBoxS1.add(fieldWidth);
		hBoxS1.add(Box.createVerticalStrut(10));
		
		vBoxFiles.add(hBoxS1);
		vBoxFiles.add(hBoxF1);
		vBoxFiles.add(hBoxF2);
		vBoxFiles.add(hBoxF3);
		/**
		 * Transformeee et codage
		 */
		Box hBoxT1 = Box.createHorizontalBox();
		hBoxT1.add(Box.createVerticalStrut(10));
		hBoxT1.add(jLabelTransformee);
		hBoxT1.add(jComboTransformee);
		hBoxT1.add(Box.createVerticalStrut(10));
		hBoxT1.add(jLabelNivResolution);
		hBoxT1.add(jComboNivResolution);
		hBoxT1.add(Box.createVerticalStrut(10));	
		hBoxT1.add(jLabelTauxComp);
		hBoxT1.add(jComboTauxCompression);
		hBoxT1.add(Box.createVerticalStrut(10));
		


		
		//vBoxTransformee.add(hBoxT1);
		//vBoxTransformee.add(hBoxT2);
		//vBoxCodage.add(hBoxC1);
		//vBoxCodage.add(hBoxC2);
		/**
		 * Bot et Top
		 */
		hBoxTop1.add(Box.createVerticalStrut(10));
		hBoxTop1.add(jButtonOpenImage);
		hBoxTop1.add(jButtonTransformee);
		hBoxTop1.add(jButtonCodage);
		hBoxTop1.add(jButHisto);
		hBoxTop1.add(Box.createVerticalStrut(10));
		hBoxTop1.setMaximumSize(hBoxTop1.getPreferredSize());
		
		hBoxTop2.add(Box.createVerticalStrut(10));
		hBoxTop2.add(jButtonOpenBitstream);
		hBoxTop2.add(jButtonDecodage);
		hBoxTop2.add(jButtonTransInverse);
		hBoxTop2.add(jButHisto);
		hBoxTop2.add(Box.createVerticalStrut(10));
		hBoxTop2.setMaximumSize(hBoxTop2.getPreferredSize());
		
		hBoxBot.add(jButtonQuit);
		/**
		 * Assemblage
		 */
		vBox.add(hBoxTop1);
		vBox.add(hBoxTop2);
		vBox.add(Box.createGlue());
		vBox.add(vBoxFiles);
		vBox.add(Box.createGlue());
		vBox.add(hBoxT1);
		vBox.add(Box.createGlue());
		vBox.add(hBoxBot);
		this.getContentPane().add(vBox);
	}

	void jButtonTransInverse_actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int niv_resolution = 1;
		String transformee;
		try {
			try {
				niv_resolution = jComboNivResolution.getSelectedIndex() + 1;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez des valeurs correctes", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboNivResolution.requestFocus();
				return;
			}
			try {
				transformee = (String) jComboTransformee.getSelectedItem();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez des valeurs correctes", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboTransformee.requestFocus();
				return;
			}

		} catch (NullPointerException ex2) {
			return;
		}
		doTransInverse(erreur, transformee, niv_resolution);
	}
	void jButtonDecodage_actionPerformed(ActionEvent e) {
		int niv_resolution = 1;
		int req_size = 1000;
		int height =0, width =0;
		try {
			try {
				niv_resolution = jComboNivResolution.getSelectedIndex() + 1;
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez une valeur correcte", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboNivResolution.requestFocus();
				return;
			}
			try {
				height = Integer.parseInt(fieldHeight.getText());
				width = Integer.parseInt(fieldWidth.getText());
				if(height <= 0 || width <= 0){
					JOptionPane.showMessageDialog(null,
							"Entrez des dimensions positives", "Erreur",
							JOptionPane.ERROR_MESSAGE);
					;
					jComboNivResolution.requestFocus();
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez une valeur correcte", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboNivResolution.requestFocus();
				return;
			}
		if(pathBitstream.equals("")){
			JOptionPane.showMessageDialog(null,
					"Ouvrez d'abord un fichier.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			try {
				throw new Exception("Ouvrez d'abord un fichier.");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		} catch (NullPointerException ex2) {
			return;
		}
		doDecodage(pathBitstream, height, width, niv_resolution);
	}


	void jButtonOpenBitstream_actionPerformed(ActionEvent e) {
		JFileChooser Ffile2 = new JFileChooser("./Images/");

		// Filtrage type de fichier a selectionner/
		MonFiltre mfi2 = new MonFiltre(new String[] { "bitstream" },
				"les fichiers code (*.bitstream)");
		Ffile2.addChoosableFileFilter(mfi2);
		Ffile2.setFileFilter(mfi2);

		int retour = Ffile2.showOpenDialog(null);

		if (retour == JFileChooser.APPROVE_OPTION) {
			nomBitstream = Ffile2.getSelectedFile().getAbsolutePath();
			pathBitstream = Ffile2.getSelectedFile().getName();
		} else {
			nomBitstream = "";
			pathBitstream = "";
		}
	}

	/**
	 * Bouton d'ouverture de fichier
	 * 
	 * @param e
	 */
	void jButton1_actionPerformed(ActionEvent e) {

		JFileChooser Ffile = new JFileChooser("./Images/");

		// Filtrage type de fichier a selectionner/
		MonFiltre mfi = new MonFiltre(new String[] { "gif", "jpg" },
				"les fichiers image (*.gif, *.jpg)");
		Ffile.addChoosableFileFilter(mfi);
		Ffile.setFileFilter(mfi);

		int retour = Ffile.showOpenDialog(null);

		if (retour == JFileChooser.APPROVE_OPTION) {
			ouvrirIm(Ffile.getSelectedFile().getAbsolutePath(), Ffile
					.getSelectedFile().getName());
			parent.path = Ffile.getSelectedFile().getAbsolutePath();
			parent.nom = Ffile.getSelectedFile().getName();
			/*
			 * this.pathOri=Ffile.getSelectedFile().getAbsolutePath();
			 * this.nomOri=Ffile.getSelectedFile().getName();
			 * oriName.setText(this.nomOri);
			 * System.out.println("Fichier :"+this.
			 * nomOri+"\nChemin : "+this.pathOri+"\n");
			 * 
			 * erreurName.setText(""); modifName.setText("");
			 */
		} else {
			ouvrirIm("", "");
			parent.path = "";
			parent.nom = "";
			/*
			 * System.out.println("Aucune selection\n"); return;
			 */
		}
		/*
		 * //Image d'origine this.imageOri=imIO.OuvrirImage(this.pathOri);
		 * 
		 * 
		 * //Image bufferisee
		 * this.bufIm=TraitImage.toBufferedImage(this.imageOri);
		 */
	}

	void ouvrirIm(String path, String nom) {
		if ((path != "") && (path != null)) {
			this.pathOri = path;
			this.nomOri = nom;
			oriName.setText(this.nomOri);
			System.out.println("Fichier :" + this.nomOri + "\nChemin : "
					+ this.pathOri + "\n");

			erreurName.setText("");
			modifName.setText("");
		} else {
			System.out.println("Aucune selection\n");
			return;
		}
		// Image d'origine
		this.imageOri = imIO.OuvrirImage(this.pathOri);

		// Image bufferisee
		this.bufIm = TraitImage.toBufferedImage(this.imageOri);
		fieldHeight.setText(""+bufIm.getHeight());
		fieldWidth.setText(""+bufIm.getWidth());

	}

	void afficherIm(BufferedImage im, CadreImage zone, String titre) {

		if (zone.getName() == "oriCadre") {
			oriCadre.dispose();
			oriCadre = new CadreImage(im, titre);
			double delta = oriCadre.getSize().getWidth() / 2;
			oriCadre.setLocation(
					(int) (oriCadre.getLocation().getX() - delta - 70),
					(int) (oriCadre.getLocation().getY() - 70));
			oriCadre.show();
		} else if (zone.getName() == "modifCadre") {
			modifCadre.dispose();
			modifCadre = new CadreImage(im, titre);
			double delta = modifCadre.getSize().getWidth() / 2;
			modifCadre
					.setLocation(
							(int) (modifCadre.getLocation().getX() + delta + 70),
							(int) (modifCadre.getLocation().getY() - 70));
			modifCadre.show();
		} else if (zone.getName() == "erreurCadre") {
			erreurCadre.dispose();
			erreurCadre = new CadreImage(im, titre);
			double delta = erreurCadre.getSize().getWidth() / 2;
			erreurCadre.setLocation((int) (erreurCadre.getLocation().getX()),
					(int) (erreurCadre.getLocation().getY() + 70));
			erreurCadre.show();
		}

		if (modifName.getText().length() > 0)
			modifCadre.toFront();
		if (erreurName.getText().length() > 0)
			erreurCadre.toFront();
		if (oriName.getText().length() > 0)
			oriCadre.toFront();

	}

	void jButtonTransformee_actionPerformed(ActionEvent e) {
		int niv_resolution = 1;
		String transformee;
		try {
			try {
				niv_resolution = jComboNivResolution.getSelectedIndex() + 1;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez des valeurs correctes", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboNivResolution.requestFocus();
				return;
			}
			try {
				transformee = (String) jComboTransformee.getSelectedItem();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez des valeurs correctes", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboTransformee.requestFocus();
				return;
			}

		} catch (NullPointerException ex2) {
			return;
		}
		doTransformee(this.bufIm, transformee, niv_resolution);
	}

	/**
	 * 
	 * @param e
	 */
	void jButtonCodage_actionPerformed(ActionEvent e) {
		int niv_resolution = 1;
		int req_size = 1000;
		try {
			try {
				niv_resolution = jComboNivResolution.getSelectedIndex() + 1;
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez une valeur correcte", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboNivResolution.requestFocus();
				return;
			}
			try {
				File f1 = new File(pathOri);
				req_size = (int) (f1.length() / (1000 * Integer.parseInt((String) jComboTauxCompression.getSelectedItem())));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Entrez une valeur correcte", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				;
				jComboTauxCompression.requestFocus();
				return;
			}

		} catch (NullPointerException ex2) {
			return;
		}
		doCodage(erreur, niv_resolution, req_size);
	}

	/**
	 * Codage ztw
	 * @param Itrans
	 * @param niv_resolution
	 * @param required_size
	 */
	public void doCodage(double[][] err, int niv_resolution,
			int required_size) {
		pathBitstream = "./Images/"+ nomOri + ".bitstream";
		/**
		 * Image to double
		 */
		//double[][] transIm = new double[Itrans.getHeight()][Itrans.getWidth()];
		//transIm = TraitImage.getPixelTab(Itrans);

		CodageZTW ztw = new CodageZTW();

		try {
			ztw.ztw_code(err, err.length, err[0].length,
					niv_resolution, required_size, pathBitstream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Decodage ztw
	 * @param path
	 * @param niv_resolution
	 */
	void doDecodage(String path, int height, int width, int niv_resolution) {
		
		erreur = new double[height][width];
		CodageZTW ztw = new CodageZTW();

		try {
			ztw.ztw_decode(erreur, erreur.length, erreur[0].length, niv_resolution, pathBitstream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * double to Image
		 */
		
		erreurIm = TraitImage.setPixelTab(erreur);
		
	}
	
	/**
	 * Transformee directe
	 * @param Iori
	 * @param valeur
	 */
	public void doTransformee(BufferedImage Iori, String transformee,
			int niv_resolution) {
		// Recupere pixels de l'image dans un tableau

		// int[][] donnee=TraitImage.getPixelTab(Iori);

		modifName.setText("");
		modifCadre.dispose();
		erreurName.setText("");
		erreurCadre.dispose();

		// ################### HAAR #######################
		// modifIm=TraitImage.setPixelTab(donnee);
		if (transformee.equals("Transformee en ondelettes de Haar")) {
			System.out.println("Debut transformee en ondelettes de Haar");
			System.out.println("Niveaux de resolution : " + niv_resolution);

			// System.out.println("Centrage de l'image");
			// double moyenne = TraitImage.calculMoyenne(Iori);
			double[][] donnee = TraitImage.centrageImage(Iori, 0);
			// System.out.println("Moyenne ="+moyenne);

			// TraitImage.getCoeff(donnee,coeff);

			// System.out.println("Reservation memoire y="+donnee.length+" x="+donnee[0].length);
			erreur = new double[donnee.length][donnee[0].length];
			// System.out.println("Calcul prediction");

			/* MODIF TITUS */
			// for(int i=0;i<coeff.length;i++)
			// for(int j=0;j<coeff[0].length;j++)
			// System.out.println("coeff "+coeff[i][j]);
			// coeff[0][0] = 0.0; coeff[0][1] = 0.5; coeff[1][0] = 0.5;
			/* FIN MODIF TITUS */

			// TraitImage.predictionAR2d(donnee,erreur, coeff,
			// Integer.parseInt(Tvalue.getText()));
			/**
			 * Transformee
			 */
			TraitImage.haar2D_multi(donnee, erreur, niv_resolution);

			// System.out.println("Reservation memoire");
			
			/*
			 * CodageZTW ztw = new CodageZTW(); try { ztw.ztw_decode(erreur,
			 * 512, 512, niv_resolution, nomOri + ".bitstream"); } catch
			 * (Exception e){//IOException e) { //TODO Auto-generated catch
			 * block e.printStackTrace(); }
			 */

			// Generer l'image d'erreur
			double[][] erreurAbs = new double[erreur.length][erreur[0].length];
			for (int i = 0; i < erreurAbs.length; i++)
				for (int j = 0; j < erreurAbs[0].length; j++) {
					/*
					 * if (erreur[i][j]<0) System.out.println(erreur[i][j]+" ");
					 */
					erreurAbs[i][j] = Math.abs(erreur[i][j]);

				}
			erreurIm = TraitImage.setPixelTab(erreurAbs);
		}
		// #############################################################
		
		erreurName.setText("E" + oriName.getText());
	}
	
	/**
	 * Transformee inverse
	 * @param Ierreur
	 * @param transformee
	 * @param niv_resolution
	 */
	public void doTransInverse(double[][] err, String transformee,
			int niv_resolution){
		
		modifName.setText("");
		modifCadre.dispose();

		double[][] x_rec = new double[err.length][err[0].length];

		if(transformee.equals("Transformee en ondelettes de Haar")){
			TraitImage.haar2D_multi_inv(err, x_rec, niv_resolution);
		}
		
		modifIm = TraitImage.setPixelTab(x_rec);
		
		String nom = "M" + oriName.getText();
		modifName.setText(nom);
	}
	// Initialise champs de la comboBox
	public void initCombo() {
		/**
		 * comboBox : choix de la transformee
		 */
		/*
		 * jComboTransformee.addItem("Aucun");
		 * jComboTransformee.addItem("Moyenneur");
		 */
		jComboTransformee.addItem("Transformee en ondelettes de Haar");
		jComboTransformee.setSelectedIndex(0);
		/**
		 * comboBox : choix du nombre de niveaux de resolution de la transformee
		 */
		for (int i = 1; i <= 10; i++) {
			jComboNivResolution.addItem(i + "");
		}
		jComboNivResolution.setSelectedIndex(3);
		/**
		 * comboBox : choix du taux de compression lors du codage
		 */
		for (int i = 1; i <= 5; i++) {
			jComboTauxCompression.addItem(""+ i);
		}
		for (int i = 10; i <= 50; i += 10) {
			jComboTauxCompression.addItem("" + i);
		}
		jComboTauxCompression.setSelectedIndex(2);
	}

	void jButton2_actionPerformed(ActionEvent e) {
		System.exit(1);
	}

	void jButHisto_actionPerformed(ActionEvent e) {

		// Affiche histo
		if (this.oriName.getText().length() != 0) {
			histoGraphOri.dispose();
			histoGraphOri = new Histogramme(oriHisto, "originale", Color.blue,
					oriEntrop);

			double delta = histoGraphOri.getSize().getWidth() / 2;
			this.oriHisto = TraitImage.calculHisto(bufIm);
			this.oriEntrop = TraitImage.calculEntropie(this.oriHisto,
					bufIm.getHeight() * bufIm.getWidth());
			this.histoGraphOri.valeurHisto = this.oriHisto;
			this.histoGraphOri.entropie = this.oriEntrop;

			histoGraphOri.show();
			histoGraphOri.setLocation((int) (histoGraphOri.getLocation().getX()
					- delta - 60),
					(int) (histoGraphOri.getLocation().getY() - 125));
		}

		if (this.modifName.getText().length() != 0) {
			histoGraphModif.dispose();
			histoGraphModif = new Histogramme(modifHisto, "modifiee",
					Color.red, modifEntrop);

			double delta = histoGraphModif.getSize().getWidth() / 2;
			this.modifHisto = TraitImage.calculHisto(modifIm);
			this.modifEntrop = TraitImage.calculEntropie(this.modifHisto,
					modifIm.getHeight() * modifIm.getWidth());
			histoGraphModif.valeurHisto = this.modifHisto;
			this.histoGraphModif.entropie = this.modifEntrop;

			histoGraphModif.show();
			histoGraphModif.setLocation((int) (histoGraphModif.getLocation()
					.getX() + delta + 60), (int) (histoGraphModif.getLocation()
					.getY() - 125));
		}

		if (this.erreurName.getText().length() != 0) {
			histoGraphErreur.dispose();
			histoGraphErreur = new Histogramme(erreurHisto, "erreur",
					Color.green, erreurEntrop);

			double delta = histoGraphErreur.getSize().getWidth() / 2;
			this.erreurHisto = TraitImage.calculHisto(erreurIm);
			this.erreurEntrop = TraitImage.calculEntropie(this.erreurHisto,
					erreurIm.getHeight() * erreurIm.getWidth());
			histoGraphErreur.valeurHisto = this.erreurHisto;
			this.histoGraphErreur.entropie = this.erreurEntrop;

			histoGraphErreur.show();
			histoGraphErreur.setLocation(
					(int) (histoGraphErreur.getLocation().getX()),
					(int) (histoGraphErreur.getLocation().getY() + 125));
		}

	}

	void BAfficheOri_actionPerformed(ActionEvent e) {
		if (this.oriName.getText().length() > 0) {
			oriCadre.setName("oriCadre");
			afficherIm(bufIm, oriCadre, "Image originale : " + this.nomOri);
		}
	}

	void BAffichErreur_actionPerformed(ActionEvent e) {
		if (this.erreurName.getText().length() > 0) {
			erreurCadre.setName("erreurCadre");
			afficherIm(erreurIm, erreurCadre, "Erreur prediction : "
					+ this.erreurName.getText());
		}

	}

	void BAffichModif_actionPerformed(ActionEvent e) {
		if (this.modifName.getText().length() > 0) {
			modifCadre.setName("modifCadre");
			afficherIm(modifIm, modifCadre, "Image reconstituee : "
					+ this.modifName.getText());
		}
	}


}
