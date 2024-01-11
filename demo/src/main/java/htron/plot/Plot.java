package htron.plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

import htron.Windowise;

public class Plot extends JPanel {

    int [] cord = {65, 20, 40 , 80};
    int marg = 60; 
    Windowise windowise;

    public Plot(Windowise windowise) {
        this.graphit(windowise);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D graph = (Graphics2D) g; 
        
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  

        int width = this.getWidth();
        int height = this.getHeight();
        
        // vertical line 
        graph.draw (
            new Line2D.Double(marg, marg, marg, height - marg) 
        );

        //horizontal line 
        graph.draw (
            new Line2D.Double(marg, height- marg, width - marg, height - marg) 
        );
                                                               //       mar                         570-mar        570
        double x = (double)(width-2*marg)/(cord.length-1);     // |------|----------------------------|------------|
                                                               //        |                            |
                                                               //        |   0  |   1  |   2   | 3    |
        double scale = (double)(height-2*marg)/getMax();   

        //set color for points  
        graph.setPaint(Color.RED);  
    
        // set points to the graph  
        for(int i=0; i<cord.length; i++){  
            double x1 = marg + i * x ;  
            double y1 = height-marg-scale*cord[i];  
            graph.fill(new Ellipse2D.Double(x1 - 2,y1 - 2, 4, 4));  
        }  

    }

 //create getMax() method to find maximum value  
    private int getMax(){  
        int max = -Integer.MAX_VALUE;  
        for(int i=0; i<cord.length; i++){  
            if(cord[i]>max)  
                max = cord[i];  
        }  
        return max;  
    }         

    public void graphit (Windowise w) {
        GridBagConstraints gc = new GridBagConstraints();
        gc .anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 75;
        gc.gridy = 15;
        gc.ipadx = 0;
        gc.ipady = 0;
        gc.gridwidth = 30;
        gc.gridheight = 40;
        w.gridPanel.add( this  , gc);
        w.gridPanel.setComponentZOrder( this  , 1);
    }

}