package htron;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class FocusListenerApp implements FocusListener {

    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    Windowise w;

    public FocusListenerApp(Windowise w) {
        this.w = w;
    }

    @Override
    public void focusGained(FocusEvent e) {
        w.frame.pack();
    }

    @Override
    public void focusLost(FocusEvent e) {
        // nothing
    }

}
