package htron.search;

import java.util.TimerTask;
import java.util.Map;
import java.util.Timer;

    class CountdownTimer<T> extends TimerTask {
        
        public static final  int  INITIAL_VALUE = 25; 
        private volatile int counter;
        private Object lockSubstitue;
        private Map< T ,Timer>  map;
        private T s; 
        private Callback<T> callback; 

        private final void setCounter(int value) {
            this.counter = value; 
        }

        public final int getCounter() {
            return this.counter; 
        }

        public CountdownTimer( T s, Map< T ,Timer>  map, Callback<T> callback) {
            super(); 
            this.counter = CountdownTimer.INITIAL_VALUE;
            this.lockSubstitue = new Object();
            this.map =   map;
            this.s =  s; 
            this.callback = callback; 
         }

        @Override
        public void run() {
            synchronized (lockSubstitue) {
                
                this.setCounter(this.counter - 1);

                if (this.getCounter() == 0) {
                    this.cback();
                }

                if (this.getCounter() == -30) {
                    this.resetCounter();
                }

            }
        }

        private void resetCounter() {
            synchronized (lockSubstitue) {
                this.counter = CountdownTimer.INITIAL_VALUE;
                this.map.remove(this.s);
                this.cancel();
            }
        }


        private void cback() {
            synchronized (lockSubstitue) {
                this.callback.execute(this.s);
            }
        }

            
}
