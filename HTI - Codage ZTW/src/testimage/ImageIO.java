package testimage;

import java.awt.*;
import java.awt.image.*;


/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ImageIO {

  private Image image;
  private int width;
  private int heigth;


  public ImageIO() {
  }

  public Image OuvrirImage(String path){
  image=Toolkit.getDefaultToolkit().createImage(path);
  System.out.println(image);
  return image;
  }

  public int getHeigth(){
    return this.heigth;
  }

  public int getWidth(){
    return this.width;
  }
}