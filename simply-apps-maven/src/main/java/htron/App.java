package htron;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.swing.JFrame;

public class App {
    
    Windowise myWindow = null;
    JFrame frame = null; 
    
    public App() {
        frame = new JFrame("App Friendly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        this.myWindow = new Windowise(frame);
        frame.addFocusListener(new FocusListenerApp(this.myWindow));
        frame.addComponentListener(new ComponentListenerV2());
    }

    public static void main (String [] args) {
        App app = new App();

    }

}
