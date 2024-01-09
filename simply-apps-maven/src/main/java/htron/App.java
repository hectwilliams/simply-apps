package htron;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

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
        // new App();
    }

}
