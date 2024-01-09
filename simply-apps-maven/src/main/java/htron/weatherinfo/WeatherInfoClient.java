package htron.weatherinfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import htron.FileHelper;

public class WeatherInfoClient {
    public static final WeatherInfoClient weatherApi = null;
    private JSONObject configJson;
    private JSONObject jsonStates;
    private JSONObject jsonRcvdOpenApi;
    private float longitude;
    private float latitude; 
    private ArrayList<String[]> routes = new ArrayList<>();
    private static final Map<String, String> map = new HashMap<>() ;
    private static final  String RAIN = "rain";
    private static final String SPEED = "speed";
    private static final String ALL = "all";
    private static final String PRESSURE = "pressure";
    private static final String SUNRISE = "sunrise";
    private static final String SUNSET = "sunset";
    private static final String HUMIDITY = "humidity";
    private static final String TEMP = "temp";

    WeatherInfoClient() {

        String filePath ;
        
        try {

            filePath = Paths.get(FileHelper.rootPath, "assets" , "config",  "weather_config.json").toAbsolutePath().normalize().toString();
            this.configJson = new JSONObject(  new String(Files.readAllBytes(Paths.get(filePath)))  );
            
            filePath = Paths.get(FileHelper.rootPath, "assets", "Search",  "states.json").toAbsolutePath().normalize().toString();

            this.jsonStates =  new JSONObject(  new String(Files.readAllBytes(Paths.get(filePath)))  );

        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
  
    }

    private JSONObject requestWeather(String urlQuery) {
        HttpURLConnection connection;
        BufferedReader rd;
        StringBuilder result;   
        URL url;
        String line;
        JSONObject rcvd; 

        result = new StringBuilder();
        rcvd = null; 

        try {

            url =  new URL(  urlQuery );
            
            connection = (HttpURLConnection) url.openConnection();
            rd = new BufferedReader(new InputStreamReader (connection.getInputStream()));
            
            while ((line = rd.readLine()) != null){
              result.append(line);
            }
            
            rcvd = new JSONObject(result.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } 
        

        return rcvd;
    }


    private void setLongitude (float l) {
        this.longitude = l;
    }

    private void setLatitude (float l) {
        this.latitude = l;
    }

    public void updateDayInfo(WeatherInfo weatherInfo) {

        // TEMP kelvin to farenheit
        weatherInfo.getTemp().updateLabelName( String.valueOf(String.valueOf( (int) (1.8* ( Double.valueOf(WeatherInfoClient.map.get(TEMP))-273) + 32)   )   ));
    
        //  HUMIDITY
        weatherInfo.getHumid().updateLabelName( String.valueOf(Double.valueOf(WeatherInfoClient.map.get(HUMIDITY))));

        // RAIN  
        if (WeatherInfoClient.map.get(RAIN) != null) {
            weatherInfo.getPercip().updateLabelName(  String.valueOf( (Integer.valueOf(WeatherInfoClient.map.get(RAIN)) )   ));
        } 

        // // windw 
        if (WeatherInfoClient.map.get(SPEED) != null) {
            weatherInfo.getWind().updateLabelName(  String.valueOf(Double.valueOf(WeatherInfoClient.map.get(SPEED)) ));
        } 
        
        // // cloud
        if (WeatherInfoClient.map.get(ALL) != null) {
            weatherInfo.getCloud().updateLabelName(  String.valueOf(Double.valueOf(WeatherInfoClient.map.get(ALL)) ));
        } 

          // // PRESSURE 
        if (WeatherInfoClient.map.get(PRESSURE) != null) {
            weatherInfo.getPressure().updateLabelName(  String.valueOf(Double.valueOf(WeatherInfoClient.map.get(PRESSURE)) ));
        } 

        // SUNRISE
        if (WeatherInfoClient.map.get(SUNRISE) != null) {
            weatherInfo.getSunrise().updateLabelName(  String.valueOf( weatherInfo.getSunrise().calculatedTime(Long.valueOf(WeatherInfoClient.map.get(SUNRISE)  )))  );
        } 
        
         // SUNSET
            if (WeatherInfoClient.map.get(SUNSET) != null) {
            weatherInfo.getSunset().updateLabelName(  String.valueOf( weatherInfo.getSunset().calculatedTime(Long.valueOf(WeatherInfoClient.map.get(SUNSET)    ))));
        } 

    }

    private void getStateDayMeasurementQuery (float longitude, float latitude) {
        String targetURL = "https://api.openweathermap.org/data/2.5/weather";
        String query = "lat="+latitude + "&" + "lon="+longitude + "&" + "appid=" +this.configJson.get("key") ;
        String q = targetURL + "?" + query; 
        
        this.jsonRcvdOpenApi = this.requestWeather(q);
        this.longitude = longitude;
        this.latitude = latitude; 
        
        this.routes.clear();
        this.routes.add( new String[] {"main", TEMP} ); //kelvin 
        this.routes.add( new String[] {"main", HUMIDITY} );
        this.routes.add( new String[] {"RAIN"} ); // volume for 1 hour, mm 
        this.routes.add( new String[] {"wind", SPEED} ); // meters /sec 
        this.routes.add( new String[] {"clouds", ALL} ); // percent 
        this.routes.add( new String[] {"main", PRESSURE} ); // percent hPa
        this.routes.add( new String[] {"sys", SUNRISE} ); // UTC
        this.routes.add( new String[] {"sys", SUNSET} ); // UTC
        this.routes.add( new String[] {"timezone"} ); // UTC
        
        this.parseDataRequest(); // maps hashmap 
        
    }

    public void getStateDayMeasurement(String stateName, WeatherInfo weatherInfo) {

        this.setLatitude((float) 0.0);
        this.setLongitude((float) 0.0);

        this.setLatitude(Float.valueOf(this.searchRoute( new String[] {stateName.toLowerCase(), "capital", "latitude"}) )); 

        this.setLongitude(Float.valueOf(this.searchRoute( new String[] {stateName.toLowerCase(), "capital", "longitude"}) )); 

        this.getStateDayMeasurementQuery(this.longitude, this.latitude); 

        this.updateDayInfo(weatherInfo); // updates weather icon(i,e, buffer) objects parameters

        // update gui 
        weatherInfo.updateGridUI();

    }
    
    public void getState30DayMeasurementQuery( ) {
        String q = "api.openweathermap.org/data/2.5/forecast/daily?lat=44.34&lon=10.99&cnt=7&appid=" + this.configJson.get("key") ;
        this.jsonRcvdOpenApi = this.requestWeather(q);
    }
    
    private String searchRoute(String [] route) {
        JSONObject jsonCurr; 
        Object obj;
        String value = "";
        
        jsonCurr =   this.jsonStates;
        
        for (int i = 0; i < route.length ; i++) {

            if ( jsonCurr.has(route[i] ) ) {
                
                obj = jsonCurr.get(route[i]);
                
                if ( !(obj instanceof JSONObject)  )  {
                    value =  String.valueOf( obj );
                } else  {
                        jsonCurr = (JSONObject) obj; 
                }
            }
        } 

        return value;
    }

    private void parseDataRequest() {
        String value = null;
        Object obj;
        JSONObject jsonCurr; 

        for (String[] route: this.routes ) {
            
            value = null; 
            jsonCurr = this.jsonRcvdOpenApi;
            
            for (int i = 0; i < route.length ; i++) {
                
                if ( jsonCurr.has(route[i] ) ) {
                    
                    obj = jsonCurr.get(route[i]);
                    
                    if ( !(obj instanceof JSONObject)  )  {
                        value =  String.valueOf( obj );
                    } else  {
                         jsonCurr = (JSONObject) obj ; 
                    }
                } 
            }                
            
            WeatherInfoClient.map.put ( route[route.length - 1],   value )  ;

        }  
    }
    
}