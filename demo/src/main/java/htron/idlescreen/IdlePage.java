package htron.idlescreen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;

import htron.Windowise;

public class IdlePage extends JLabel {

    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    
    class MouseListenerT implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }

    public class WaitIcon extends JPanel {
        public boolean ready = false;
        private ArrayList<Double> buffer;
        Windowise w; 
        Graphics g; 
        ScheduledFuture<?> future;
 
        int index; 
        boolean enablePaint = false; 
        Double centerX, centerY;
        int width, height; 
        double r; 
        int count = 0;

        double xx,yy, xpos, factorY, factorX, x, y; 
        public boolean searchStatusReady = false;

        public WaitIcon(Windowise w) {
            this.setLayout(new GridBagLayout());
            this.setOpaque(true);
            this.setObject(w);
            this.g = null;
            this.w = w; 
            this.buffer = new ArrayList<Double>();
            this.ready = true;
            this.searchStatusReady = false;
            this.index = 1;
            this.centerX = null; 
            this.workerRun();
        }

        protected void paintComponent(Graphics g) {
            this.g = g;

            if (this.centerX == null) {
                this.width = this.getWidth();
                this.height = this.getHeight();
                this.r = 10.0; 
                this.centerX = width / 2.0 ;
                this.centerY = height / 2.0 ;
            }

            if (this.index == 5) {
                this.index = 1;
            }
            
            if (!this.future.isDone()) {
                this.runWaitIcon(g, centerX, centerY, r,this.index, Color.GRAY);
            }

            this.index++;

        }

        private void workerRun() {

            this.future = sched.scheduleAtFixedRate( 
                () -> {
                    this.repaint();
                }
            , 500, 250, TimeUnit.MILLISECONDS);

        }

        private void setObject(Windowise w) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc .anchor = GridBagConstraints.FIRST_LINE_START;
            gbc .weightx = 1;
            gbc .weighty = 1;
            gbc .fill = GridBagConstraints.BOTH;
            gbc.gridx = 50;
            gbc.gridy = 25;
            gbc.gridheight = 10;
            gbc.gridwidth= 10 ;
            w.add(this, gbc);
            w.setComponentZOrder(this, 1);
        }

        private void runWaitIcon(Graphics g, double centerX, double centerY, double r, int quandrant, Color color) {

            if (this.w.search != null) {

                if (this.w.search.ready ) {

                    this.w.banner.setVisible(true);

                    while (!this.w.banner.isEnabled()) {} // wait for banner 

                    this.future.cancel(true);
                    this.buffer.clear();

                    this.drawQuadrant( (Graphics2D) g.create(), r, centerX, centerY, -1, this.buffer, Color.RED);
                    this.drawQuadrant( (Graphics2D) g.create(), r, centerX, centerY, -2, this.buffer, Color.GREEN);
                    this.drawQuadrant( (Graphics2D) g.create(), r, centerX, centerY, -3, this.buffer, new Color(255, 215, 0));
                    this.drawQuadrant( (Graphics2D) g.create(), r, centerX, centerY, -4, this.buffer, Color.MAGENTA);

                    // make weather UI components visible 
                    this.w.search.setVisible(true);
                    this.w.weatherinfo.setVisible(true);
                }
                
            } else {

                this.drawQuadrant( (Graphics2D) g.create(), r, centerX, centerY, quandrant, this.buffer, color);
            }
        }

        private void drawQuadrant(Graphics2D graphic2D, double r, double centerX, double centerY, int quadrant, ArrayList<Double> buffer, Color color) { 
        
            // clear painted x,y buffer 
                this.x = 0.0;
                this.y = 0.0;
                this.factorY = 0; 
                this.factorX = 0; 
                this.xpos  = 0;

                if (quadrant > 1 ) {
                    
                    graphic2D = (Graphics2D) graphic2D.create();
                    graphic2D.setPaint(Color.LIGHT_GRAY); 

                    while(buffer.size() != 0) {
                        xx = buffer.remove(0);
                        yy = buffer.remove(0);
                        graphic2D.fill(new Ellipse2D.Double( xx, yy ,2, 2));  // center 
                    }

                } 
                
                
                graphic2D = (Graphics2D) graphic2D.create();
                graphic2D.setPaint(color);
                
                quadrant = Math.abs(quadrant);

                if (quadrant == 1) {
                    factorX = 1;
                    factorY = -1;
                    xpos = centerX + r;
                }
                
                if (quadrant == 2) {
                    factorX = -1;
                    factorY = -1;
                    xpos = centerX - r;
                }
                
                if (quadrant == 4) {
                    factorX = 1;
                    factorY = 1;
                    xpos = centerX + r;
                }
                
                if (quadrant == 3) {
                    factorX = -1;
                    factorY = 1;
                    xpos = centerX - r;
                }
                
                for (Double x_ = 0.0; (quadrant == 4 || quadrant == 1) ? x < xpos :x == 0 || x > xpos ; x_+= 1e-3 ) {
                    x = (centerX + factorX * x_) * 1.00;
                    y = centerY + factorY * Math.sqrt( Math.pow(r,2.0) - Math.pow(x - centerX, 2.0) ) ;
                    buffer.add(x);
                    buffer.add(y);
                    graphic2D.fill(new Ellipse2D.Double( x , y  ,2, 2));  
                }   
        
            
        }

    }
    
    String imgUrl;
    public WaitIcon waitIcon;
    Image img ;

    public IdlePage( Windowise w) {
        this.setLayout(new GridBagLayout());
        // imgUrl = Paths.get(FileHelper.getWorkingDirectoryPath(), "IdleScreen", "space.png").toAbsolutePath().normalize().toString();
        this.setOpaque(true);
        sched.schedule(()-> {
            this.setGrid(w);
            this.waitIcon = new WaitIcon(w);
        }
        , 0, TimeUnit.MILLISECONDS);

    }


    public void paintComponent (Graphics g) {
        g.drawImage(this.img, 0, 0, null);
    }

    private void setGrid(Windowise w) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc .anchor = GridBagConstraints.FIRST_LINE_START;
        gbc .weightx = 1;
        gbc .weighty = 1;
        gbc .fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 75;
        gbc.gridwidth= 150;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        w.add(this, gbc);
        w.setComponentZOrder(this, 0);
    }

  

  

}
