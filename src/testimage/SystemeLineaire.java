package testimage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class SystemeLineaire {
	public static final int INITIAL      = 1;
	public static final int TRIANGULAIRE = 2;
	public static final int RESOLU       = 3;
	public static final int SINGULIER    = 4;

	private int n;                      // dimension du systeme
	private double mat[][];             // matrice n*(n+1) du systeme
	private double epsilon;             // |x| < epsilon => x est nul
	private int etat;                   // etat courant du systeme

	public static DecimalFormat fmt;   // pour l'affichage des nombres
	static {
		DecimalFormatSymbols fs = new DecimalFormatSymbols();
		fs.setDecimalSeparator('.');
		fmt = new DecimalFormat("0.0000", fs);
	}

	public SystemeLineaire(int dim) {
		// Construction d'un systeme n x n, entierement fait de 0
		n = dim;
		mat = new double[n][n + 1];
		epsilon = 1e-8;
		etat = INITIAL;
	}

	public SystemeLineaire(double[][] a, double[] b) {
		// Construction d'un systeme a partir de ses coefficients
		this(a.length);
		for (int i = 0; i < n; i++) {
			int jMax = Math.min(n, a[i].length);
			for (int j = 0; j < jMax; j++)
				mat[i][j] = a[i][j];
		}
		int iMax = Math.min(n, b.length);
		for (int i = 0; i < iMax; i++)
			mat[i][n] = b[i];
	}

	public int n() {
		return n;
	}

	public double epsilon() {
		return epsilon;
	}

	public int etat() {
		return etat;
	}

	public void defCoef(int i, int j, double aij) {
		mat[i][j] = aij;
		etat = INITIAL;
	}

	public void defEpsilon(double epsilon) {
		this.epsilon = epsilon;
		etat = INITIAL;
	}

	public void triangularisation() {
		if (etat == INITIAL) {
			for (int k = 0; k < n; k++) {
				// recherche du pivot max
				int ip = k;
				double absPiv = Math.abs(mat[ip][k]);
				for (int j = k + 1; j < n; j++)
					if (Math.abs(mat[j][k]) > absPiv) {
						ip = j;
						absPiv = Math.abs(mat[ip][k]);
					}
				// test du pivot
				if (absPiv < epsilon) {
					etat = SINGULIER;
					return;
				}
				// permutation eventuelle de lignes
				if (ip != k) {
					double[] w = mat[k];
					mat[k] = mat[ip];
					mat[ip] = w;
				}
				// arrangement des lignes sous le pivot
				for (int j = k + 1; j < n; j++) {
					double q = - mat[j][k] / mat[k][k];
					for (int i = k; i <= n; i++)
						mat[j][i] += q * mat[k][i];
				}
			}
			etat = TRIANGULAIRE;
		}
	}

	public void resolution() {
		triangularisation();
		if (etat == RESOLU || etat == SINGULIER)
			return;
		// la solution est rangee a la place du second membre
		for (int i = n - 1; i >= 0; i--) {
			double s = 0;
			for (int j = i + 1; j < n; j++) {
				s += mat[i][j] * mat[j][n];
				mat[i][j] = 0;
			}
			mat[i][n] = (mat[i][n] - s) / mat[i][i];
			mat[i][i] = 1;
		}
		etat = RESOLU;
	}

	public double[] solution() {
		if (etat != RESOLU)
			resolution();
		if (etat != RESOLU)
			return null;
		double[] res = new double[n];
		for (int i = 0; i < n; i++)
			res[i] = mat[i][n];
		return res;
	}

	public void impression(java.io.PrintStream sortie) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= n; j++)
				sortie.print(cadreADroite(fmt.format(mat[i][j]), 12));
			sortie.println();
		}
		sortie.println();
	}

	public static void main(String[] args) {
		double a[][] = { {  1,  2,  3,  4 },
				{  5,  6,  0,  8 },
				{  9,  0, 11, 12 },
				{ 13, 14, 15, 16 } };
		double b[] = { 30, 49, 90, 150 };

		SystemeLineaire sys = new SystemeLineaire(a, b);
		sys.impression(System.out);






		double[] sol = sys.solution();
		if (sol == null)
			System.out.println("solution impossible");
		else
			for (int i = 0; i < sol.length; i++)
				System.out.println("sol[" + i + "] = " + fmt.format(sol[i]));
	}


	private static String cadreADroite(String s, int n) {
		s = "                                                          " + s;
		return s.substring(s.length() - n);
	}
}

