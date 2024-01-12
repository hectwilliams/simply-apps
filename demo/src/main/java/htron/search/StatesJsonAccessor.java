package htron.search;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import htron.FileHelper;

public  final class StatesJsonAccessor {
    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    private static final StatesJsonAccessor api = new StatesJsonAccessor();
    private JSONObject dataStates = new JSONObject(); 
    private JSONObject dataConfig = new JSONObject(); 
    private boolean ready = false; 

    private StatesJsonAccessor () {
        sched.schedule(() -> {
            try {
                this.dataStates = new JSONObject (
                new String(Files.readAllBytes(Paths.get( 
                Paths.get(FileHelper.getAssetsPath(), "Search",  "states.json").toString()))));
                this.dataConfig = new JSONObject (
                new String(Files.readAllBytes(Paths.get(  Paths.get(FileHelper.getConfigPath(),  "config.json").toString()))));
                this.ready = true; 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 100, TimeUnit.MILLISECONDS);
        
    }

    public static final JSONObject getStatesJson() {
        return api.dataStates;
    }
    public static final JSONObject getConfigJson() {
        return api.dataConfig;
    }
    public static final boolean getReadyStatus() {
        return api.ready;
    }
}