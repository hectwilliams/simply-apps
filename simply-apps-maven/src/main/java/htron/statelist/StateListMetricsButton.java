package htron.statelist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;

    public class StateListMetricsButton extends JLabel implements MouseListener {
        
        private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);;
        private static  Font boldfont = new Font("Dialog", Font.BOLD, 11);
        private static  Font defaultFont = new Font("Dialog", Font.PLAIN, 11);
        private static final float[] hsbSet = Color.RGBtoHSB(173,216,230,null); // sky blue
        private static final Color activeColor = Color.getHSBColor(hsbSet[0], hsbSet[1], hsbSet[2]);
        private JScrollBar scrollbar; 
        
        Color defaultBackgroundColor;
        StateListMetricsModule metricPanel;

        public final static Color getActiveColor () {
            return StateListMetricsButton.activeColor;
        }

        public StateListMetricsButton(String name , StateListMetricsModule metricPanel  )   {
            super(name);
            this.addMouseListener(this);    // register listener to JButton 
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setOpaque(true);
            this.metricPanel = metricPanel.getMetricPanel();
            this.setFont(defaultFont); 
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = this.getWidth();
            int height = this.getHeight();
            Dimension arcs = new Dimension(5,10);   //Border corners arcs {width,height}, change this to whatever you want
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);    //paint border
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            sched.schedule(  () -> {
                StateListMetricsButton tmp = (StateListMetricsButton) e.getSource();
                if (tmp.getBackground() != StateListMetricsButton.activeColor) {
                    
                    tmp.setFont(StateListMetricsButton.boldfont);
                    
                    sched.schedule(
                        
                    () -> {
                        
                        if (tmp.getText().equals( "Like") ) {
                            System.out.println("change the like");
                            ((StateListMetricsButton)tmp.getParent().getComponent(0)).setBackground(StateListMetricsButton.activeColor);
                            ((StateListMetricsButton)tmp.getParent().getComponent(1)).setBackground(this.defaultBackgroundColor);
                            metricPanel.testimonialList.setVisible(false);
                            metricPanel.graphy.setVisible(true);
                            scrollbar = metricPanel.getVerticalScrollBar();
                            scrollbar.setValue(scrollbar.getMaximum());
                            metricPanel.setVerticalScrollBar(scrollbar);

                        } else {
                            System.out.println("change the testimonial");
                            ((StateListMetricsButton)tmp.getParent().getComponent(1)).setBackground(StateListMetricsButton.activeColor);
                            ((StateListMetricsButton)tmp.getParent().getComponent(0)).setBackground(this.defaultBackgroundColor);
                            metricPanel.testimonialList.setVisible(true);
                            metricPanel.graphy.setVisible(false);
                        }
                        
                        tmp.setFont(StateListMetricsButton.defaultFont);

                    },100, TimeUnit.MILLISECONDS );

                }
            }, 0, TimeUnit.MILLISECONDS);
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            /*no op */
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            sched.schedule(  () -> {
                StateListMetricsButton tmp = (StateListMetricsButton) e.getSource();
                if (tmp.getBackground() != StateListMetricsButton.activeColor) {
                    ((StateListMetricsButton) e.getSource()).setFont(StateListMetricsButton.defaultFont);
                }
            }, 0, TimeUnit.MILLISECONDS);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            sched.schedule(  () -> {
                StateListMetricsButton tmp = (StateListMetricsButton) e.getSource();
                if (tmp.getBackground() != StateListMetricsButton.activeColor) {
                    ((StateListMetricsButton) e.getSource()).setBackground( Color.LIGHT_GRAY );
                }
            }, 0, TimeUnit.MILLISECONDS);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            sched.schedule(  () -> {
                  StateListMetricsButton tmp = (StateListMetricsButton) e.getSource();
                if (tmp.getBackground() != StateListMetricsButton.activeColor) {
                    ((StateListMetricsButton) e.getSource()).setBackground( this.defaultBackgroundColor );
                    ((StateListMetricsButton) e.getSource()).setFont(StateListMetricsButton.defaultFont);
                }
            }, 0, TimeUnit.MILLISECONDS);
        }
        
    }

