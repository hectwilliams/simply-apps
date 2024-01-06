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
import htron.weatherinfo.*;

public class WeatherInfoClient {
    public static final WeatherInfoClient weatherApi = null;
    private JSONObject configJson;
    private JSONObject jsonStates;
    private JSONObject jsonRcvdOpenApi;
    private float longitude;
    private float latitude; 
    private ArrayList<String[]> routes = new ArrayList<>();
    public static  Map<String, String> map = new HashMap<>() ;

    WeatherInfoClient() {

        String filePath ;
        
        try {

            filePath = Paths.get(FileHelper.getWorkingDirectoryPath(), "../", "config",  "weather_config.json").toAbsolutePath().normalize().toString();
            this.configJson = new JSONObject(  new String(Files.readAllBytes(Paths.get(filePath)))  );
            
            filePath = Paths.get(FileHelper.getWorkingDirectoryPath(), "Search",  "states.json").toAbsolutePath().normalize().toString();

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
        } finally {

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

        // temp kelvin to farenheit
        weatherInfo.temp.updateLabelName( String.valueOf(String.valueOf( (int) (1.8* ( Double.valueOf(this.map.get("temp"))-273) + 32)   )   ), null);
    
        //  humidity
        weatherInfo.humidity.updateLabelName( String.valueOf(Double.valueOf(this.mapnfoClient.map.get("humidity"))), null);

        // rain  
        if (WeatherInfoClient.map.get("rain") != null) {
            weatherInfo.percipitation.updateLabelName(  String.valueOf( (Integer.valueOf(WeatherInfoClient.map.get("rain")) )   ), null);
        } 

        // // windw 
        if (WeatherInfoClient.map.get("speed") != null) {
            weatherInfo.wind.updateLabelName(  String.valueOf(Double.valueOf(WeatherInfoClient.map.get("speed")) ), null);
        } 
        
        // // cloud
        if (WeatherInfoClient.map.get("all") != null) {
            weatherInfo.cloud.updateLabelName(  String.valueOf(Double.valueOf(WeatherInfoClient.map.get("all")) ), null);
        } 

          // // pressure 
        if (WeatherInfoClient.map.get("pressure") != null) {
            weatherInfo.pressure.updateLabelName(  String.valueOf(Double.valueOf(WeatherInfoClient.map.get("pressure")) ), null);
        } 

        // sunrise
        if (WeatherInfoClient.map.get("sunrise") != null) {
            weatherInfo.sunRiseTime.updateLabelName(  String.valueOf( weatherInfo.sunRiseTime.calculatedTime(Long.valueOf(WeatherInfoClient.map.get("sunrise")  )))  , null);
        } 
        
         // sunset
            if (WeatherInfoClient.map.get("sunset") != null) {
            weatherInfo.sunSetTime.updateLabelName(  String.valueOf( weatherInfo.sunSetTime.calculatedTime(Long.valueOf(WeatherInfoClient.map.get("sunset")    ))), null);
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
        this.routes.add( new String[] {"main", "temp"} ); //kelvin 
        this.routes.add( new String[] {"main", "humidity"} );
        this.routes.add( new String[] {"rain"} ); // volume for 1 hour, mm 
        this.routes.add( new String[] {"wind", "speed"} ); // meters /sec 
        this.routes.add( new String[] {"clouds", "all"} ); // percent 
        this.routes.add( new String[] {"main", "pressure"} ); // percent hPa
        this.routes.add( new String[] {"sys", "sunrise"} ); // UTC
        this.routes.add( new String[] {"sys", "sunset"} ); // UTC
        this.routes.add( new String[] {"timezone"} ); // UTC
        
        this.parseDataRequest(); // maps hashmap 
        
    }

    public void getStateDayMeasurement(String stateName, WeatherInfo weatherInfo) {

        this.setLatitude((float) 0.0);;
        this.setLongitude((float) 0.0);

        this.setLatitude(Float.valueOf(this.searchRoute( new String[] {stateName.toLowerCase(), "capital", "latitude"}) )); 

        this.setLongitude(Float.valueOf(this.searchRoute( new String[] {stateName.toLowerCase(), "capital", "longitude"}) )); 

        this.getStateDayMeasurementQuery(this.longitude, this.latitude); 

        this.updateDayInfo(weatherInfo); // updates weather icon(i,e, buffer) objects parameters

        // update gui 
        weatherInfo.updateGridUI();

    }
    
    public void getState30DayMeasurementQuery(String stateName) {
        String targetURL = "https://api.openweathermap.org/data/2.5/forecast/climate";
        String query = "lat="+latitude + "&" + "lon="+longitude + "&" + "cnt=10" + "&" +"appid=" +this.configJson.get("key") ;
        String q = targetURL + "?" + query; 
         q = "api.openweathermap.org/data/2.5/forecast/daily?lat=44.34&lon=10.99&cnt=7&appid=" + this.configJson.get("key") ;
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
                         jsonCurr = (JSONObject) obj ; ;
                    }
                } 
            }                
            
            WeatherInfoClient.map.put ( route[route.length - 1],   value )  ;

        }  
    }
    
}