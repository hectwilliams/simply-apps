package htron.search;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import htron.weatherinfo.Picture;

public class SearchScrollBlock extends JScrollPane {
    final public JPanel client;
    GridBagLayout gridBagLayout = null;


    public SearchScrollBlock() {
        this.client = new JPanel ();
        this.client.setVisible(true);
        this.client.setLayout( new  BoxLayout(this.client, BoxLayout.Y_AXIS) );
        this.getVerticalScrollBar().setUnitIncrement(10);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.setWheelScrollingEnabled(true);
        this.setViewportView(this.client);  // set viewpointview 
    }

    public void addChildToView (Picture pic) {

        JPanel pp = new JPanel();
        
        // pp.setVisible(false);
        // pp.setPreferredSize(Picture.DIMENSION);
        // pp.setMaximumSize(new Dimension( Picture.DIMENSION));

        pp.setSize(100, 100);
        pp.setLayout( new GridBagLayout());
        pp.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int i = 0 ; i < 10; i++) {
            for (int j = 0 ; j < 10; j++) {
                GridBagConstraints gc = new GridBagConstraints();
                gc .anchor = GridBagConstraints.FIRST_LINE_START;
                gc .weightx = 1;
                gc .weighty = 1;
                gc .fill = GridBagConstraints.BOTH;
                gc.gridx = i;
                gc.gridy = j;
                gc.ipadx = 0;
                gc.ipady = 0;
                JPanel p = new JPanel();
                p.setBorder( BorderFactory.createLineBorder(new Color(255, 255, 255, 0  ) ) );
                p.setVisible(false);
                pp.add(p, gc);
            }
        }

        GridBagConstraints gc = new GridBagConstraints();
        gc .anchor = GridBagConstraints.FIRST_LINE_START;
        gc .weightx = 1;
        gc .weighty = 1;
        gc .fill = GridBagConstraints.BOTH;
        gc.gridheight = 10;
        gc.gridwidth =  8;
        gc.ipadx = 0;
        gc.ipady = 0;
        // write c to pp via GridBag
        pp.add( pic, gc);
        this.client.add(pp);
    }

    public void clearView () {
        this.client.removeAll(); 
    }

}
