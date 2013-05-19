package testimage;

/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */

public class TopControl {
	public menu me;
	String path;
	String nom;
	int w;
	int h;

	public TopControl() {
	}

	public void MAJ(int w,int h){
		me.dispose();
		me=new menu(this,w,h);
		me.setVisible(true);
		me.ouvrirIm(path,nom);
		me.jComboNivResolution.setSelectedIndex(h-1);
//		me.jComboW.setSelectedIndex(w-1);
	}
}