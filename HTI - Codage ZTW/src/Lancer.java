import testimage.*;

/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Lancer {
	
	public Lancer() {
	}
	public static void main(String[] args) {
		Lancer lancer1 = new Lancer();
		TopControl top=new TopControl();
		menu men = new menu(top,4,4);
		men.show();
		top.me=men;

	}
}
