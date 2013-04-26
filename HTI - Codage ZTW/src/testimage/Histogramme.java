package testimage;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * <p>Title: TestImage</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Histogramme extends JFrame {
  Dimension d ;
  int oriX,oriY;
  String titre;
  Color colorTrace;
  int[] valeurHisto = new int[256];
  double entropie=0;

  public Histogramme(int[] donnee,String cible,Color colorTrace, double entropie) {

        PlaceFenetres.PlaceFenetre(1.0/3,1.0/3,this);
        this.valeurHisto=donnee;
        this.entropie=entropie;
        this.titre=cible;
        this.colorTrace=colorTrace;
        repaint();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

 private void jbInit() throws Exception {
    this.setBackground(Color.white);
    this.setTitle("Histogramme de l\'image");
  }


  public void paint(Graphics g){
    this.d=this.getSize();
    this.oriX=20;
    this.oriY=(int)d.getHeight()*4/5;
    this.setVisible(true);

    //Reinitialise affichage
    g.clearRect(0,0,(int)this.getSize().getWidth(),(int)this.getSize().getHeight());

    //Trace repere
    g.setColor(Color.black);
    g.drawLine(this.oriX,this.oriY,(int)this.getSize().getWidth()-20,this.oriY);
    g.drawLine(this.oriX, this.oriY+20,this.oriX,this.oriY-20);
    g.drawLine((int)this.getSize().getWidth()-20, this.oriY+20,(int)this.getSize().getWidth()-20,this.oriY-20);
    g.drawString("0",this.oriX,this.oriY+35);
    g.drawString("255",(int)this.getSize().getWidth()-35,this.oriY+35);

    //trace histogrammes
    traceHisto(this.valeurHisto,"ori",g);

    //Affiche entropie
    String txtEntrop="Entropie H(X) = "+this.entropie;
    int decal=(int)((this.getSize().getWidth()/2))-100;
    g.drawString(txtEntrop,decal,this.oriY+35);
  }



  /**
  * Trace histogramme
  * @param valeur Tableau d'entier de taille 256 contenant les donnees de l'histogramme
  * @param graphe Type d'histogramme a tracer : image originale ou image traitee
  * @param g  Conteneur graphique
  */
  public void traceHisto(int[] valeur,String graphe, Graphics g){

    //System.out.println(this.entropie);

    this.setTitle("Histogramme de l\'image "+this.titre);
      g.setColor(colorTrace);

    //Recupere la valeur max en ordonnee pour la mise a l'echelle du trace
    int max=0;
    float pasH=(float)(this.getSize().getWidth()-20-this.oriX)/256;

    for(int i=0;i<valeur.length;i++)
    {
      if(valeur[i]>max)
        max=valeur[i];
    }

    //Facteur de mise a l'echelle verticale du trace
    float rapport=((float)this.oriY-40f)/((float)max);

    for(int i=0;i<valeur.length;i++)
    {
        int X=(int)(this.oriX+i*pasH);
        if(i==255)
          X=(int)this.getSize().getWidth()-20;

        g.drawLine(X,this.oriY,X,(int)(this.oriY-((float)valeur[i])*rapport));
    }
  }




}