package htron;


import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import htron.loggy.Loggy;


public class App {
    
    Windowise myWindow = null;
    JFrame frame = null; 
    // protected static final Logger logger = Logger.getLogger(null)
    
    public App() {
        // frame = new JFrame("App Friendly");
        // frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // frame.addComponentListener(new ComponentListenerV2());
        // frame.setVisible(true);
        // this.myWindow = new Windowise(frame);
        // frame.addFocusListener(new FocusListenerApp(this.myWindow));
        
    } 

    public static void main (String [] args) {
        
        // new App();

        //logging messages
        for(int i=0; i<1000; i++){
            Loggy.get().log(Level.INFO, "Config data");
        }
        Loggy.get().log(Level.CONFIG, "Config data");

        
    } 

}
