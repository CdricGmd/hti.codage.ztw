package testimage;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MonPane extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  private Toolkit t;
  private Image i;


  public MonPane() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    this.setLayout(borderLayout1);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    //unJeu.laBalle.dessineToi(g);
    }



  public void setImage(Image im){
    this.i=im;
  }


}