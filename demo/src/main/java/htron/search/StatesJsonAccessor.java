package htron.search;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import htron.FileHelper;

public final class StatesJsonAccessor {
    
    public static final StatesJsonAccessor api = new StatesJsonAccessor();
    private JSONObject data = new JSONObject(); 

    private StatesJsonAccessor () {
        try {
            this.data = new JSONObject (
                new String(Files.readAllBytes(Paths.get( 
                    Paths.get(FileHelper.getAssetsPath(), "Search",  "states.json").toString()))));
            System.out.println(this.data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final JSONObject get() {
        return api.data;
    }
}