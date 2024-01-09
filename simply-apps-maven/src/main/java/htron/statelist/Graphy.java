package htron.statelist;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Graphy extends JPanel {
    StateLabel stateLabel;
    int marg = 30;
    int elementsNum = 3;
    public Graphy(StateLabel stateLabel) {
        this.stateLabel = stateLabel;
        this.setBorder(BorderFactory.createLineBorder(Color.gray));
    }
    
    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D graph2D = (Graphics2D) g.create(); // create new instance (safer)

        graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  

        int width = this.getWidth();
        int height = this.getHeight();
        double x1;
        double y1; 

        // vertical coordinate line 
        graph2D.draw(
            new Line2D.Double(
                //top marker
                marg, /** x1 */
                marg, /** y1 */
                
                //bottom marker
                marg, /** x2 */
                (double) height - marg /** y2 */
            ) 
        );
        
        // horizontal coordinate line 
        graph2D.draw(
            new Line2D.Double(
                //bottom marker
                marg, /** x1 */
                (double)  height - marg, /** y1 */
                
                //bottom-right marker
                (double) width - marg, /** x2 */
                (double) height - marg  /** y1 */
            ) 
        );

        double x = (double) (width - 2*marg) / elementsNum;

        double scaleFactor = (height - 2*marg) / this.getMax(); // mult y values by this to normalize amplitude samples 

        // paint point color (marker)
        graph2D.setPaint(Color.RED);

        // graph2D = (Graphics2D) g.create(); // create new instance (safer)

        // old stroke 
        Stroke oldStroke = graph2D.getStroke();

        for (int i =0; i < this.stateLabel.getIconCollectionLength(); i++) {
            x1 = marg + i * x;
            y1 = height - marg - (scaleFactor * this.stateLabel.getIcon(i).getCount());
            
            // plot sample point 
            graph2D.fill(new Ellipse2D.Double(x1 - 1,y1 - 1,2,2));

            //   Set the stroke of the copy, not the original 
            // Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);

            // String test 
            this.stringTest(g, this.stateLabel.getIcon(i).getID(), (int) x1, height - marg );
            
            // x_markers
            this.xMarkersTest(g, (int)x1, height - marg);

            // polygon 
            this.polygonTest(g, x1,  y1, marg, (double)height - marg , x * (i + 1));

            // set stroke 
            // graph2D.setStroke(dashed);
            
            graph2D.draw(
                new Line2D.Double(
                    x1, 
                    y1, 
                    x1 + x,
                    y1
                )
            );

             // set stroke back to original 
            graph2D.setStroke(oldStroke);
        }

    }    

    private double getMax() {
        double max = -Integer.MAX_VALUE / 1.0; 
        for (int i = 0; i < this.stateLabel.getIconCollectionLength() ; i++ ) {
            if (max <  this.stateLabel.getIcon(i).getCount() ) {
                max = this.stateLabel.getIcon(i).getCount() ;
            }
        }

        if (max == 0) {
            max = 1;
        }
        return max;
    }

    private void stringTest(Graphics g, String s, int x,int  y) {
        Graphics2D graph2D = (Graphics2D) g.create(); // create new instance (safer)
        graph2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        graph2D.drawString(s, x+10 , y + 10);
    } 

    private void xMarkersTest(Graphics g, int x,int  y) {
        Graphics2D graph2D = (Graphics2D) g.create(); // create new instance (safer)
        graph2D.setColor(Color.darkGray);
        graph2D.draw(
            new Line2D.Double(x , (double) y-5 ,x, (double)y +5)
        );
    } 

    private void polygonTest(Graphics g, double x,double  y, double xOrigin,  double  yOrigin, double xDelta) {
        Graphics2D graph2D = (Graphics2D) g.create(); // create new instance (safer)
        Polygon p = new Polygon();
        
        p.addPoint((int)x, (int)yOrigin);
        p.addPoint( (int)x,  (int)y);
        p.addPoint( (int)xDelta, (int)y);
        p.addPoint( (int)xDelta,(int) yOrigin);
        
        graph2D.drawPolygon(p);
        graph2D.setColor(Color.ORANGE);
        graph2D.fillPolygon(p);
        graph2D.setColor(Color.GRAY);

        graph2D.dispose();
    } 

}
