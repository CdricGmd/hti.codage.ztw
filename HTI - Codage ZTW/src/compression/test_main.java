/**
 * 
 */
package compression;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.xml.crypto.dsig.Transform;

import testimage.ImageIO;
import testimage.TraitImage;

/**
 * @author Cedric Golmard
 *
 */
public class test_main {

	private static DataInputStream dis;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		
		int height = 512, width = 512 ; 
		double[][]  xt =  new double[height][width];
		String file = "Lena512";
		ImageIO iIO = new ImageIO();
		Image image = iIO.OuvrirImage(file);
		BufferedImage buffImage = TraitImage.toBufferedImage(image);
		xt = TraitImage.getPixelTab(buffImage);
		
		
		
		//ouvrirOnd(file, file, xt, height, width );
		//Transform t = new Transform( new WaveletPacketTransform( new Haar02( ) ) );
		 //double[ ][ ] matHilb = t.forward( xt ); // 2-D WPT Haar forward

		    
		System.out.println("Ouverture de " + file);
		/*
		try {
			CodageZTW.ztw_code(xt, height, width, 4, 700, "lena512-2.ond.bitstream");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		System.out.println("Image codee !");
		
		double[][] image_reconstruite = new double[height][width];
		/*
		try {
			CodageZTW.ztw_decode(image_reconstruite, 512, 512, 4, "lena512-2.ond.bitstream");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		System.out.println("Image decodee !");
		//double[ ][ ] matReco = t.reverse( matHilb ); // 2-D WPT Haar reverse
	}
	
	/*
	static void ouvrirOnd(String path,String nom, double[][] xt, int h, int w) throws IOException{
		dis = new DataInputStream(new FileInputStream(new File(path)));
		for(int i =0; i < h; i++)
			for(int j=0; j<w; j++)
				if (dis.available() > 0){
					xt[i][j] = (double)dis.read();
					System.out.println("Debug xt " + i + " " + j + " " + xt[i][j]);
				}
	}
	*/
	//Bufferiser une image
		private static BufferedImage toBufferedImage(Image image) {
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
		
		private static double[][] getPixelTab(BufferedImage im){

			//Raster pour modifier l'image;
			WritableRaster raster=im.getRaster() ;
			int w=raster.getWidth();
			int h=raster.getHeight();
			//System.out.println("h = "+h+"    w = "+w+"\n");

			double[][] donnee=new double[w][h];

			for(int i=0;i<w;i++)
				for(int j=0;j<h;j++)
					donnee[i][j]=raster.getSample(i,j,0);

			return donnee;
		}
}

