package htron.weatherinfo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

public class Picture extends JPanel{
    
    String path; 
    
    Image  img; 
    BufferedImage  bimg; 
    Graphics  gprime;

    int w;
    int h;
    Dimension dpanel = null;
    int pixelsPerCellX = 20; 
    int pixelsPerCellY = 11;


    public Picture ( String filePathImg) {
        Graphics g; 

        if (filePathImg == null) {
            this.bimg =  new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            g = this.bimg.createGraphics();
            g.setColor(new Color(255, 255, 255, 0  )); 
            g.fillRect(0, 0, 100, 100);
            this.img = this.bimg;
            
        } else {

            this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
            this.setPreferredSize(new Dimension(400, 300));
            
            if (filePathImg.length() > 0) {
                try {
                    img = ImageIO.read(new File(  filePathImg)  ) ; 
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

    }

    public Picture (int width, int height, String filePathImg) {


         if (filePathImg != null) {

            this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
            this.setPreferredSize(new Dimension(this.pixelsPerCellX * width , this.pixelsPerCellY * height));
            
            if (filePathImg.length() > 0) {

                try {
                    img = ImageIO.read(new File(  filePathImg)  ) ;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } 
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        Dimension d = this.getSize();;
        super.paintComponent(g);
        this.gprime = g; 
        g.drawImage(this.img, 0, 0, d.width, d.height, this );
        this.revalidate();
    }

     public void updateImg (String fileString) {
      
         try {
             this.img = ImageIO.read(new File(  fileString)  );
          
             // ensure component was initialized with initial paintComponent call 
             if (this.gprime != null) {
                 this.paintComponent(this.getGraphics());
             }        
             this.revalidate();

         } catch (IOException e) {
             e.printStackTrace();
         }

     }

     public void  setDim(int x, int y) {
        dpanel.setSize(x, y);
     }
    
}
