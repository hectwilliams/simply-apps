package htron.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import htron.weatherinfo.WeatherInfo;

public class SearchTextArea extends JPanel{

    SearchTextAreaField field;
    int pixelsPerCellX = 5; 
    int pixelsPerCellY = 8;

    public class SearchTextAreaField extends JTextField { 
    
        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            
            Dimension arcs = new Dimension(7,7); //Border corners arcs {width,height}, change this to whatever you want

            int width = this.getWidth();
            int height = this.getHeight();
            
            Graphics2D graphics = (Graphics2D) g;
            
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);    //paint border

        }
        
        Queue<Long> queue;
        int pixelsPerCellX = 10; 
        int pixelsPerCellY = 8;


        public SearchTextAreaField(String str, SearchResults searchResults, WeatherInfo weatherinfo) {
            
            super(10);

            this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN,10));
            this.setMinimumSize(new Dimension(this.pixelsPerCellX, this.pixelsPerCellY));

            queue = new LinkedList<>();

            this.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 180 )));
            
            this.getDocument().addDocumentListener(new DocumentListenerV2( searchResults));

            this.addMouseListener(  new MouseListener() {
                
                @Override
                public void mouseClicked(MouseEvent e) { /* no op */ }

                @Override
                public void mousePressed(MouseEvent e) { /* no op */ }

                @Override
                public void mouseReleased(MouseEvent e) { /* no op */ }

                @Override
                public void mouseEntered(MouseEvent e) {
                    JTextField field = (JTextField) e.getSource();
                    field.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 0 )));
                };

                @Override
                public void mouseExited(MouseEvent e) {
                    JTextField field = (JTextField) e.getSource();
                    field.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 180 )));
                }
        
            });
        }
    }

    public SearchTextArea(String str, SearchResults searchResults, WeatherInfo weatherinfo) {
        
        this.field = new SearchTextAreaField(str, searchResults, weatherinfo);

        Dimension d = new Dimension(this.pixelsPerCellX, this.pixelsPerCellY);
        this.setMinimumSize(d);
        this.setMaximumSize(d);
        this.setPreferredSize(d);

        this.add(this.field);

        this.setAlignmentY(SwingConstants.TOP);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.revalidate();
    }   

    public void hide( ) {
        this.field.setVisible(false);
        // this.revalidate();
        // this.repaint();
    }

    public void show( ) {
        this.field.setVisible(true);
        // this.revalidate();
        // this.repaint();
    }

}
