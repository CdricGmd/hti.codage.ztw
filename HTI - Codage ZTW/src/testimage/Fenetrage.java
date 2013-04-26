package testimage;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */

public class Fenetrage extends JPanel {
  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();
  TitledBorder titledBorder1;
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JCheckBox jCheckBox1 = new JCheckBox();
  int w;
  int h;

  public Fenetrage(int w, int h) {
    try {
      this.w=w;
      this.h=h;
      jbInit(w, h);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit(int w, int h) throws Exception {


    gridLayout2.setColumns(this.w);
    gridLayout2.setHgap(5);
    gridLayout2.setRows(this.h);
    gridLayout2.setVgap(5);
    this.setLayout(gridLayout2);

    for(int i =1;i<(this.w*this.h);i++)
    {
      JCheckBox temp = new JCheckBox();
      temp.setName("C"+i);
      temp.setHorizontalAlignment(SwingConstants.CENTER);
      temp.setText("");
      this.add(temp, null);
      //buttonGroup1.add(temp);
    }
    JLabel croix = new JLabel("X",SwingConstants.CENTER);
    this.add(croix, null);

  }

  public Fenetrage() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    jCheckBox1.setText("jCheckBox1");
    this.setFont(new java.awt.Font("Dialog", 1, 11));
    this.setBorder(BorderFactory.createLoweredBevelBorder());
    gridLayout2.setColumns(3);
    gridLayout2.setHgap(5);
    gridLayout2.setRows(3);
    gridLayout2.setVgap(5);
    this.setLayout(gridLayout2);

  }

  protected void refait(){
    try {
     jbInit(w, h);
   }
   catch(Exception e) {
     e.printStackTrace();
   }
  }
}