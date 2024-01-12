package htron.search;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import htron.weatherinfo.Picture;
import htron.weatherinfo.WeatherInfo;

/**
 * 
 * The first event of many events seperated by nanoseconds to milliseconds is 
 * prioritized and handled. 
 * 
 */
public class DebounceFirst <T> {
    
    private long interval; 
    private Callback2<T> callback = null;
    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;

    public DebounceFirst(int interval, Callback2<T> callback) {
        this.interval = interval;
        this.callback = callback;
        this.scheduledFuture = null; 
    }

    public void terminate() {
        this.sched.shutdown();
    }

    public void call (WeatherInfo winfo, Picture pic) {
        if (this.scheduledFuture == null) {
            this.scheduledFuture = sched.schedule( new TimerTask(winfo, pic), this.interval, TimeUnit.MILLISECONDS);
        }
    }

    private class TimerTask implements Runnable {
        WeatherInfo w;
        Picture p;
        public TimerTask(WeatherInfo w, Picture pic) {
            this.w = w;
            this.p = pic;
        }

        public void run() {
            try {
                callback.execute(this.w, this.p);
            } catch (Exception e ) {
                e.printStackTrace();
                // System.out.println("Faulty callback");
            } 
        }
        
    }

}

