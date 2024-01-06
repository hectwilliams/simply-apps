package htron;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;

import htron.Windowise;
import htron.Banner;

public class MouseListenerBanner implements MouseListener{
    
    Banner banner; 
    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    Windowise w;    
    Object lock = new Object();;

    public MouseListenerBanner(Windowise w) {
        super();
        this.w = w;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel label = ((JLabel) e.getSource());

        if (  label.isEnabled()  ) {

            sched.schedule( () -> {
                synchronized(this.lock) {
                    this.w.banner.currPageName = label.getText().toLowerCase();
                    
                    if (this.w.banner.prevPageName   != this.w.banner.currPageName) {
                        
                        switch(this.w.banner.prevPageName) {
                            case "statelist" :
                                this.w.stateList.setVisible(false);
                                break;
                    
                            case "weather" : 
                                this.w.weatherInfo.setVisible(false); //  componentActiveList[0].setVisible(false);
                                this.w.search.setVisible(false);
                                break;
                        }

                        switch(this.w.banner.currPageName) {
                            case "statelist" :
                                this.w.stateList.setVisible(true);
                                this.w.stateList.requestFocus();
                                break;
                    
                            case "weather" : 
                                this.w.weatherInfo.setVisible(true); //  componentActiveList[0].setVisible(false);
                                this.w.search.setVisible(true);
                                break;
                        }
                        this.w.frame.pack();
                        this.w.banner.prevPageName =  this.w.banner.currPageName ;
                    }
            
                }
            }, (long) 0, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JLabel label = ((JLabel) e.getSource());

        if (  label.isEnabled()  ) {
            sched.schedule(() -> {
                label.setForeground(Color.LIGHT_GRAY);
            }, (long) 0, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JLabel label = ((JLabel) e.getSource());

        if (  label.isEnabled()  ) {
            sched.schedule(() -> {
                label.setForeground(null);
            }, (long) 0, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel label = ((JLabel) e.getSource());
        
        if (  label.isEnabled()  ) {
            sched.schedule(() -> {
                label.setFont((new Font(Font.SERIF, Font.BOLD, 13)));
            }, (long) 0, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel label = ((JLabel) e.getSource());

        if (  label.isEnabled()  ) {
            sched.schedule(() -> {
                label.setFont((new Font(Font.SERIF, Font.PLAIN, 13)));
            }, (long) 0, TimeUnit.MILLISECONDS);
        }
    }

}
