package testimage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */

public class CadreImage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1977104241047460485L;
	Dimension d ;
	int oriX,oriY;
	String titre;
	BufferedImage im;

	public CadreImage(BufferedImage im, String titre) {
		PlaceFenetres.PlaceFenetre(1.0/3,1.0/3,this);
		this.im=im;
		this.titre=titre;
		this.setBackground(Color.black);
		this.setResizable(false);

	}

	public void paint(Graphics g){
		this.d=this.getSize();
		this.oriX=70;
		this.oriY=60;
		this.setVisible(true);
		this.setTitle(this.titre);

		int w = this.im.getWidth()+20;
		int h =this.im.getHeight()+20;

		//RÈinitialise affichage
		g.clearRect(0,0,(int)this.getSize().getWidth(),(int)this.getSize().getHeight());

		//Affiche Image
		g.drawImage(this.im,this.oriX,this.oriY,w,h,null);

		this.setSize(w+140,h+100);
	}
}