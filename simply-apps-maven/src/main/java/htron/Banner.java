package htron;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.RenderingHints;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Windowise;
import StateList.StateList;
import MouseListenerBanner;


public class Banner extends JPanel{

    JLabel label; 
    FlowLayout flowlayout;
    String [] labelNames = {"Weather", "StateList"}; 
    public String currPageName;
    public String prevPageName;
    Windowise w;
    MouseListenerBanner appButtonSelectListener;
    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);

    public Banner (Windowise w, StateList statelist)  {

        super(new FlowLayout(FlowLayout.LEFT, 5 ,0));
        
        this.setVisible(false);

        this.currPageName = "weather"; 
        this.prevPageName = this.currPageName;
        this.appButtonSelectListener = new MouseListenerBanner( w);
        // create banner and banner buttons 
        sched.schedule(
            () -> { this.setButtons(w);} , 0, TimeUnit.MILLISECONDS);
        
        // add banner to parent window 
        sched.schedule(() -> { this.placeBanner(w);}, 0, TimeUnit.MILLISECONDS);
    }


    public void placeBanner(Windowise w) {
        GridBagConstraints gc = new GridBagConstraints();
        gc .anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 5;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 1;
        gc.gridy = 8;
        gc.ipadx = 0;
        gc.ipady = 0;
        gc.gridwidth = 200;
        gc.gridheight = 2;
        w.add( this , gc);
        w.setComponentZOrder( this , 0);
    }

    private void setButtons(Windowise w) {
    
        // add labels to contai ner 
        for (int i = 0; i < labelNames.length; i++) {

            JLabel label = new JLabel(labelNames[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Dimension arcs = new Dimension(12,12); //Border corners arcs {width,height}, change this to whatever you want
                    int width = this.getWidth();
                    int height = this.getHeight();
                    Graphics2D graphics = (Graphics2D) g;
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);    //paint border
                }
            };

            label.setOpaque(true);
            label.setFont((new Font(Font.SERIF, Font.PLAIN, 13)));
            label.setBorder(new EmptyBorder(0, 20,0,  20));
            label.addMouseListener(this.appButtonSelectListener ); // mouse listener
            
            // label.setForeground(new Color(255, 255, 255, 40 ));
            label.setEnabled(false);
            this.setBorder( BorderFactory.createCompoundBorder ( BorderFactory.createMatteBorder(0,0,1,0,Color.gray), new EmptyBorder(3, 0, 0, 0)));
            this.add(label);
            // this.revalidate();
        }
            
    }

    public void enableButton(int idx) {
        ((JLabel) this.getComponent(idx)).setEnabled(true);

    }





    // public void setLogo()  {

    //     Image img;

    //     // get first label component 
    //     this.label = (JLabel) componenetsLabels[0]; // first label  
    //     this.label.setBorder(BorderFactory.createLineBorder(Color.orange, 1));
    //     this.label.setVisible(true);
        
    //     // path to file 
    //     this.pathToImg = Paths.get(this.workingdir, "../",  "assets", "Banner",  "atlas.jpeg").toAbsolutePath().normalize().toString();
        
    //     // read image and load into Jlabel component 
    //     try {
            
    //         img = ImageIO.read(new File(  this.pathToImg)  );
    //         img = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH)    ;

    //         // panel
    //         // .getGraphics()
    //         // .drawImage(img /*scaled image*/ , 0, 0, panel);

    //         ImageIcon icon = new ImageIcon(img);
    //         this.label.setIcon((icon));
            
    //     } catch (IOException ioe) {
            
    //         ioe.printStackTrace();
            
    //     }
        

    // }




}
