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
	 * Code binaired'un pixel Zero-Tree Root
	 */
	public final boolean[] ZTR = { false, false };

	/**
	 * Code binaired'un pixel Zero-Isolated
	 */
	public final boolean[] ZI = { true, true };

	/**
	 * Code binaired'un pixel Positive
	 */
	public final boolean[] P = { false, true };

	/**
	 * Code binaired'un pixel Negative
	 */
	public final boolean[] N = { true, false };

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
	public int ztw_code(double[][] xt, int width, int height, int niv_resol,
			int size, char[] bitstream_name) {
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
				if (max_temp < donnee[i][j])
					max_temp = donnee[i][j];
		return max_temp / 2;
	}

	/**
	 * Actualise les coefficients d'un pixel significatif selon son signe
	 * 
	 * @param coeff
	 *            coefficient du pixel significatif
	 * @param seuil
	 *            valeur de seuil
	 * 
	 * @return nouvelle valeur du coefficient
	 */
	private double actualiseCoeff(double coeff, double seuil) {
		if (coeff > 0)
			return coeff - seuil / 2;
		else
			return coeff + seuil / 2;
	}

	/**
	 * Effectue un balayage de l'image transformee selon l'ordre de parcours des
	 * sous-bande.
	 * 
	 * @param donnee
	 *            image transformee
	 * @param seuil
	 *            valeur de seuil
	 * 
	 */
	private void balayage(double[][] donnee, double seuil) {
		// Premier pixel
		int t = 0;
		rasterscan(donnee, 0, 0, seuil);
		// Balayage iteratif
		for (int n = 0; n < Math.sqrt(donnee.length); n++) {
			t = (int) Math.pow(2, n);
			rasterscan(donnee, 1, t, seuil);
			rasterscan(donnee, t, 1, seuil);
			rasterscan(donnee, t, t, seuil);
		}
	}

	/**
	 * Effectue un balayge raster-scan des coefficients d'une sous-bande
	 * 
	 * @param donnee
	 *            image transformee
	 * @param i
	 *            premiere coordonnee du premier pixel de la sous-bande
	 * @param j
	 *            deuxieme coordonnee du deuxieme pixel de la sous-bande
	 * @param seuil
	 *            valeur de seuil de comparaison
	 */
	private void rasterscan(double[][] donnee, int i, int j, double seuil) {
		int taille = Math.max(i, j);

		for (int u = i; u < taille; u++)
			for (int v = i; v < taille; v++)
				if (donnee[u][v] < seuil)
					System.out.println("hello world");

	}
}
