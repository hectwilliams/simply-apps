package htron;

import java.nio.file.Files;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import htron.search.StatesJsonAccessor;

public class App {
    
    Windowise myWindow = null;
    JFrame frame = null; 
    
    public App() {
        frame = new JFrame("App Friendly");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        this.myWindow = new Windowise(frame);
        frame.addFocusListener(new FocusListenerApp(this.myWindow));
        frame.addComponentListener(new ComponentListenerV2());
    } 

    public static void main (String [] args) {
        System.out.println(FileHelper.getAssetsPath());
        // System.out.println(StatesJsonAccessor.get());


        
        // System.out.println("hello world");
        // System.out.println(FileHelper.getTestpath().toAbsolutePath().normalize().toString());
        // System.out.println(Files.exists(FileHelper.getTestpath()) );

        App app = new App();
        // System.out.println("hello world");
        // System.out.println(App.class.getName());
        
        // System.out.println(FileHelper.ROOTPATH);
        
        // try {
        //     JSONObject configJson = new JSONObject(  new String(Files.readAllBytes(Paths.get(filePath)))  );
        //     System.out.println(configJson.get("name"));
        // } catch (JSONException | IOException e) {
        //     e.printStackTrace();
        // }
        
    } 

}
