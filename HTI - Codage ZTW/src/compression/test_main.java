/**
 * 
 */
package compression;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

import testimage.ImageIO;
import testimage.TraitImage;
import testimage.*;

/**
 * @author Cedric
 *
 */
public class test_main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ouvrir Lena512.4.ond
		double xt[][] = null;
		int height = 0, width =0 ; 
		ouvrirIm("Lena512.4.ond", "Lena512.4.ond", xt, height, width );
		try {
			CodageZTW.ztw_code(xt, height, width, 4, 200, "lena512.4.ond.bitstream");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double[][] image_reconstruite = null;
		try {
			CodageZTW.ztw_decode(image_reconstruite, 512, 512, 4, "lena512.4.ond.bitstream");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void ouvrirIm(String path,String nom, double[][] xt, int h, int w){
		if ((path != "")&&(path!=null)) {
			System.out.println("Fichier :" + nom + "\nChemin : " +
					path + "\n");
		}
		else {
			System.out.println("Aucune selection\n");
			return;
		}
		//Image d'origine
		ImageIO mon_image = new ImageIO();
		Image img_ori = mon_image.OuvrirImage(path);
		h = mon_image.getHeigth();
		w = mon_image.getWidth();

		//Image bufferisee
		BufferedImage bufIm=toBufferedImage(img_ori);
		xt = TraitImage.getPixelTab(bufIm);

	}
	
	//Bufferiser une image
	static BufferedImage toBufferedImage(Image image) {
		/* On test si l'image n'est pas deja une instance de BufferedImage */
		if( image instanceof BufferedImage ){
			/* cool, rien a faire */
			return( (BufferedImage)image );
		} else{
			/* On s'assure que l'image est completement chargee */
			image = new ImageIcon(image).getImage();

			/* On cree la nouvelle image */
			BufferedImage bufferedImage = new BufferedImage( image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_GRAY );
			Graphics g = bufferedImage.createGraphics();
			g.drawImage(image,0,0,null);
			g.dispose();

			return( bufferedImage );
		}
	}

}
