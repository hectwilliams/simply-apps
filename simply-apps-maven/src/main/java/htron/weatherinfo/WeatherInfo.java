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

    private final Component[] componentActiveList = new Component[]{null};

    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    static final int N_SIZE = 50;  
    public static final  Dimension DIMENSION = new Dimension(200, 100); 
    
    public WeatherInfo (Windowise w) {

        super(new GridLayout(3,3));
        
        this.setVisible(false);
        
        this.setTemp(WeatherInfoClient.weatherApi);
        this.setpercip(WeatherInfoClient.weatherApi);
        this.setwind(WeatherInfoClient.weatherApi);
        this.setcloud(WeatherInfoClient.weatherApi);
        this.setpressure(WeatherInfoClient.weatherApi);
        this.setsunrise(WeatherInfoClient.weatherApi);
        this.setsunset(WeatherInfoClient.weatherApi);

        this.componentActiveList [0] = (this);

        this.setWeatherInfoWindow(w);
        
        this.windowFutures();

    }

    public void setTemp(WeatherInfoClient weatherApiClient) {
        this.temp = new WeatherMeasureIcon("temp", weatherApiClient);
    }
    
    public void setpercip(WeatherInfoClient weatherApiClient) {
        this.temp = new WeatherMeasureIcon("percip", weatherApiClient);
    }

    public void setwind(WeatherInfoClient weatherApiClient) {
        this.temp = new WeatherMeasureIcon("wind", weatherApiClient);
    }

    public void setcloud(WeatherInfoClient weatherApiClient) {
        this.temp = new WeatherMeasureIcon("cloud", weatherApiClient);
    }

    public void setpressure(WeatherInfoClient weatherApiClient) {
        this.temp = new WeatherMeasureIcon("pressure", weatherApiClient);
    }

    public void setsunrise(WeatherInfoClient weatherApiClient) {
        this.temp = new WeatherMeasureIcon("sunrise", weatherApiClient);
    }

    public void setsunset(WeatherInfoClient weatherApiClient) {
        this.temp = new WeatherMeasureIcon("sunset", weatherApiClient);
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

