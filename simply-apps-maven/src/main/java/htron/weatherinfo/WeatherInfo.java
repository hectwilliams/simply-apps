package htron.weatherinfo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import htron.Windowise;


public class WeatherInfo extends JPanel{
    GridBagConstraints gd = null;
    
    public static final WeatherInfoClient weatherApi = new WeatherInfoClient(); 

    private WeatherMeasureIcon temp;
    private WeatherMeasureIcon humidity;
    private WeatherMeasureIcon percipitation;
    private WeatherMeasureIcon wind; 
    private WeatherMeasureIcon cloud; 
    private WeatherMeasureIcon pressure;
    private WeatherMeasureIcon sunrise;
    private WeatherMeasureIcon sunset;
    
    public final Component[] componentActiveList = new Component[]{null};

    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    static final int N_SIZE = 50;  
    public static final  Dimension DIMENSION = new Dimension(200, 100); 
    
    public WeatherInfo (Windowise w) {

        super(new GridLayout(3,3));
        
        this.setVisible(false);
        
        this.setTemp();
        this.setHumid();

        this.setPercip();
        this.setWind();
        this.setCloud();
        this.setPressure();
        this.setSunrise();
        this.setSunset();

        this.componentActiveList [0] = (this);

        this.setWeatherInfoWindow(w);
        
        this.windowFutures();

    }

    private void setTemp() {
        this.temp = new WeatherMeasureIcon("temp");
    }

    public final WeatherMeasureIcon getTemp() {
        return this.temp;
    }
    

    private void setHumid() {
        this.humidity = new WeatherMeasureIcon("humid");
    }

    public final WeatherMeasureIcon getHumid() {
        return this.humidity;
    }

    private void setPercip() {
        this.percipitation = new WeatherMeasureIcon("percip");
    }

    public final WeatherMeasureIcon getPercip() {
        return this.percipitation;
    }
    
    private void setWind() {
        this.wind = new WeatherMeasureIcon("wind");
    }

    public final WeatherMeasureIcon getWind() {
        return this.wind;
    }

  private void setCloud() {
        this.cloud = new WeatherMeasureIcon("cloud");
    }

    public final WeatherMeasureIcon getCloud() {
        return this.cloud;
    }

    private void setPressure() {
        this.pressure = new WeatherMeasureIcon("pressure");
    }

    public final WeatherMeasureIcon getPressure() {
        return this.pressure;
    }


    private void setSunrise() {
        this.sunrise = new WeatherMeasureIcon("sunrise");
    }

    public final WeatherMeasureIcon getSunrise() {
        return this.sunrise;
    }

    private void setSunset() {
        this.sunset = new WeatherMeasureIcon("sunset");
    }

    public final WeatherMeasureIcon getSunset() {
        return this.sunset;
    }

    public void setWeatherInfoWindow(Windowise w) {
        GridBagConstraints gc = new GridBagConstraints();
        gc .anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx  = 80;
        gc.gridy = 20;
        gc.ipadx = 0;
        gc.ipady = 0;
        gc.gridwidth = 21;
        gc.gridheight = 27;
        w.add( this , gc);
        w.setComponentZOrder( this , 0);
    }

     public void windowFutures() {

        ScheduledFuture<?>[] futures =  {
            sched.schedule(() -> { this.add(new WeatherMeasure(this.temp)); } , 0, TimeUnit.MILLISECONDS) ,
            sched.schedule(() -> { this.add(new WeatherMeasure(this.humidity)); } , 0, TimeUnit.MILLISECONDS) ,
            sched.schedule(() -> { this.add(new WeatherMeasure(this.percipitation)); } , 0, TimeUnit.MILLISECONDS) ,
            sched.schedule(() -> { this.add(new WeatherMeasure(this.wind)); } , 0, TimeUnit.MILLISECONDS) ,
            sched.schedule(() -> { this.add(new WeatherMeasure(this.cloud)); } , 0, TimeUnit.MILLISECONDS) ,
            sched.schedule(() -> { this.add(new WeatherMeasure(this.pressure)); } , 0, TimeUnit.MILLISECONDS) 
        };

        sched.schedule (

            () -> {
                
                try {

                    int sum = 0;

                    while ( sum != futures.length) {

                        sum = 0;

                        for (ScheduledFuture<?> f: futures) {
                            sum +=  (f.isDone()) ? 1: 0;
                        }
                    }     
                    
                    /*  sleep thread, allow other threads to work  */
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    /* interrupted thread currently handling above interrupt */
                    e.printStackTrace();
                    Thread.currentThread().interrupt(); 
                } catch (SecurityException  ee) {
                    /* security interrupt, interrupted have dwelled */
                    ee.printStackTrace();
                }

            }, 0, TimeUnit.MILLISECONDS );
        
    }

    public void updateGridUI () {
        for(Component c : this.getComponents()){
            ((WeatherMeasure) c).updateValue();
        }
    }

}

