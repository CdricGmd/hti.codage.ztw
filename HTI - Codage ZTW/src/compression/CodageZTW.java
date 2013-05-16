package compression;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * CodageZTW est une classe qui permet d'effectuer un codage/decodage binaire
 * d'une decomposition en ondelettres.
 * 
 * <p>
 * Codage binaire ZTW d'une decoposition multiresolution en ondelettes. Zero
 * Tree Wavelet Coding.
 * </p>
 * 
 * @author Cedric Golmard, Ghizlane Aroussi
 * @version 1.0
 */
public class CodageZTW {

	/**
	 * Code binaire des classes de pixels.
	 * 
	 * Remarque : le premier boolean est false si non significatif, true si
	 * significatif
	 */
	private int debug_temp;
	/**
	 * Code binaire d'un pixel Zero-Tree Root
	 */
	//private final  boolean[] ZTR = { false, false, true };
	private final int ZTR = 0x0000;

	/**
	 * Code binaire d'un pixel Zero-Isolated
	 */
	//private final  boolean[] ZI = { false, true, true };
	private final int ZI = 0x0001;

	/**
	 * Code binaire d'un pixel Positive
	 */
	//private final  boolean[] P = { true, true, true };
	private final int P = 0x0003;

	/**
	 * Code binaire d'un pixel Negative
	 */
	//private final  boolean[] N = { true, false, true };
	private final int N = 0x0002;

	/**
	 * Code pour un pixel descendant d'un ZTR
	 */
	//private final  boolean[] NS = { false, false, false };
	private final int NS = 0x0004;

	private int bitBuffer;
	private final  int sizeOfBitBuffer = 32;
	private  int posBuffer;
	
	public CodageZTW(){
		this.bitBuffer = 0x0000;
		this.posBuffer = 0x0000;
		debug_temp = 0;
	}
	
	/**
	 * Codage ZTW d'une image transformee.
	 * <p>
	 * Le critere d'arret du codage peut etre le niveau de resolution ou la
	 * taille du fichier.
	 * </p>
	 * 
	 * @param xt
	 *            image transformee a coder
	 * @param width
	 *            taille de l'image
	 * @param height
	 *            taile de l'image
	 * @param niv_resol
	 *            nombre de niveaux de resolution
	 * @param size
	 *            taille imposee du flux binaire en (kbits)
	 * @param bitstream_name
	 *            nom du fichier de stockage du flux binaire
	 * 
	 * @return entier informant de la reussite ou non du codage
	 * @throws IOException
	 */
	// condition : niv-resol < sqrt(height)
	public int ztw_code(double[][] xt, int width, int height,
			int niv_resol, int size, String bitstream_name) throws IOException {
		/**
		 * Initialisation
		 */
		int MM = (int) (height / Math.pow(2, niv_resol - 1));
		int NN = (int) (width / Math.pow(2, niv_resol - 1));
		double T = seuil(xt);
		int current_size = 0;
		int[][] etiquettes = new int[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				affecter(etiquettes, 0xFFFF, i, j);
		
		DataOutputStream ecrivain = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(bitstream_name)));

		System.out.println("[CodageZTW] : codage de l'image dans '"
				+ bitstream_name + "'... ");
		System.out.println("[CodageZTW] : taille demandee " + size + " kbit. ");
		System.out.println("[CodageZTW] : seuil = " + T);

		/**
		 * Ecriture du seuil initial
		 */
		ecrivain.writeDouble(T);
		
		/**
		 * Ecriture de la sous-bande basses frequences
		 */
		for (int i = 0; i < MM; i++) {
			for (int j = 0; j < NN; j++) {
				ecrivain.writeDouble(xt[i][j]);
			}
		}
		
		/**
		 * Iterations de l'algorithme
		 */
		while (current_size  < (size * 1000)) {
			/**
			 * Reaffectation des etiquettes
			 */
			for (int i = 0; i < height; i++)
				for (int j = 0; j < width; j++)
					affecter(etiquettes, 0xFFFF, i, j);
			
			MM = (int) (height / Math.pow(2, niv_resol - 1));
			NN = (int) (width / Math.pow(2, niv_resol - 1));
			
			/**
			 * Sous-bande basses frequences
			 * 
			 */
			/*
			for (int i = 0; i < MM; i++) {
				for (int j = 0; j < NN; j++) {
					determinerEtiquette(xt, etiquettes, i, j, T, niv_resol,
							height, width);
					ecrireEtiquette(etiquettes, i, j, ecrivain);
				}
			}
			*/
			/**
			 * Sous-bande hautes frequences
			 * 
			 */
			while (MM < height && NN < width) {
				for (int i = 0; i < MM; i++)
					for (int j = NN; j < 2 * NN; j++) { // Sous-bande 1
						determinerEtiquette(xt, etiquettes, i, j, T, niv_resol,
								height, width);
						ecrireEtiquette(etiquettes, i, j, ecrivain);
						
					}
				for (int i = MM; i < 2 * MM; i++)
					for (int j = 0; j < NN; j++) { // Sous-bande 2
						determinerEtiquette(xt, etiquettes, i, j, T, niv_resol,
								height, width);
						ecrireEtiquette(etiquettes, i, j, ecrivain);
					}
				for (int i = MM; i < 2 * MM; i++)
					for (int j = NN; j < 2 * NN; j++) { // Sous-bande 3
						determinerEtiquette(xt, etiquettes, i, j, T, niv_resol,
								height, width);
						ecrireEtiquette(etiquettes, i, j, ecrivain);
					}
				MM *= 2;
				NN *= 2;
			}
			
			System.out.println("[CodageZTW] : taille du fichier "+ (ecrivain.size() / 1000) + " kbit. ");
			// Flush : écrit cette partie dans le fichier, au cas ou ça bloque
			// avant on a toujours cette partie de codée.
			current_size = ecrivain.size();
			ecrivain.flush();
			
			if(current_size < (size * 1000)){
				/**
				 * Actualisation du seuil et des coefficients de l'image
				 */
				T /= 2;
				actualiseCoeff(xt, etiquettes, T, niv_resol, height, width);
			}
			else{
				ecrivain.writeInt(bitBuffer);
				current_size = ecrivain.size();
				bitBuffer = 0;
				posBuffer = 0;
				break;
			}
		}

		/**
		 * Fermeture du fichier et fin.
		 */
		ecrivain.close();
		System.out.println("[CodageZTW] : fin du codage de l'image dans '"
				+ bitstream_name + "'. ");
		
		return current_size;
	}

	/**
	 * Decodage d'un flux binaire ZWTC
	 * <p>
	 * </p>
	 * 
	 * @param xtrec
	 *            image transformee reconstruite a partir du flux bianire
	 * @param width
	 *            taille de l'image
	 * @param height
	 *            taile de l'image
	 * @param niv_resol
	 *            nombre de niveaux de resolution
	 * @param bitstream_name
	 *            nom du fichier de stockage du flux binaire
	 * 
	 * @return entier informant de la reussite ou non du decodage
	 * @throws IOException
	 */
	public  int ztw_decode(double[][] xtrec, int width, int height,
			int niv_resol, String bitstream_name) throws IOException {
		/**
		 * Initialisations
		 */
		int[][] etiquettes = new int[height][width];
		double T = 0;
		int MM = (int) (height / Math.pow(2, niv_resol - 1));
		int NN = (int) (width / Math.pow(2, niv_resol - 1));
		
		assert (xtrec.length >= height && xtrec[0].length >= width);
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				xtrec[i][j] = 0;
		
		/**
		 * Ouverture du fichier
		 */
		DataInputStream dis = null;
		dis = new DataInputStream(new FileInputStream(new File(bitstream_name)));

		System.out.println("[CodageZTW] : decodage de '"+ bitstream_name + "'... ");

		/**
		 * Lecture du seuil initial
		 */
		T = dis.readDouble();
		/**
		 * Lecture de la sous-bande basse frequence
		 */
		for (int i = 0; i < MM; i++) {
			for (int j = 0; j < NN; j++) {
				xtrec[i][j] = dis.readDouble();
			}
		}
		/**
		 * Iteration
		 */
		while (dis.available() > 4) {
			System.out.println("[CodageZTW] : seuil = " + T);
			System.out.println("[CodageZTW] : reste " + dis.available() / 1000
					+ " kbit a decoder.");
			for (int i = 0; i < height; i++)
				for (int j = 0; j < width; j++) {
					affecter(etiquettes, 0xFFFF, i, j);
				}
			/**
			 * Sous-bande basses frequences
			 */
			/*
			for (int i = 0; i < MM; i++) {
				for (int j = 0; j < NN; j++) {
					readEtiquetteFromBitstream(etiquettes, width, height,
							niv_resol, i, j, dis);
				}
			}
			*/
			MM = (int) (height / Math.pow(2, niv_resol - 1));
			NN = (int) (width / Math.pow(2, niv_resol - 1));
			/**
			 * Sous-bande hautes frequences
			 */
			while (MM < height && NN < width) {
				for (int i = 0; i < MM; i++)
					for (int j = NN; j < 2 * NN; j++) { // Sous-bande 1
						readEtiquetteFromBitstream(etiquettes, width, height, niv_resol, i, j, dis);
					}
				for (int i = MM; i < 2 * MM; i++)
					for (int j = 0; j < NN; j++) { // Sous-bande 2
						readEtiquetteFromBitstream(etiquettes, width, height, niv_resol, i, j, dis);
					}
				for (int i = MM; i < 2 * MM; i++)
					for (int j = NN; j < 2 * NN; j++) { // Sous-bande 3
						readEtiquetteFromBitstream(etiquettes, width, height, niv_resol, i, j, dis);
					}
				MM *= 2;
				NN *= 2;
			}
			/**
			 * Actualisation des coefficients de l'image
			 */
			actualiseCoeff(xtrec, etiquettes, T,niv_resol, height, width);
			/**
			 * Actualisation du seuil
			 */
			T /= 2;
		}

		dis.close();
		System.out.println("[CodageZTW] : fin du decodage de l'image dans '"+ bitstream_name + "'... ");
		return 0;
	}

	/**
	 * readEtiquetteFromBitstream
	 * @param etiquettes
	 * @param width
	 * @param height
	 * @param niv_resol
	 * @param i
	 * @param j
	 * @param dis
	 * @throws IOException
	 */
	 private void readEtiquetteFromBitstream(int [][] etiquettes, int width,
			int height, int niv_resol, int i, int j, DataInputStream dis)
			throws IOException {
		 
		if(i>height || j>width){
			 return;
		}
		/**
		* Cas 1 : etiquette NS, descendant de ZTR.
		*/
		if (estEgal(etiquettes, NS, i, j)) {
			return;
		}
		/**
		 * Recharcher le buffer si deja parcouru.
		 */
		if(posBuffer == 0x0000){
			if(dis.available()>4){
				bitBuffer = dis.readInt();
			}else{
				return;
			}
		}
		/**
		 * Lecture de l'etiquette.
		 */
		else{
			/**
			 * Recuperation de l'étiquette.
			 */
			int etiq = 0x0000;
			int bitMask = 0x0003;
			bitMask = (bitMask << (sizeOfBitBuffer - posBuffer -2));
			etiq |= (bitBuffer & bitMask);
			etiq = etiq >> (sizeOfBitBuffer - posBuffer);
			posBuffer += 2;

			/**
			 * Affectation et verification de la descendance si ZTR.
			 */
			affecter(etiquettes, etiq, i, j);
			if (estEgal(etiquettes, ZTR, i, j)) {
				marquerDescendantsNS(etiquettes, i, j, niv_resol, height, width);
			}
			/**
			 * Verification de la position courante dans le buffer.
			 */
		}
		if(posBuffer >= sizeOfBitBuffer){
			posBuffer = 0x0000;
		}
	}

	/**
	 * Calcul de la valeur de seuil initiale.
	 * <p>
	 * La valeur initiale de seuil est la moitie de la plus grande valeur (en
	 * valeur absolue) de l'image transformee.
	 * </p>
	 * 
	 * @param donnee
	 *            image transformee dans un tableau de double
	 * 
	 * @return valeur de seuil initiale
	 */
	private  double seuil(double[][] donnee) {
		double max_temp = -10000;
		for (int i = 0; i < donnee.length; i++)
			for (int j = 0; j < donnee[0].length; j++)
				if (max_temp < Math.abs(donnee[i][j])){
					max_temp = Math.abs(donnee[i][j]);
				}
		return (max_temp / 2);
	}

	/**
	 * Actualise les coefficients d'un pixel significatif selon son signe
	 * 
	 * @param donnee
	 *            image transformee
	 * @param etiquettes
	 *            tableau auxiliaire qui code si un pixel est significatif ou
	 *            non
	 * @param i
	 * @param j
	 *            coordonnees du coefficient
	 * @param seuil
	 *            valeur de seuil
	 * 
	 * @return nouvelle valeur du coefficient
	 */
	//private  void actualiseCoeff(double donnee[][],boolean[][][] etiquettes, double seuil, int height, int width) {
	private  void actualiseCoeff(double donnee[][],int[][] etiquettes, double seuil, int niv_resol, int height, int width) {
		int MM = (int) (height / Math.pow(2, niv_resol - 1));
		int NN = (int) (width / Math.pow(2, niv_resol - 1));
		for (int i = 0; i<MM; i++) {
			for (int j = NN; j<width; j++) {
				if (estEgal(etiquettes, P, i, j)){
					donnee[i][j] -= seuil;
				}
				else if (estEgal(etiquettes, N, i, j)){
					donnee[i][j] += seuil;
				}
			}
		}
		for (int i = MM; i<height; i++) {
			for (int j = 0; j<width; j++) {
				if (estEgal(etiquettes, P, i, j)){
					donnee[i][j] -= seuil;
				}
				else if (estEgal(etiquettes, N, i, j)){
					donnee[i][j] += seuil;
				}
			}
		}
		return;
	}

	/**
	 * Determne l'etiquette associee au pixel de l'image donnee.
	 * 
	 * @param x
	 * @param etiquettes
	 * @param i
	 * @param j
	 * @param seuil
	 * @param niv_resol
	 * @param height
	 * @param width
	 */
	//private  void determinerEtiquette(double[][] x,boolean[][][] etiquettes, int i, int j, double seuil, int niv_resol, int height, int width) {
	private  void determinerEtiquette(double[][] x,int[][] etiquettes, int i, int j, double seuil, int niv_resol, int height, int width) {
		
		/**
		 * Extremites depassees
		 */
		if ((i >= height) || (j >= width))
			return;
		//System.out.println("Debug - PS value = " + x[i][j]);

		/**
		 * Etiquette deja connue
		 */
		else if ((estEgal(etiquettes, NS, i, j)) || (estEgal(etiquettes, N, i, j))
						|| (estEgal(etiquettes, P, i, j)) || (estEgal(etiquettes, ZI, i, j)) || (estEgal(etiquettes, ZTR, i, j))) {
			// Rien a faire =)
		}
		/**
		 * Pixel significatif
		 */
		else if (Math.abs(x[i][j]) > seuil) {
			if (x[i][j] >= 0) {
				affecter(etiquettes, P, i, j);
			} else {
				affecter(etiquettes, N, i, j);
			}
		}

		/**
		 * Pixel non-significatif
		 */
		else {
			int MM = (int) (height / Math.pow(2, niv_resol - 1));
			int NN = (int) (width / Math.pow(2, niv_resol - 1));
			/**
			 * Sous-bande basse frequence
			 */
			if ((i < MM) && (j < NN)) {
				// Appel recursif
				determinerEtiquette(x, etiquettes, i, j + NN, seuil, niv_resol,
						height, width);
				determinerEtiquette(x, etiquettes, i + MM, j, seuil, niv_resol,
						height, width);
				determinerEtiquette(x, etiquettes, i + MM, j + NN, seuil,
						niv_resol, height, width);

				// Comparaison
				/**
				 * Extremite
				 */
				if ((i + MM >= height) || (j + NN >= width)){
					affecter(etiquettes, ZTR, i, j);
				}
				/**
				 * Pixel ZI
				 */
				else if (etiquettes[i][j + NN] == P || etiquettes[i][j + NN] == N || etiquettes[i][j + NN] == ZI
						|| etiquettes[i+MM][j] == P || etiquettes[i+MM][j] == N || etiquettes[i+MM][j] == ZI
						|| etiquettes[i+MM][j+NN] == P || etiquettes[i+MM][j+NN] == N || etiquettes[i+MM][j+NN] == ZI) {
					affecter(etiquettes, ZI, i, j);
				}
				/**
				 * Pixel ZTR
				 */
				else {
					affecter(etiquettes, ZTR, i, j);
					marquerDescendantsNS(etiquettes, i, j, niv_resol, height,
							width);
				}
			}
			/**
			 * Sous-bande haute frequence
			 */
			else {
				// Appel recursif
				determinerEtiquette(x, etiquettes, 2 * i, 2 * j, seuil,
						niv_resol, height, width);
				determinerEtiquette(x, etiquettes, 2 * i, 2 * j + 1, seuil,
						niv_resol, height, width);
				determinerEtiquette(x, etiquettes, 2 * i + 1, 2 * j, seuil,
						niv_resol, height, width);
				determinerEtiquette(x, etiquettes, 2 * i + 1, 2 * j + 1, seuil,
						niv_resol, height, width);

				// Comparaison
				/**
				 * Extremite
				 */
				if ((2 * i + 1 >= height) || (2 * j + 1 >= width)) {
					affecter(etiquettes, ZTR, i, j);
				}
				/**
				 * Pixel ZI
				 */
				else if (etiquettes[2 * i][2 * j] == P || etiquettes[2 * i][2 * j] == N || etiquettes[2 * i][2 * j] == ZI
						|| etiquettes[2 * i+1][2 * j] == P || etiquettes[2 * i+1][2 * j] == N || etiquettes[2 * i+1][2 * j] == ZI
						|| etiquettes[2 * i][2 * j +1] == P || etiquettes[2 * i][2 * j +1] == N || etiquettes[2 * i][2 * j+1] == ZI
						|| etiquettes[2 * i+1][2 * j +1] == P || etiquettes[2 * i+1][2 * j +1] == N || etiquettes[2 * i+1][2 * j+1] == ZI){
					affecter(etiquettes, ZI, i, j);
				}
				/**
				 * Pixel ZTR
				 */
				else {
					affecter(etiquettes, ZTR, i, j);
					marquerDescendantsNS(etiquettes, i, j, niv_resol, height,
							width);
				}
			}
		}
	}

	/**
	 * Marque tous les descendants du pixel comme NS : a en pas balayer
	 * 
	 * @param etiquettes
	 * @param i
	 * @param j
	 * @param niv_resol
	 * @param height
	 * @param width
	 */
	private  void marquerDescendantsNS(int[][] etiquettes, int i,int j, int niv_resol, int height, int width) {
	//private  void marquerDescendantsNS(boolean[][][] etiquettes, int i,int j, int niv_resol, int height, int width) {
	
		/**
		 * Critere d'arret bords de l'image
		 */
		if ((i >= height) || (j >= width))
			return;
		int MM = (int) (height / Math.pow(2, niv_resol - 1));
		int NN = (int) (width / Math.pow(2, niv_resol - 1));
		/**
		 * Sous-bande basse frequence
		 */
		if ((i < MM) | (j < NN)) {
			if ((i + MM >= height) || (j + NN >= width))
				return;
			else {
				// Etiquetage en ZNS des descendants directs
				affecter(etiquettes, NS, i + MM, j);
				affecter(etiquettes, NS, i, j + NN);
				affecter(etiquettes, NS, i + MM, j + NN);

				// Appel recursif
				marquerDescendantsNS(etiquettes, i + MM, j, niv_resol, height,
						width);
				marquerDescendantsNS(etiquettes, i, j + NN, niv_resol, height,
						width);
				marquerDescendantsNS(etiquettes, i + MM, j + NN, niv_resol,
						height, width);
			}

		}
		/**
		 * Sous-bande haute frequence
		 */
		else {
			// Bord de l'image
			if ((2 * i + 1 >= height) || (2 * j + 1 >= width))
				return;
			else {
				// Etiquetage en ZNS des descendants directs
				/*
				 * for(int t = 0; t<3; t++){ etiquettes[2 * i][2 * j][t] =
				 * NS[t]; etiquettes[2 * i + 1][2 * j][t] = NS[t]; etiquettes[2
				 * * i][2 * j + 1][t] = NS[t]; etiquettes[2 * i + 1][2 * j +
				 * 1][t] = NS[t]; }
				 */
				affecter(etiquettes, NS, 2 * i, 2 * j);
				affecter(etiquettes, NS, 2 * i + 1, 2 * j);
				affecter(etiquettes, NS, 2 * i, 2 * j + 1);
				affecter(etiquettes, NS, 2 * i + 1, 2 * j + 1);

				// Appel recursif
				marquerDescendantsNS(etiquettes, 2 * i, 2 * j, niv_resol,
						height, width);
				marquerDescendantsNS(etiquettes, 2 * i + 1, 2 * j, niv_resol,
						height, width);
				marquerDescendantsNS(etiquettes, 2 * i, 2 * j + 1, niv_resol,
						height, width);
				marquerDescendantsNS(etiquettes, 2 * i + 1, 2 * j + 1,
						niv_resol, height, width);
			}
		}
	}

	/**
	 * 
	 * @param etiquettes
	 * @param etiq
	 * @param u
	 * @param v
	 */
	private  void affecter(int[][] etiquettes, int etiq,int u, int v) {
	//private  void affecter(boolean[][][] etiquettes, boolean[] etiq,int u, int v) {
		etiquettes[u][v] = etiq;
	}

		/**
		 * 
		 * @param etiquettes
		 * @param etiq
		 * @param u
		 * @param v
		 * @return
		 */
	 //private  boolean estEgal(boolean[][][] etiquettes, boolean[] etiq, int u, int v) { 
	private  boolean estEgal(int[][] etiquettes, int etiq, int u, int v) { 
		//if ((etiq[0] == etiquettes[u][v][0]) && (etiq[1] == etiquettes[u][v][1]) && (etiq[2] == etiquettes[u][v][2])) 
		//	return true;
		//return false;
		return (etiq == etiquettes[u][v]);
	}
	 

	/**
	 * Ecriture des etiquettes a l'aide d'un buffer.
	 * L'entier bitBuffer (32bits) accumule les 16 prochaines etiquettes (16x2 bits) a ecrire.
	 * Lorsque le buffer est plein, il est ecrit dans DataOutputStream.
	 * @param etiquettes
	 * @param i
	 * @param j
	 * @param dos
	 * @throws IOException
	 */
	private  void ecrireEtiquette(int[][] etiquettes, int u, int v,DataOutputStream dos) throws IOException {
	//private  void ecrireEtiquette(boolean[][][] etiquettes, int u, int v,DataOutputStream dos) throws IOException {
			if( u >= etiquettes.length || v >= etiquettes[0].length)
				return;
			if(etiquettes[u][v] == NS)
				return;
			
			bitBuffer |= (etiquettes[u][v] >> (sizeOfBitBuffer - posBuffer));
			posBuffer +=2;

			/*
			for(int t=0; t<2; t++){
				if(etiquettes[u][v][t] == true){
					// Met le bit courant du buffer a 1
					bitBuffer = bitBuffer | (1 >> (sizeOfBitBuffer - posBuffer));
					posBuffer ++;
				}else{
					// Met le bit courant du buffer a 0
					bitBuffer = bitBuffer & ~(1 >> (sizeOfBitBuffer - posBuffer));
					posBuffer ++;
				}		
			}
			*/
			
			/**
			 * Lorsque le buffer est plein, on écrit l'octet entier.
			 */
			if(posBuffer >= sizeOfBitBuffer){
				dos.writeByte(bitBuffer);
				bitBuffer = 0;
				posBuffer = 0;
			}
	}

	//private boolean estSignificatif(int etiq){
	//	return ((etiq == P)  || (etiq == N));
	//}
	// FAUX MAIS ON GARDE AU CAS OU.
	// /**
	// * Effectue un balayage de l'image transformee selon l'ordre de parcours
	// des
	// * sous-bande.
	// *
	// * @param donnee
	// * image transformee
	// * @param etiquettes
	// * tableau auxiliaire qui code si un pixel est significatif ou
	// * non
	// * @param seuil
	// * valeur de seuil
	// *
	// */
	// private void balayage(double[][] donnee, boolean[][][] etiquettes,
	// int niv_resol, double seuil) {
	// // Taille de l'image = 2^t
	// int t = (int) Math.sqrt(donnee.length);
	// // Premiere sous-bande
	// for (int u = 0; u < (int) Math.pow(2, t - niv_resol); u++)
	// for (int v = 0; v < (int) Math.pow(2, t - niv_resol); v++) {
	// // Les pixels ZTR ne sont pas balayes
	// if (etiquettes[u][v] == NB)
	// continue;
	// else {
	// // Determination de la etiquettes du pixel :
	// // singificatif P, N ou non.
	// if (Math.abs(donnee[u][v]) > seuil) {
	// if (donnee[u][v] < 0)
	// etiquettes[u][v] = N;
	// else
	// etiquettes[u][v] = P;
	// } else
	// etiquettes[u][v] = NS;
	// }
	// }
	//
	// // Balayage iteratif
	// for (int n = (int) Math.pow(2, t - niv_resol); n < donnee.length; n *= 2)
	// {
	// // sous-bande 1
	// rasterscan(donnee, etiquettes, 1, n, seuil);
	// // sous-bande 2
	// rasterscan(donnee, etiquettes, n, 1, seuil);
	// // sous-bande 3
	// rasterscan(donnee, etiquettes, n, n, seuil);
	// }
	// }
	//
	// /**
	// * Effectue un balayge raster-scan des coefficients d'une sous-bande
	// *
	// * @param donnee
	// * image transformee
	// * @param etiquettes
	// * tableau auxiliaire qui code si un pixel est significatif ou
	// * non
	// * @param i
	// * premiere coordonnee du premier pixel de la sous-bande
	// * @param j
	// * deuxieme coordonnee du deuxieme pixel de la sous-bande
	// * @param seuil
	// * valeur de seuil de comparaison
	// */
	// private void rasterscan(double[][] donnee, boolean[][][] etiquettes,
	// int i, int j, double seuil) {
	// int taille = Math.max(i, j);
	//
	// for (int u = i; u < taille; u++) {
	// for (int v = i; v < taille; v++) {
	// // Les pixels ZTR ne sont pas balayes
	// if (etiquettes[u][v] == ZTR) {
	// continue;
	// } else {
	// // Determination de la etiquettes du pixel :
	// // singificatif P, N ou non.
	// if (Math.abs(donnee[u][v]) < seuil) {
	// if (donnee[u][v] < 0)
	// etiquettes[u][v] = N;
	// else
	// etiquettes[u][v] = P;
	// } else {
	// etiquettes[u][v] = NS;
	// }
	// }
	// }
	// }
	// }
	//
	// /**
	// *
	// * @param etiquettes
	// * @param i
	// * @param j
	// * @param height
	// * @param width
	// * @param niv_resol
	// */
	// private void getetiquettes(boolean[][][] etiquettes, int i, int j,
	// int height, int width, int niv_resol) {
	// // Cas de sortie : pixel deja classe
	// if (etiquettes[i][j].length == 2)
	// return;
	// if (estEgal(etiquettes, N, i, j)B)
	// return;
	//
	// // Cas initial : Sous-bandes extremes pour pixels non significatifs
	// // Les pixels n'ont pas de descendants
	// if (i > height / 2 | j > width / 2) {
	// etiquettes[i][j] = ZTR;
	// return;
	// }
	//
	// // Cas initial : Sous-bande 0 et pixel non significatif
	// if (i < height / Math.pow(2, niv_resol - 1)
	// | j < width / Math.pow(2, niv_resol - 1)) {
	// // on regare les descendants
	// int N = (int) Math.pow(2, (int) Math.sqrt(height) - niv_resol);
	// getetiquettes(etiquettes, i + N, j, height, width,
	// niv_resol);
	// getetiquettes(etiquettes, i, j + N, height, width,
	// niv_resol);
	// getetiquettes(etiquettes, i + N, j + N, height, width,
	// niv_resol);
	// boolean[] fils1 = etiquettes[i + N][j];
	// boolean[] fils2 = etiquettes[i][j + N];
	// boolean[] fils3 = etiquettes[i + N][j + N];
	// boolean[] fils4 = NS; // ajout fictif pour reutilser le code suivant
	//
	// if (fils1[0] == true | fils2[0] == true | fils3[0] == true
	// | fils4[0] == true) {
	// etiquettes[i][j] = ZI;
	// return;
	// } else {
	// // mise a jour de la etiquettes
	// etiquettes[i][j] = ZTR;
	// // marquage des enfants
	// etiquettes[i + N][j] = NB;
	// etiquettes[i][j + N] = NB;
	// etiquettes[i + N][j + N] = NB;
	// return;
	// }
	// }
	// // Cas general : pixel non significatif et pas encore classe
	// // on regarde les descendants de facon recursive
	// getetiquettes(etiquettes, 2 * i, 2 * j, height, width,
	// niv_resol);
	// getetiquettes(etiquettes, 2 * i + 1, 2 * j, height, width,
	// niv_resol);
	// getetiquettes(etiquettes, 2 * i, 2 * j + 1, height, width,
	// niv_resol);
	// getetiquettes(etiquettes, 2 * i + 1, 2 * j + 1, height, width,
	// niv_resol);
	// boolean[] fils1 = etiquettes[2 * i][2 * j];
	// boolean[] fils2 = etiquettes[2 * i + 1][2 * j];
	// boolean[] fils3 = etiquettes[2 * i][2 * j + 1];
	// boolean[] fils4 = etiquettes[2 * i + 1][2 * j + 1];
	//
	// if (fils1[0] == true | fils2[0] == true | fils3[0] == true
	// | fils4[0] == true) {
	// etiquettes[i][j] = ZI;
	// return;
	// } else {
	// // mise a jour de la etiquettes
	// etiquettes[i][j] = ZTR;
	// // marquage des enfants
	// etiquettes[2 * i][2 * j] = NB;
	// etiquettes[2 * i][2 * j] = NB;
	// etiquettes[2 * i][2 * j] = NB;
	// etiquettes[2 * i][2 * j] = NB;
	// return;
	// }
	//
	// }
	//
	// /*
	// private void updateBitstream(boolean[][] bistream, boolean[][][]
	// etiquettes, int width, int height, int niv_resol){
	// // Premiere sous-bande
	// int t = (int) Math.pow(2, t - niv_resol)
	// for (int u = 0; u < t; u++)
	// for (int v = 0; v < (int) Math.pow(2, t - niv_resol); v++) {
	// }
	//
	// // Balayage iteratif
	// for (int n = t; n < width; n *= 2) {
	// // Sous-bande 1
	// for (int u = 0; u < t; u++) {
	// for (int v = t; v < 2*t; v++) {
	//
	// }
	// }
	// // Sous-bande 2
	// for (int u = t; u < 2*t; u++) {
	// for (int v = 0; v < t; v++) {
	//
	// }
	// }
	// // Sous-bande 3
	// for (int u = t; u < 2*t; u++) {
	// for (int v = t; v < 2*t; v++) {
	//
	// }
	// }
	// }
	// }
	// */

}
