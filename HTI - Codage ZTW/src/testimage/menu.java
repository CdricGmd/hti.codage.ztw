package testimage;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;



/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

@SuppressWarnings("unused")
public class menu extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4371450938936625837L;
	JPanel jPanel1 = new JPanel();
	JButton jButton1 = new JButton();

	ImageIO imIO=new ImageIO();
	String pathOri="";
	String nomOri="";
	JPanel jPanel2 = new JPanel();
	JTextField oriName = new JTextField();


	int[] oriHisto = new int[256];
	int[] modifHisto = new int[256];
	int[] erreurHisto = new int[256];
	double oriEntrop = 0;
	double modifEntrop = 0;
	double erreurEntrop = 0;
	Histogramme histoGraphOri = new Histogramme(oriHisto,"originale",Color.blue,oriEntrop);
	Histogramme histoGraphModif = new Histogramme(modifHisto,"modifiee",Color.red,modifEntrop);
	Histogramme histoGraphErreur = new Histogramme(erreurHisto,"Erreure prediction",Color.green,erreurEntrop);

	private Image imageOri;
	private BufferedImage bufIm;
	private BufferedImage modifIm;
	private BufferedImage erreurIm;
	CadreImage oriCadre = new CadreImage(bufIm,"Image originale");
	CadreImage modifCadre = new CadreImage(modifIm,"Image modifiee");
	CadreImage erreurCadre = new CadreImage(modifIm,"Erreure de prediction");
	TopControl parent;
	int Fw;
	int Fh;

	TitledBorder titledBorder1;
	TitledBorder titledBorder2;
	JTextField erreurName = new JTextField();
	JPanel jPanel4 = new JPanel();
	JButton jButtonTraitement = new JButton();
	JTextField Tvalue = new JTextField();
	JLabel jLabel1 = new JLabel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel5 = new JLabel();
	JComboBox ComboTraitement = new JComboBox();
	JLabel jLabel6 = new JLabel();
	JPanel jPanel5 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout4 = new BorderLayout();
	JButton jButton2 = new JButton();
	JButton jButHisto = new JButton();

	Fenetrage jtoto;

	GridLayout gridLayout1 = new GridLayout();
	JTextField modifName = new JTextField();
	JLabel jLabel4 = new JLabel();
	JLabel jLabel7 = new JLabel();
	JLabel jLabel8 = new JLabel();
	JButton BAfficheOri = new JButton();
	JButton BAffichErreur = new JButton();
	JButton BAffichModif = new JButton();
	JLabel jLabel9 = new JLabel();
	JComboBox jComboW = new JComboBox();
	JLabel jLabel10 = new JLabel();
	JComboBox jComboH = new JComboBox();
	JButton JButModifFen = new JButton();




	public menu(TopControl parent, int w,int h) {
		try {

			this.parent=parent;
			this.Fw=w;
			this.Fh=h;
			jbInit();
			initCombo();
			PlaceFenetres.PlaceFenetre(1.7/2.0,1.9/2.0,this);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}





	private void jbInit() throws Exception {

		oriCadre.setName("oriCadre");
		modifCadre.setName("modifCadre");
		erreurCadre.setName("erreurCadre");

		titledBorder1 = new TitledBorder("");
		titledBorder2 = new TitledBorder("");
		jtoto = new Fenetrage(Fw,Fh);
		this.getContentPane().setLayout(borderLayout4);
		jButton1.setText("Ouvrir image...");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton1_actionPerformed(e);
			}
		});
		this.setDefaultCloseOperation(3);
		this.setTitle("Ouverture Image");
		oriName.setEnabled(false);
		oriName.setMaximumSize(new Dimension(200, 21));
		oriName.setMinimumSize(new Dimension(200, 21));
		oriName.setPreferredSize(new Dimension(300, 21));
		oriName.setDisabledTextColor(Color.black);
		oriName.setHorizontalAlignment(SwingConstants.CENTER);

		//Fenetrage
		jtoto.setBorder(BorderFactory.createLoweredBevelBorder());
		jtoto.setSize(Fw*10,Fh*10);

		erreurName.setHorizontalAlignment(SwingConstants.CENTER);
		erreurName.setDisabledTextColor(Color.black);
		erreurName.setPreferredSize(new Dimension(300, 21));
		erreurName.setMinimumSize(new Dimension(200, 21));
		erreurName.setMaximumSize(new Dimension(200, 21));
		erreurName.setEnabled(false);
		jButtonTraitement.setText("Appliquer traitement");
		jButtonTraitement.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonTraitement_actionPerformed(e);
			}
		});
		jPanel4.setLayout(gridBagLayout1);
		jLabel1.setText("Pas de quantification :");
		Tvalue.setText("1");
		Tvalue.setHorizontalAlignment(SwingConstants.CENTER);

		jPanel1.setLayout(gridBagLayout2);
		jLabel2.setText("_________________________________________________________");
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel3.setText("_________________________________________________________");

		jLabel5.setText("Fenetrage causal :");
		jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel6.setText("Traitement :");
		jPanel5.setLayout(borderLayout1);
		jButton2.setText("Quitter");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});
		jButHisto.setText("Afficher Histogramme");
		jButHisto.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButHisto_actionPerformed(e);
			}
		});
		histoGraphModif.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		histoGraphOri.getContentPane().setBackground(SystemColor.control);
		jPanel2.setLayout(gridLayout1);
		gridLayout1.setColumns(3);
		gridLayout1.setHgap(5);
		gridLayout1.setRows(3);
		gridLayout1.setVgap(5);
		modifName.setEnabled(false);
		modifName.setDisabledTextColor(Color.black);
		modifName.setText("");
		modifName.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel4.setText("Image originale :");
		jLabel7.setRequestFocusEnabled(true);
		jLabel7.setVerifyInputWhenFocusTarget(true);
		jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel7.setText("Erreur de prediction :");
		jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel8.setText("Image reconstituee :");
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
		jLabel9.setText("Largeur :");
		jLabel10.setText("Hauteur :");
		JButModifFen.setText("Modifier");
		JButModifFen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButModifFen_actionPerformed(e);
			}
		});
		jPanel2.add(jLabel4, null);
		jPanel2.add(oriName, null);
		jPanel2.add(BAfficheOri, null);
		jPanel2.add(jLabel7, null);
		jPanel2.add(erreurName, null);
		jPanel2.add(BAffichErreur, null);
		jPanel2.add(jLabel8, null);
		jPanel2.add(modifName, null);
		jPanel2.add(BAffichModif, null);
		jPanel4.add(jLabel3, new GridBagConstraints(0, 1, 8, 1, 0.0, 0.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 9, 9, 10), 62, 0));
		jPanel4.add(Tvalue, new GridBagConstraints(3, 2, 4, 1, 1.0, 0.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 113), 79, 0));
		jPanel4.add(jLabel9, new GridBagConstraints(2, 4, 1, 2, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 11), 0, 0));
		jPanel4.add(jComboW, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 3), 29, 0));
		jPanel4.add(jLabel10, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 11, 2, 15), 0, 0));
		jPanel4.add(jComboH, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 2, 4), 28, 0));
		jPanel4.add(JButModifFen, new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 9, 0, 0), 0, 0));
		jPanel4.add(jtoto, new GridBagConstraints(3, 5, 2, 2, 0.0, 0.0
				,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(15, 3, 8, 0), 76, 13));
		jPanel4.add(jButtonTraitement,   new GridBagConstraints(2, 7, 3, 1, 0.0, 0.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(14, 0, 0, 6), 40, 0));
		jPanel4.add(jLabel1,                         new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
				,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 1, 0, 0), 0, 0));
		jPanel4.add(jLabel5,                   new GridBagConstraints(0, 4, 2, 2, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 12, 0));
		jPanel4.add(ComboTraitement,                               new GridBagConstraints(1, 3, 4, 1, 0.0, 0.0
				,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(21, 2, 22, 39), 0, 0));
		jPanel4.add(jLabel6,                     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(15, 83, 12, 2), 37, 0));
		jPanel4.add(jButton2,             new GridBagConstraints(0, 8, 5, 1, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(7, 172, 13, 0), 0, 0));
		jPanel4.add(jPanel2,                                              new GridBagConstraints(0, 0, 9, 1, 0.0, 0.0
				,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 1, 0, 16), -436, -12));
		jPanel1.add(jPanel5,                         new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel5.add(jPanel4, BorderLayout.CENTER);

		//****************
		/* jPanel4.add(jtoto,    new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 321, 29)); */
            //**************************

		jPanel1.add(jButton1,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 111, 5, 25), 97, -1));
		jPanel1.add(jButHisto,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(2, 37, 6, 88), 27, -2));
		jPanel1.add(jLabel2,     new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
				,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(5, 0, 11, 0), 0, 0));
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);




	}


	/**
	 * Bouton d'ouverture de fichier
	 * @param e
	 */
	void jButton1_actionPerformed(ActionEvent e) {

		JFileChooser Ffile=new JFileChooser("./Images");

		//Filtrage type de fichier a selectionner
		MonFiltre mfi = new MonFiltre(
				new String[]{"gif","jpg"},
				"les fichiers image (*.gif, *.jpg)");
		Ffile.addChoosableFileFilter(mfi);

		int retour=Ffile.showOpenDialog(null);

		if (retour == JFileChooser.APPROVE_OPTION)
		{
			ouvrirIm(Ffile.getSelectedFile().getAbsolutePath(),Ffile.getSelectedFile().getName());
			parent.path=Ffile.getSelectedFile().getAbsolutePath();
			parent.nom=Ffile.getSelectedFile().getName();
			/* this.pathOri=Ffile.getSelectedFile().getAbsolutePath();
        this.nomOri=Ffile.getSelectedFile().getName();
        oriName.setText(this.nomOri);
        System.out.println("Fichier :"+this.nomOri+"\nChemin : "+this.pathOri+"\n");

        erreurName.setText("");
        modifName.setText("");*/
		}
		else {
			ouvrirIm("","");
			parent.path="";
			parent.nom="";
			/*
        System.out.println("Aucune selection\n");
        return;*/
		}
		/*
    //Image d'origine
    this.imageOri=imIO.OuvrirImage(this.pathOri);


    //Image bufferisee
    this.bufIm=TraitImage.toBufferedImage(this.imageOri);



		 */
	}

	void ouvrirIm(String path,String nom){
		if ((path != "")&&(path!=null)) {
			this.pathOri = path;
			this.nomOri = nom;
			oriName.setText(this.nomOri);
			System.out.println("Fichier :" + this.nomOri + "\nChemin : " +
					this.pathOri + "\n");

			erreurName.setText("");
			modifName.setText("");
		}
		else {
			System.out.println("Aucune selection\n");
			return;
		}
		//Image d'origine
		this.imageOri=imIO.OuvrirImage(this.pathOri);


		//Image bufferisee
		this.bufIm=TraitImage.toBufferedImage(this.imageOri);


	}



	void afficherIm(BufferedImage im,CadreImage zone,String titre){

		if(zone.getName()=="oriCadre")
		{
			oriCadre.dispose();
			oriCadre = new CadreImage(im,titre);
			double delta= oriCadre.getSize().getWidth()/2;
			oriCadre.setLocation((int)(oriCadre.getLocation().getX()-delta-70),(int)(oriCadre.getLocation().getY()-70));
			oriCadre.show();
		}
		else  if(zone.getName()=="modifCadre")
		{
			modifCadre.dispose();
			modifCadre = new CadreImage(im,titre);
			double delta= modifCadre.getSize().getWidth()/2;
			modifCadre.setLocation((int)(modifCadre.getLocation().getX()+delta+70),(int)(modifCadre.getLocation().getY()-70));
			modifCadre.show();
		}
		else  if(zone.getName()=="erreurCadre")
		{
			erreurCadre.dispose();
			erreurCadre = new CadreImage(im,titre);
			double delta= erreurCadre.getSize().getWidth()/2;
			erreurCadre.setLocation((int)(erreurCadre.getLocation().getX()),(int)(erreurCadre.getLocation().getY()+70));
			erreurCadre.show();
		}

		if(modifName.getText().length()>0)
			modifCadre.toFront();
		if(erreurName.getText().length()>0)
			erreurCadre.toFront();
		if(oriName.getText().length()>0)
			oriCadre.toFront();

	}



	void jButtonTraitement_actionPerformed(ActionEvent e) {
		int valIntens=0;
		try{
			try{
				valIntens=Integer.parseInt(Tvalue.getText());
				if( (valIntens<-255) ||(valIntens>255) )
				{
					JOptionPane.showMessageDialog(null,"Entrez une valeur entre -255 et 255","Erreur",JOptionPane.ERROR_MESSAGE);;
					Tvalue.requestFocus();
					return;
				}
			}
			catch(NumberFormatException ex){
				JOptionPane.showMessageDialog(null,"Entrez une valeur entre -255 et 255","Erreur",JOptionPane.ERROR_MESSAGE);;
				Tvalue.requestFocus();
				return;
			}

			doTraitement(this.bufIm,valIntens);
		}
		catch(NullPointerException ex2){
			return;
		}
	}


	public void doTraitement(BufferedImage Iori,int valeur){
		//Recupere pixels de l'image dans un tableau




		//int[][] donnee=TraitImage.getPixelTab(Iori);

		modifName.setText("");
		modifCadre.dispose();
		erreurName.setText("");
		erreurCadre.dispose();

		//####################Extraction des coefficient##################
		//System.out.println("Extraction des coefficients");
		double[][] coeff = new double[Fw][Fh];


		for(int i =0;i<Fw;i++)
			for(int j=0;j<Fh;j++)
			{   if ((i<Fw-1)||(j<Fh-1))
			{
				JCheckBox temp=(JCheckBox)jtoto.getComponent(i+j*Fw);
				if(temp.isSelected())
					coeff[i][j]=1;
				else
					coeff[i][j]=0;
			}
			//coeff[i][k]=1;
			}

		coeff[Fw-1][Fh-1]=0;
		/*
      int somme=0;
      for(int i=0;i<Fw;i++)
       for(int j=0;j<Fh;j++)
        somme+=coeff[i][j];

      if (somme==0)
        somme=1;

       for(int i=0;i<Fw;i++)
       	for(int j=0;j<Fh;j++)
          coeff[i][j]/=somme;
		 */

		//################### Methode 32x32 #######################
		if(ComboTraitement.getSelectedItem()=="Prediction par blocs")
		{
			//System.out.println("Prediction par blocs");
			double[][] donnee = TraitImage.getPixelTab(Iori);
			//System.out.println("Reservation memoire y="+donnee.length+" x="+donnee[0].length);
			double[][] erreur = new double[donnee.length][donnee[0].length];
			double[][] x_rec = new double[donnee.length][donnee[0].length];

			double moyenne =0;
			double[][] carre_c = new double[32][32];
			double[][] carre_nc = new double[32][32];
			double[][] carre_o = new double[32][32];
			double[][] erreur_i = new double[32][32];

			//##########################debut traitement blocs normaux 32x32
			for (int i=0;i<(donnee.length/32)-1;i++)
				for (int j=0;j<(donnee[0].length/32)-1;j++)
				{//System.out.println("Copie bloc ["+i+"]["+j+"]");
					for (int k=0;k<32;k++)
						for (int l=0;l<32;l++)
						{
							carre_nc[k][l]=donnee[i*32+k][j*32+l];
						}
					//System.out.println("Moyenne");
					moyenne = TraitImage.calculMoyenne(carre_nc);
					//System.out.println("Centrage");
					carre_c = TraitImage.centrageImage(carre_nc,moyenne);
					//System.out.println("Prediction");

					double[][] coeff_temp = new double[coeff.length][coeff[0].length];
					for (int k=0;k<coeff.length;k++)
						for (int l=0;l<coeff[0].length;l++)
							coeff_temp[k][l]=coeff[k][l];
					TraitImage.getCoeff(carre_c,coeff_temp);

					TraitImage.predictionAR2d(carre_c,erreur_i, coeff_temp, Integer.parseInt(Tvalue.getText()));
					//System.out.println("Restitution");
					TraitImage.predictionAR2d_inv(erreur_i,carre_o, coeff_temp, Integer.parseInt(Tvalue.getText()),moyenne);
					//System.out.println("Copie vers sortie");
					for (int k=0;k<32;k++)
						for (int l=0;l<32;l++)
						{
							erreur[i*32+k][j*32+l]=erreur_i[k][l];
							x_rec[i*32+k][j*32+l]=carre_o[k][l];
						}
				}

			//#################################traitement des blocs de droite
			if ((((donnee.length/32)-1)*32)!=donnee.length)
			{
				int reste = donnee.length-(((donnee.length/32)-1)*32);
				double[][] carre_c_droite = new double[reste][32];
				double[][] carre_nc_droite = new double[reste][32];
				double[][] carre_o_droite = new double[reste][32];
				double[][] erreur_i_droite = new double[reste][32];
				for (int j=0;j<(donnee[0].length/32)-1;j++)
				{
					for (int k=0;k<reste;k++)
						for (int l=0;l<32;l++)
						{
							carre_nc_droite[k][l]=donnee[(((donnee.length/32)-1)*32)+k][j*32+l];
						}
					//System.out.println("Moyenne");
					moyenne = TraitImage.calculMoyenne(carre_nc_droite);
					//System.out.println("Centrage");
					carre_c_droite = TraitImage.centrageImage(carre_nc_droite,moyenne);
					//System.out.println("Prediction");

					double[][] coeff_temp = new double[coeff.length][coeff[0].length];
					for (int k=0;k<coeff.length;k++)
						for (int l=0;l<coeff[0].length;l++)
							coeff_temp[k][l]=coeff[k][l];

					TraitImage.getCoeff(carre_c_droite,coeff_temp);

					TraitImage.predictionAR2d(carre_c_droite,erreur_i_droite, coeff_temp, Integer.parseInt(Tvalue.getText()));
					//System.out.println("Restitution");
					TraitImage.predictionAR2d_inv(erreur_i_droite,carre_o_droite, coeff_temp, Integer.parseInt(Tvalue.getText()),moyenne);
					//System.out.println("Copie vers sortie");
					for (int k=0;k<reste;k++)
						for (int l=0;l<32;l++)
						{
							erreur[(((donnee.length/32)-1)*32)+k][j*32+l]=erreur_i_droite[k][l];
							x_rec[(((donnee.length/32)-1)*32)+k][j*32+l]=carre_o_droite[k][l];
						}
				}
			}

			//##########################debut traitement blocs du bas
			if ((((donnee[0].length/32)-1)*32)!=donnee[0].length)
			{
				int reste = donnee[0].length-(((donnee[0].length/32)-1)*32);
				double[][] carre_c_bas = new double[32][reste];
				double[][] carre_nc_bas = new double[32][reste];
				double[][] carre_o_bas = new double[32][reste];
				double[][] erreur_i_bas = new double[32][reste];
				for (int i=0;i<(donnee.length/32)-1;i++)
				{
					for (int k=0;k<32;k++)
						for (int l=0;l<reste;l++)
						{
							carre_nc_bas[k][l]=donnee[i*32+k][(((donnee[0].length/32)-1)*32)+l];
						}
					//System.out.println("Moyenne");
					moyenne = TraitImage.calculMoyenne(carre_nc_bas);
					//System.out.println("Centrage");
					carre_c_bas = TraitImage.centrageImage(carre_nc_bas,moyenne);
					//System.out.println("Prediction");

					double[][] coeff_temp = new double[coeff.length][coeff[0].length];
					for (int k=0;k<coeff.length;k++)
						for (int l=0;l<coeff[0].length;l++)
							coeff_temp[k][l]=coeff[k][l];

					TraitImage.getCoeff(carre_c_bas,coeff_temp);

					TraitImage.predictionAR2d(carre_c_bas,erreur_i_bas, coeff_temp, Integer.parseInt(Tvalue.getText()));
					//System.out.println("Restitution");
					TraitImage.predictionAR2d_inv(erreur_i_bas,carre_o_bas, coeff_temp, Integer.parseInt(Tvalue.getText()),moyenne);
					//System.out.println("Copie vers sortie");
					for (int k=0;k<32;k++)
						for (int l=0;l<reste;l++)
						{
							erreur[i*32+k][(((donnee[0].length/32)-1)*32)+l]=erreur_i_bas[k][l];
							x_rec[i*32+k][(((donnee[0].length/32)-1)*32)+l]=carre_o_bas[k][l];
						}
				}
			}

			//##########################debut traitement blocs du bas a droite
			if ((((donnee[0].length/32)-1)*32)!=donnee[0].length)
				if ((((donnee.length/32)-1)*32)!=donnee.length)
				{
					int reste_x = donnee.length-(((donnee.length/32)-1)*32);
					int reste_y = donnee[0].length-(((donnee[0].length/32)-1)*32);
					double[][] carre_c_coin = new double[reste_x][reste_y];
					double[][] carre_nc_coin = new double[reste_x][reste_y];
					double[][] carre_o_coin = new double[reste_x][reste_y];
					double[][] erreur_i_coin = new double[reste_x][reste_y];


					for (int k=0;k<reste_x;k++)
						for (int l=0;l<reste_y;l++)
						{
							carre_nc_coin[k][l]=donnee[(((donnee.length/32)-1)*32)+k][(((donnee[0].length/32)-1)*32)+l];
						}
					//System.out.println("Moyenne");
					moyenne = TraitImage.calculMoyenne(carre_nc_coin);
					//System.out.println("Centrage");
					carre_c_coin = TraitImage.centrageImage(carre_nc_coin,moyenne);
					//System.out.println("Prediction");

					double[][] coeff_temp = new double[coeff.length][coeff[0].length];
					for (int k=0;k<coeff.length;k++)
						for (int l=0;l<coeff[0].length;l++)
							coeff_temp[k][l]=coeff[k][l];

					TraitImage.getCoeff(carre_c_coin,coeff_temp);

					TraitImage.predictionAR2d(carre_c_coin,erreur_i_coin, coeff_temp, Integer.parseInt(Tvalue.getText()));
					//System.out.println("Restitution");
					TraitImage.predictionAR2d_inv(erreur_i_coin,carre_o_coin, coeff_temp, Integer.parseInt(Tvalue.getText()),moyenne);
					//System.out.println("Copie vers sortie");
					for (int k=0;k<reste_x;k++)
						for (int l=0;l<reste_y;l++)
						{
							erreur[(((donnee.length/32)-1)*32)+k][(((donnee[0].length/32)-1)*32)+l]=erreur_i_coin[k][l];
							x_rec[(((donnee.length/32)-1)*32)+k][(((donnee[0].length/32)-1)*32)+l]=carre_o_coin[k][l];
						}
				}

			//Generer l'image d'erreur
			for(int i=0;i<erreur.length;i++)
				for(int j=0;j<erreur[0].length;j++)
				{
					/* if (erreur[i][j]<0)
                   System.out.println(erreur[i][j]+" ");*/
					erreur[i][j]=Math.abs(erreur[i][j]);

				}
			erreurIm=TraitImage.setPixelTab(erreur);

			//calcul des blocs
			//System.out.println("Changement de format");
			modifIm=TraitImage.setPixelTab(x_rec);
		}
		//###########################################################






		//################### Methode globale #######################
		//modifIm=TraitImage.setPixelTab(donnee);
		if(ComboTraitement.getSelectedItem()=="Prediction globale")
		{


			//System.out.println("Debut traitement");
			//System.out.println("Centrage de l'image");
			double moyenne = TraitImage.calculMoyenne(Iori);
			double[][] donnee = TraitImage.centrageImage(Iori,moyenne);
			//System.out.println("Moyenne ="+moyenne);

			TraitImage.getCoeff(donnee,coeff);

			//System.out.println("Reservation memoire y="+donnee.length+" x="+donnee[0].length);
			double[][] erreur = new double[donnee.length][donnee[0].length];
			//System.out.println("Calcul prediction");

			/* MODIF TITUS */
			for(int i=0;i<coeff.length;i++)
				for(int j=0;j<coeff[0].length;j++)
					System.out.println("coeff "+coeff[i][j]);
			//		coeff[0][0] = 0.0; 	coeff[0][1] = 0.5; 	coeff[1][0] = 0.5; 
			/* FIN MODIF TITUS */

			TraitImage.predictionAR2d(donnee,erreur, coeff, Integer.parseInt(Tvalue.getText()));

			//System.out.println("Reservation memoire");
			double[][] x_rec = new double[donnee.length][donnee[0].length];
			//System.out.println("Calcul restitution");
			TraitImage.predictionAR2d_inv(erreur,x_rec, coeff, Integer.parseInt(Tvalue.getText()),moyenne);
			//System.out.println("Changement de format");
			modifIm = TraitImage.setPixelTab(x_rec);
			//System.out.println("Fin traitement");
			//modifIm =TraitImage.applicMasque(modifIm,coeff,Fw*2-1,Fh*2-1);

			//System.out.println("end");


			//Generer l'image d'erreur
			for(int i=0;i<erreur.length;i++)
				for(int j=0;j<erreur[0].length;j++)
				{
					/* if (erreur[i][j]<0)
                   System.out.println(erreur[i][j]+" ");*/
					erreur[i][j]=Math.abs(erreur[i][j]);

				}
			erreurIm=TraitImage.setPixelTab(erreur);
		}
		//#############################################################




		String nom="M"+oriName.getText();

		modifName.setText(nom);
		erreurName.setText("E"+oriName.getText());
	}





	//Initialise champs de la comboBox
	public void initCombo(){
		/* ComboTraitement.addItem("Aucun");
    ComboTraitement.addItem("Moyenneur");*/
		ComboTraitement.addItem("Prediction globale");
		ComboTraitement.addItem("Prediction par blocs");

		for(int i=1;i<=10;i++)
		{
			jComboH.addItem(i+"");
			jComboW.addItem(i+"");
		}
		jComboH.setSelectedIndex(3);
		jComboW.setSelectedIndex(3);
	}



	void jButton2_actionPerformed(ActionEvent e) {
		System.exit(1);
	}

	void jButHisto_actionPerformed(ActionEvent e) {

		//Affiche histo
		if(this.oriName.getText().length()!=0)
		{
			histoGraphOri.dispose();
			histoGraphOri = new Histogramme(oriHisto,"originale",Color.blue,oriEntrop);

			double delta= histoGraphOri.getSize().getWidth()/2;
			this.oriHisto = TraitImage.calculHisto(bufIm);
			this.oriEntrop = TraitImage.calculEntropie(this.oriHisto,bufIm.getHeight()*bufIm.getWidth());
			this.histoGraphOri.valeurHisto=this.oriHisto;
			this.histoGraphOri.entropie=this.oriEntrop;

			histoGraphOri.show();
			histoGraphOri.setLocation((int)(histoGraphOri.getLocation().getX()-delta-60),(int)(histoGraphOri.getLocation().getY()-125));
		}

		if(this.modifName.getText().length()!=0)
		{
			histoGraphModif.dispose();
			histoGraphModif = new Histogramme(modifHisto,"modifiee",Color.red,modifEntrop);

			double delta= histoGraphModif.getSize().getWidth()/2;
			this.modifHisto= TraitImage.calculHisto(modifIm);
			this.modifEntrop = TraitImage.calculEntropie(this.modifHisto,modifIm.getHeight()*modifIm.getWidth());
			histoGraphModif.valeurHisto=this.modifHisto;
			this.histoGraphModif.entropie=this.modifEntrop;

			histoGraphModif.show();
			histoGraphModif.setLocation((int)(histoGraphModif.getLocation().getX()+delta+60),(int)(histoGraphModif.getLocation().getY()-125));
		}

		if(this.erreurName.getText().length()!=0)
		{
			histoGraphErreur.dispose();
			histoGraphErreur = new Histogramme(erreurHisto,"erreure",Color.green,erreurEntrop);

			double delta= histoGraphErreur.getSize().getWidth()/2;
			this.erreurHisto= TraitImage.calculHisto(erreurIm);
			this.erreurEntrop = TraitImage.calculEntropie(this.erreurHisto,erreurIm.getHeight()*erreurIm.getWidth());
			histoGraphErreur.valeurHisto=this.erreurHisto;
			this.histoGraphErreur.entropie=this.erreurEntrop;

			histoGraphErreur.show();
			histoGraphErreur.setLocation((int)(histoGraphErreur.getLocation().getX()),(int)(histoGraphErreur.getLocation().getY()+125));
		}





	}

	void BAfficheOri_actionPerformed(ActionEvent e) {
		if(this.oriName.getText().length()>0)
		{
			oriCadre.setName("oriCadre");
			afficherIm(bufIm,oriCadre,"Image originale : "+this.nomOri);
		}
	}

	void BAffichErreur_actionPerformed(ActionEvent e) {
		if(this.erreurName.getText().length()>0)
		{
			erreurCadre.setName("erreurCadre");
			afficherIm(erreurIm,erreurCadre,"Erreure prediction : "+this.erreurName.getText());
		}

	}

	void BAffichModif_actionPerformed(ActionEvent e) {
		if(this.modifName.getText().length()>0)
		{
			modifCadre.setName("modifCadre");
			afficherIm(modifIm,modifCadre,"Image reconstituee : "+this.modifName.getText());
		}
	}

	void JButModifFen_actionPerformed(ActionEvent e) {
		this.jtoto.w=Integer.parseInt(jComboW.getSelectedItem().toString());
		this.jtoto.h=Integer.parseInt(jComboH.getSelectedItem().toString());
		this.parent.w=this.jtoto.w;
		this.parent.h=this.jtoto.h;
		this.parent.MAJ(this.jtoto.w,this.jtoto.h);
	}





}
