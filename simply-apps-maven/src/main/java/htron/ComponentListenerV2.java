package htron;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

public class ComponentListenerV2  implements ComponentListener {
    
    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> future;
    GraphicsDevice gd;
    DisplayMode dm;
    JFrame frame = null;
    
    public ComponentListenerV2 () {
        super(); 

        this.gd =  GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        this.dm =  this.gd.getDisplayMode();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        this.handler(e);
    }

    @Override
    public void componentMoved(ComponentEvent e  ) {
        this.handler(e);
     }

    @Override
    public void componentShown(ComponentEvent e) {
      // Event not monittored
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // Event not monittored
    }

    public void handler(ComponentEvent e) {

        if (this.frame == null) {

            this.frame = (JFrame) e.getSource();
            
            if (this.future != null)
                this.future.cancel(true);
            
            this.future =  sched.schedule(( ()-> {
            
                this.frame.setSize(this.dm.getWidth(), this.dm.getHeight());    
                
            }), 1000, TimeUnit.MILLISECONDS);
        }

    }

}
