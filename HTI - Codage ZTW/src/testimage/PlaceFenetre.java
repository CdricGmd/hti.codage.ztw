package testimage;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.border.*;


/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

abstract class PlaceFenetres {


	/**
	 * Placement et dimensionnement automatique de la fenetre
	 * @param Kw  Proportion de la largeur de la fenetre par rapport a la largeur de l'ecran
	 * @param Kh  Proportion de la hauteur de la fenetre par rapport a la hauteur de l'ecran
	 */
	public static void PlaceFenetre(double Kw,double Kh, JFrame fr){

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double w=screenSize.width * Kw;
		double h=screenSize.height * Kh;
		fr.setSize((int)w,(int) h);
		fr.setLocation(screenSize.width * 1 / 2-fr.getWidth()/2, screenSize.height * 1 / 2-fr.getHeight()/2);
	}

	public static void PlaceFenetreFix(double Kw,double Kh, JFrame fr){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		/*double w=screenSize.width * Kw;
    double h=screenSize.height * Kh;*/
		fr.setSize((int)Kw,(int) Kh);
		fr.setLocation(screenSize.width * 1 / 2-fr.getWidth()/2, screenSize.height * 1 / 2-fr.getHeight()/2);
	}


}