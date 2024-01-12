package htron.banner;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import htron.Windowise;

public class BannerPage extends JPanel {
    final static int N_SIZE = 150;
    Windowise w;
    public boolean ready = false; 
    public GridBagConstraints gbcBannerPage;
    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);

    public BannerPage(Windowise w) {
        this.gbcBannerPage = new GridBagConstraints();
        this.setVisible(false);
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        this.w = w;

        while (!this.w.getReadyState()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.setBannerPageCanvas(w);
        this.setBannerPageGrid();
        this.setVisible(true);
        this.ready = true; 

    } 

    private void setBannerPageCanvas (Windowise w) {
        this.gbcBannerPage.anchor = GridBagConstraints.FIRST_LINE_START;
        this.gbcBannerPage.weightx = 1;
        this.gbcBannerPage.weighty = 1;
        this.gbcBannerPage.fill = GridBagConstraints.BOTH;
        this.gbcBannerPage.gridx = 0;
        this.gbcBannerPage.gridy = 15;
        this.gbcBannerPage.ipadx = 0;
        this.gbcBannerPage.ipady = 0;
        this.gbcBannerPage.gridwidth = 170;
        this.gbcBannerPage.gridheight = 5;
        w.add( this , this.gbcBannerPage);
        w .setComponentZOrder( this , 1);
    }

    public void setBannerPageGrid () {

        for(int x = 0; x < BannerPage.N_SIZE; x++) {
           
           for(int y = 0; y < BannerPage.N_SIZE/4; y++) {

               GridBagConstraints gbc = new GridBagConstraints();
               gbc .anchor = GridBagConstraints.FIRST_LINE_START;
               gbc .weightx = 1;
               gbc .weighty = 1;
               gbc .fill = GridBagConstraints.BOTH;
               gbc.gridx = x;
               gbc.gridy = y;
               gbc.ipadx = 0;
               gbc.ipady = 0;

            //    JPanel cell = new JPanel();
            //    cell.setBorder(BorderFactory.createLineBorder( new Color(255, 255, 255, 100 )  , 1));
            //    this.add(cell, gbc);
            //    this.setComponentZOrder(cell, 0);

           }
       }

    }

}
