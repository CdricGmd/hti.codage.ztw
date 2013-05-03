package compression;

import java.lang.Math;

/**
 * CodageZTW est une classe qui permet d'effectuer un codage/decodage binaire
 * d'une decomposition en ondelettres.
 * 
 * <p>
 * Codage binaire ZTW d'une decoposition multiresolution en ondelettes. Zero
 * Tree Wavelet Coding.
 * </p>
 * 
 * @author Cedric Golmard
 * @version 1.0
 */
public abstract class CodageZTW {

	/**
	 * Code binaire des classes de pixels. Remarque : le premier boolean est
	 * false si non significatif, true si significatif
	 */

	/**
	 * Code binaire d'un pixel Zero-Tree Root
	 */
	public final boolean[] ZTR = { false, false };

	/**
	 * Code binaire d'un pixel Zero-Isolated
	 */
	public final boolean[] ZI = { false, true };

	/**
	 * Code binaire d'un pixel Positive
	 */
	public final boolean[] P = { true, true };

	/**
	 * Code binaire d'un pixel Negative
	 */
	public final boolean[] N = { true, false };

	/**
	 * Code pour un pixel descendant d'un ZTR
	 */
	public final boolean[] NS = { false, false, false };


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
	 */
	// condition : niv-resol < sqrt(height)
	public int ztw_code(double[][] xt, int width, int height, int niv_resol,
			int size, char[] bitstream_name) {
		/**
		 * Verifications
		 */
		//
		/*
		double T = seuil(xt);
		boolean[][][] classPixels = null;
		boolean[][] bistream = null;
		while(500*bistream.length < size){
			balayage(xt, classPixels, niv_resol, T);
			for(int i = 0; i< height / Math.pow(2, niv_resol - 1); i++)
				for(int j=0; j< width / Math.pow(2, niv_resol - 1) ; j++)
					getClassification(classPixels, i, j, height, width, niv_resol);
			updateBitstream(bistream, classPixels, width, height, niv_resol);
		}
		*/
		return 0;
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
	 */
	public int ztw_decode(double[][] xtrec, int width, int height,
			int niv_resol, char[] bitstream_name) {
		return 0;
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
	private double seuil(double[][] donnee) {
		double max_temp = -1;
		for (int i = 0; i < donnee.length; i++)
			for (int j = 0; j < donnee[i].length; j++)
				if (max_temp < Math.abs(donnee[i][j]))
					max_temp = Math.abs(donnee[i][j]);
		return max_temp / 2;
	}

	/**
	 * Actualise les coefficients d'un pixel significatif selon son signe
	 * 
	 * @param donnee
	 *            image transformee
	 * @param classification
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
	private void actualiseCoeff(double donnee[][], double[][][] classification,
			int i, int j, double seuil) {
		if (classification[i][j].equals(P))
			donnee[i][j] -= seuil / 2;
		else if (classification[i][j].equals(N))
			donnee[i][j] += seuil / 2;
	}
	
	
//	/**
//	 * Effectue un balayage de l'image transformee selon l'ordre de parcours des
//	 * sous-bande.
//	 * 
//	 * @param donnee
//	 *            image transformee
//	 * @param classification
//	 *            tableau auxiliaire qui code si un pixel est significatif ou
//	 *            non
//	 * @param seuil
//	 *            valeur de seuil
//	 * 
//	 */
//	private void balayage(double[][] donnee, boolean[][][] classification,
//			int niv_resol, double seuil) {
//		// Taille de l'image = 2^t
//		int t = (int) Math.sqrt(donnee.length);
//		// Premiere sous-bande
//		for (int u = 0; u < (int) Math.pow(2, t - niv_resol); u++)
//			for (int v = 0; v < (int) Math.pow(2, t - niv_resol); v++) {
//				// Les pixels ZTR ne sont pas balayes
//				if (classification[u][v] == NB)
//					continue;
//				else {
//					// Determination de la classification du pixel :
//					// singificatif P, N ou non.
//					if (Math.abs(donnee[u][v]) > seuil) {
//						if (donnee[u][v] < 0)
//							classification[u][v] = N;
//						else
//							classification[u][v] = P;
//					} else
//						classification[u][v] = NS;
//				}
//			}
//
//		// Balayage iteratif
//		for (int n = (int) Math.pow(2, t - niv_resol); n < donnee.length; n *= 2) {
//			// sous-bande 1
//			rasterscan(donnee, classification, 1, n, seuil);
//			// sous-bande 2
//			rasterscan(donnee, classification, n, 1, seuil);
//			// sous-bande 3
//			rasterscan(donnee, classification, n, n, seuil);
//		}
//	}
//
//	/**
//	 * Effectue un balayge raster-scan des coefficients d'une sous-bande
//	 * 
//	 * @param donnee
//	 *            image transformee
//	 * @param classification
//	 *            tableau auxiliaire qui code si un pixel est significatif ou
//	 *            non
//	 * @param i
//	 *            premiere coordonnee du premier pixel de la sous-bande
//	 * @param j
//	 *            deuxieme coordonnee du deuxieme pixel de la sous-bande
//	 * @param seuil
//	 *            valeur de seuil de comparaison
//	 */
//	private void rasterscan(double[][] donnee, boolean[][][] classification,
//			int i, int j, double seuil) {
//		int taille = Math.max(i, j);
//
//		for (int u = i; u < taille; u++) {
//			for (int v = i; v < taille; v++) {
//				// Les pixels ZTR ne sont pas balayes
//				if (classification[u][v] == ZTR) {
//					continue;
//				} else {
//					// Determination de la classification du pixel :
//					// singificatif P, N ou non.
//					if (Math.abs(donnee[u][v]) < seuil) {
//						if (donnee[u][v] < 0)
//							classification[u][v] = N;
//						else
//							classification[u][v] = P;
//					} else {
//						classification[u][v] = NS;
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * @param classification
//	 * @param i
//	 * @param j
//	 * @param height
//	 * @param width
//	 * @param niv_resol
//	 */
//	private void getClassification(boolean[][][] classification, int i, int j,
//			int height, int width, int niv_resol) {
//		// Cas de sortie : pixel deja classe
//		if (classification[i][j].length == 2)
//			return;
//		if (classification[i][j] == NB)
//			return;
//
//		// Cas initial : Sous-bandes extremes pour pixels non significatifs
//		// Les pixels n'ont pas de descendants
//		if (i > height / 2 | j > width / 2) {
//			classification[i][j] = ZTR;
//			return;
//		}
//
//		// Cas initial : Sous-bande 0 et pixel non significatif
//		if (i < height / Math.pow(2, niv_resol - 1)
//				| j < width / Math.pow(2, niv_resol - 1)) {
//			// on regare les descendants
//			int N = (int) Math.pow(2, (int) Math.sqrt(height) - niv_resol);
//			getClassification(classification, i + N, j, height, width,
//					niv_resol);
//			getClassification(classification, i, j + N, height, width,
//					niv_resol);
//			getClassification(classification, i + N, j + N, height, width,
//					niv_resol);
//			boolean[] fils1 = classification[i + N][j];
//			boolean[] fils2 = classification[i][j + N];
//			boolean[] fils3 = classification[i + N][j + N];
//			boolean[] fils4 = NS; // ajout fictif pour reutilser le code suivant
//
//			if (fils1[0] == true | fils2[0] == true | fils3[0] == true
//					| fils4[0] == true) {
//				classification[i][j] = ZI;
//				return;
//			} else {
//				// mise a jour de la classification
//				classification[i][j] = ZTR;
//				// marquage des enfants
//				classification[i + N][j] = NB;
//				classification[i][j + N] = NB;
//				classification[i + N][j + N] = NB;
//				return;
//			}
//		}
//		// Cas general : pixel non significatif et pas encore classe
//		// on regarde les descendants de facon recursive
//		getClassification(classification, 2 * i, 2 * j, height, width,
//				niv_resol);
//		getClassification(classification, 2 * i + 1, 2 * j, height, width,
//				niv_resol);
//		getClassification(classification, 2 * i, 2 * j + 1, height, width,
//				niv_resol);
//		getClassification(classification, 2 * i + 1, 2 * j + 1, height, width,
//				niv_resol);
//		boolean[] fils1 = classification[2 * i][2 * j];
//		boolean[] fils2 = classification[2 * i + 1][2 * j];
//		boolean[] fils3 = classification[2 * i][2 * j + 1];
//		boolean[] fils4 = classification[2 * i + 1][2 * j + 1];
//
//		if (fils1[0] == true | fils2[0] == true | fils3[0] == true
//				| fils4[0] == true) {
//			classification[i][j] = ZI;
//			return;
//		} else {
//			// mise a jour de la classification
//			classification[i][j] = ZTR;
//			// marquage des enfants
//			classification[2 * i][2 * j] = NB;
//			classification[2 * i][2 * j] = NB;
//			classification[2 * i][2 * j] = NB;
//			classification[2 * i][2 * j] = NB;
//			return;
//		}
//
//	}
//
//	/*
//	private void updateBitstream(boolean[][] bistream, boolean[][][] classification, int width, int height, int niv_resol){
//		// Premiere sous-bande
//		int t = (int) Math.pow(2, t - niv_resol)
//		for (int u = 0; u < t; u++)
//			for (int v = 0; v < (int) Math.pow(2, t - niv_resol); v++) {
//			}
//		
//		// Balayage iteratif
//		for (int n = t; n < width; n *= 2) {
//			// Sous-bande 1
//			for (int u = 0; u < t; u++) {
//				for (int v = t; v < 2*t; v++) {
//					
//				}
//			}
//			// Sous-bande 2
//			for (int u = t; u < 2*t; u++) {
//				for (int v = 0; v < t; v++) {
//					
//				}
//			}
//			// Sous-bande 3
//			for (int u = t; u < 2*t; u++) {
//				for (int v = t; v < 2*t; v++) {
//					
//				}
//			}
//		}
//	}
//	*/
	
}
