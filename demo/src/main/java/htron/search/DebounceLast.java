package htron.search;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DebounceLast <T> {
    
    private long interval; 
    private Callback<T> callback = null;
    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    
    public DebounceLast(int interval, Callback<T> callback) {
        this.interval = interval;
        this.callback = callback;
    }

    private class TimerTask implements Runnable {
        private T s;

        public TimerTask(T s) {
            this.s = s;
        }

        @Override
        public void run() {
            try {
                callback.execute(this.s); 
            }  catch (Exception e ) {
                e.printStackTrace();
                // System.out.println("Faulty thread/callback");
            }  
        }
        
    }

    public void terminate() {
        this.sched.shutdown();
    }

    public void call (T s) {
        /* cancel scheduled threads that have not completed */
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
        }
        /* reschedule task  */
        this.scheduledFuture =  sched.schedule( new TimerTask(s), this.interval, TimeUnit.MILLISECONDS);
    }


}

