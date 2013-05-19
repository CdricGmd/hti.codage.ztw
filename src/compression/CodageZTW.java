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
 * <p>
 * La classe doit être instanciee car elle utilise un buffer pour la lecture et l'écriture d'un fichier binaire.
 * </p>
 * @author Cedric Golmard, Ghizlane Aroussi
 * @version 1.0
 */
public class CodageZTW {

	/**
	 * Code des classes de pixels.
	 *
	 *Pour l'implémentation une etiquette est codee sur un entier.
	 *Seul les deux bits de poid faible du code de cet entier representent l'etiquette.
	 */
	
	/**
	 * Code d'un pixel Zero-Tree Root
	 * Etiquette = 2 bits de poids faible.
	 * Etiquette = 00
	 */
	private final int ZTR = 0x0000;

	/**
	 * Code d'un pixel Zero-Isolated
	 * Etiquette = 2 bits de poids faible.
	 * Etiquette = 01
	 */
	private final int ZI = 0x0001;

	/**
	 * Code d'un pixel Positive
	 * Etiquette = 2 bits de poids faible.
	 * Etiquette = 11
	 */
	private final int P = 0x0003;

	/**
	 * Code d'un pixel Negative
	 * Etiquette = 2 bits de poids faible.
	 * Etiquette = 11
	 */
	private final int N = 0x0002;

	/**
	 * Code pour un pixel descendant d'un ZTR
	 * Cette etiquette n'est pas ecrite dans le fichier binaire.
	 */
	private final int NS = 0x0004;

	private int bitBuffer;
	private final  int sizeOfBitBuffer = 32;
	private  int posBuffer;
	
	public CodageZTW(){
		this.bitBuffer = 0x0000;
		this.posBuffer = 0x0000;
	}
	
	/**
	 * Codage ZTW d'une image transformee.
	 * <p>
	 * Le critere d'arret du codage peut etre le niveau de resolution ou la
	 * taille du fichier.
	 * </p>
	 * Condition : 
	 * 
	 * @param xt
	 *            image transformee a coder
	 * @param width
	 *            taille de l'image
	 * @param height
	 *            taile de l'image
	 * @param niv_resol
	 *            nombre de niveaux de resolution. Condition : niv-resol < min(sqrt(height), sqrt(width))
	 * @param size
	 *            taille imposee du flux binaire en (kbits)
	 * @param bitstream_name
	 *            nom du fichier de stockage du flux binaire
	 * 
	 * @return entier informant de la reussite ou non du codage
	 * @throws IOException
	 */
	public int ztw_code(double[][] xt, int width, int height,
			int niv_resol, int size, String bitstream_name) throws IOException {
		/**
		 * Initialisation
		 */
		int MM = (int) (height / Math.pow(2, niv_resol));
		int NN = (int) (width / Math.pow(2, niv_resol));
		double T = seuil(xt, height, width, niv_resol);
		int current_size = 0;
		
		int[][] etiquettes = new int[height][width];
		
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
			
			MM = (int) (height / Math.pow(2, niv_resol));
			NN = (int) (width / Math.pow(2, niv_resol));
			
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
			while (MM <= height && NN <= width) {
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
		int[][] etiquettes ;//= new int[height][width];
		double T = 0;
		int MM,NN; 
		bitBuffer = 0x0000;
		posBuffer = 0;
		xtrec = new double[height][width];
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
		System.out.println("[CodageZTW] : seuil = " + T);
		/**
		 * Lecture de la sous-bande basse frequence
		 */
		MM =(int) (height / Math.pow(2, niv_resol));
		NN = (int) (width / Math.pow(2, niv_resol));
		for (int i = 0; i < MM; i++) {
			for (int j = 0; j < NN; j++) {
				xtrec[i][j] = dis.readDouble();
			}
		}
		
		/**
		 * Iteration
		 */
		while (dis.available() > 4) {
			System.out.println("[CodageZTW] : reste " + dis.available() / 1000
					+ " kbit a decoder.");
			
			etiquettes = new int[height][width];
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
			MM = (int) (height / Math.pow(2, niv_resol));
			NN = (int) (width / Math.pow(2, niv_resol));
			/**
			 * Sous-bande hautes frequences
			 */
			while (MM <= height && NN <= width) {
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
				MM = MM * 2;
				NN = NN * 2;
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
		 
		if(i>=height || j>=width){
			 return;
		}
		/**
		* Cas 1 : etiquette NS, descendant de ZTR.
		*/
		if ((estEgal(etiquettes, NS, i, j)) || (estEgal(etiquettes, N, i, j))
				|| (estEgal(etiquettes, P, i, j)) || (estEgal(etiquettes, ZI, i, j)) || (estEgal(etiquettes, ZTR, i, j))) {
			return;
		}
		/**
		 * Recharcher le buffer si deja parcouru.
		 */
		if(posBuffer == -1){
			if(dis.available()>4){
				try {
					bitBuffer = dis.readInt();
					posBuffer = 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return;
			}
		}
		/**
		 * Recuperation de l'étiquette.
		 */
		int etiq = 0x0000;
		int bitMask = 0x0003;
		bitMask = (bitMask << (sizeOfBitBuffer - posBuffer -2));
		etiq |= (bitBuffer & bitMask);
		etiq = etiq >> (sizeOfBitBuffer - posBuffer -2);
		posBuffer += 2;
		/**
		 * Verification de la position courante dans le buffer.
		 */
		if(posBuffer >= sizeOfBitBuffer - 2){
			posBuffer = -1;
		}
		/**
		 * Affectation et verification de la descendance si ZTR.
		 */
		affecter(etiquettes, etiq, i, j);
		if (estEgal(etiquettes, ZTR, i, j)) {
			marquerDescendantsNS(etiquettes, i, j, niv_resol, height, width);
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
	private  double seuil(double[][] donnee, int height, int width, int niv_resol) {
		double max_temp = -10000;
		int MM = (int) (height / Math.pow(2, niv_resol));
		int NN = (int) (width / Math.pow(2, niv_resol));
		for (int i = 0; i<MM; i++) {
			for (int j = NN; j<width; j++) {
				if (max_temp < Math.abs(donnee[i][j])){
					max_temp = Math.abs(donnee[i][j]);
				}
			}
		}
		for (int i = MM; i<height; i++) {
			for (int j = 0; j<width; j++) {
				if (max_temp < Math.abs(donnee[i][j]))
					max_temp = Math.abs(donnee[i][j]);
				
			}
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
	private  void actualiseCoeff(double donnee[][],int[][] etiquettes, double seuil, int niv_resol, int height, int width) {
		int MM = (int) (height / Math.pow(2, niv_resol));
		int NN = (int) (width / Math.pow(2, niv_resol));
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
	private  void determinerEtiquette(double[][] x,int[][] etiquettes, int i, int j, double seuil, int niv_resol, int height, int width) {	
		/**
		 * Extremites depassees
		 */
		if ((i >= height) || (j >= width))
			return;

		/**
		 * Etiquette deja connue
		 */
		else if ((estEgal(etiquettes, NS, i, j)) || (estEgal(etiquettes, N, i, j))
						|| (estEgal(etiquettes, P, i, j)) || (estEgal(etiquettes, ZI, i, j)) || (estEgal(etiquettes, ZTR, i, j))) {
			// Rien a faire =)
			return;
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
			return;
		}

		/**
		 * Pixel non-significatif
		 */
		else {
			
			/**
			 * Sous-bande haute frequence
			 */
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
				return;
			}
			/**
			 * Pixel ZI
			 */
			else if (etiquettes[2*i][2*j] == P || etiquettes[2*i][2*j] == N || etiquettes[2*i][2*j] == ZI
					|| etiquettes[2*i+1][2*j] == P || etiquettes[2*i+1][2*j] == N || etiquettes[2*i+1][2*j] == ZI
					|| etiquettes[2*i][2*j+1] == P || etiquettes[2*i][2*j+1] == N || etiquettes[2*i][2*j+1] == ZI
					|| etiquettes[2*i+1][2*j +1] == P || etiquettes[2*i+1][2*j +1] == N || etiquettes[2*i+1][2*j+1] == ZI){
				affecter(etiquettes, ZI, i, j);
				return;
			}
			/**
			 * Pixel ZTR
			 */
			else {
				affecter(etiquettes, ZTR, i, j);
				marquerDescendantsNS(etiquettes, i, j, niv_resol, height,
						width);
				return;
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
		
		/**
		 * Sous-bande basse frequence
		 */
		/*
		//int MM = (int) (height / Math.pow(2, niv_resol));
		//int NN = (int) (width / Math.pow(2, niv_resol));
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
			return;

		}
		*/
		/**
		 * Sous-bande haute frequence
		 */	
		// Etiquetage en ZNS des descendants directs
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

	/**
	 * Affecter un code (etiquette) à un pixel dans le tableau de etiquettes
	 * @param etiquettes
	 * @param etiq
	 * @param u
	 * @param v
	 */
	private  void affecter(int[][] etiquettes, int etiq,int u, int v) {
	//private  void affecter(boolean[][][] etiquettes, boolean[] etiq,int u, int v) {
		if(u >= etiquettes.length || v >= etiquettes[0].length)
			return;
		etiquettes[u][v] = etiq;
	}

	/**
	 * Fonction de compairaison d'un code (etiquette) et d'une entree dans le tableau des etiquettes
	 * @param etiquettes
	 * @param etiq
	 * @param u
	 * @param v
	 * @return
	 */
	private  boolean estEgal(int[][] etiquettes, int etiq, int u, int v) { 
		if(u>= etiquettes.length || v >= etiquettes[1].length)
			return false;
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
			if(estEgal(etiquettes,NS, u, v))
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
}
