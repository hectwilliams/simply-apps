package htron.statelist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

import org.json.JSONException;
import org.json.JSONObject;

import htron.FileHelper;

public class StateLabel extends JPanel {
    String name = "";
    GridBagLayout grid;
    JLabel labelName ;
    String working; 
    static final int N = 60;
    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    Image currImg;
    private JSONObject configJson;
    String filePathJson;
    ArrayList<String> stateArrayList;
    private static int cnt = 0;
    StateListMetricsModuleWrapper metrics; 
    private  Map<String, Boolean > usageMapIcon;
    
    private  IconBlock[] icons = {null, null, null};

    // private void setIcon (IconBlock label, int index) {
    //     this.icons[index] = label;
    // }   
    
    public IconBlock getIcon (int index) {
        return this.icons[index];
    }   

    public int getIconCollectionLength () {
        return this.icons.length;
    }   

    private static final void incrementCnt(int current) {
        /* static because cnt variable is static */
        StateLabel.cnt = current + 1;
    }    

    public final int getCnt () {
        return StateLabel.cnt; 
    }

    public class CircleIcon  extends JPanel {
        Color circleColor;
        boolean mouseEventsValid; 
        IconBlock iconx;
        Color alphaRed = new Color(255, 0, 0, 0 );
        StateLabel statelabel;
        Color currentColor; 
        Ellipse2D dotEllipse2d;

        public CircleIcon (IconBlock iconx, StateLabel statelabel) {
            this.paint(this.getGraphics());
            this.setBorder(null);
            this.setBackground( new Color(255, 255, 255, 0 ) );
            this.setPreferredSize(new Dimension(7, 7));
            this.setIgnoreRepaint(true);
            this.mouseEventsValid = false;
            this.iconx = iconx;
            this.statelabel = statelabel;
            this.currentColor = this.statelabel.getBackground();
            this.dotEllipse2d = new Ellipse2D.Double(0,0,4,4);
        }   
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Graphics2D g2d1 = null;
            IconBlock iconxLocal;
            boolean bool; 

             // clear dots 
            g2d.setColor(this.currentColor );
            g2d.fill(this.dotEllipse2d);

            if (this.mouseEventsValid) {

                if ( (iconx.id.equals( "up") &&  (!statelabel.usageMapIcon.get("up") && statelabel.usageMapIcon.get("down") ) )  || ( iconx.id.equals( "down") &&  (!statelabel.usageMapIcon.get("down") ) && statelabel.usageMapIcon.get("up") ))   {  

                    iconxLocal = statelabel.icons[ iconx.id.equals("up") ? 2 : 1 ]; 
                    statelabel.usageMapIcon.put( iconxLocal.id,  !statelabel.usageMapIcon.get(iconxLocal.id) );
                    g2d1= (Graphics2D) iconxLocal.circleIcon.getGraphics();

                }
                
                // main icon graphic2D object 
                statelabel.usageMapIcon.put( iconx.id,  !statelabel.usageMapIcon.get(iconx.id) );
                bool = this.statelabel.usageMapIcon.get(iconx.id);
                g2d.setColor( bool  ? Color.green  : alphaRed);
                g2d.fill( this.dotEllipse2d);
            
                // thumpy graphic2D object 
                if (g2d1 != null) {
                    g2d1.setColor( this.currentColor);
                    g2d1.fill( this.dotEllipse2d);
                }

            } 

        }
        
    }

    public class IconBlock extends JLabel {
        private int count;
        StateLabel stateLabel;
        int n = 25;
        private String id = "";
        private CircleIcon circleIcon;
        private static final String STATELIST = "Statelist";
        private static final String ASSETS = "assets";
        private static final String HEART = "heart";

        private void setID (String s) {
            this.id = s; 
        }

        public String getID () {
            return this.id;
        }

        public int getCount () {
            return this.count;
        }


        public IconBlock(StateLabel stateLabel, String nameOfFile) {
            String imgUrl;
            int offset; 
            GridBagConstraints gbc;

            this.setID(nameOfFile);

            this.stateLabel = stateLabel;
            this.circleIcon = new CircleIcon(this, stateLabel);
            switch(nameOfFile) {
                case IconBlock.HEART:
                    offset = 0;
                    n = 30;
                    break;
                case "up":
                    offset = 1;
                    break;
                case "down":
                    offset = 2;
                    break;
                default:
                    offset = 0;
                    break;
            }
            
            count = 0;
            imgUrl = Paths.get(FileHelper.rootPath, IconBlock.ASSETS, IconBlock.STATELIST, "icons", nameOfFile + ".png").toAbsolutePath().normalize().toString();
            this.addMouseListener(new MouseOverIcon(stateLabel));
            this.setIcon(new ImageIcon(new ImageIcon(imgUrl).getImage().getScaledInstance(n, n, Image.SCALE_SMOOTH) ));
            this.setBorder(null);

            // active indicator (i.e. color green)
            for (int i = 0; i < 1; i++) {
                gbc = new GridBagConstraints();
                gbc .anchor = GridBagConstraints.FIRST_LINE_START;
                gbc .fill = GridBagConstraints.BOTH;
                gbc .weightx = 1;
                gbc .weighty = 1;
                gbc.gridy = 48;
                gbc.gridx = offset;
                stateLabel.add(this.circleIcon, gbc);
            }

            // add heart , like icon, or dislike 
            gbc = new GridBagConstraints();
            gbc .anchor = GridBagConstraints.FIRST_LINE_START;
            gbc .fill = GridBagConstraints.BOTH;
            gbc .weightx = 1;
            gbc .weighty = 1;
            gbc.gridx =  offset;
            gbc.gridy = 48;
            gbc.gridwidth = 1;
            gbc.gridheight = 6;
            stateLabel.add(this, gbc);
            stateLabel.setComponentZOrder( this , 1);

            stateLabel.validate();
            
        }

        public void updateCounter () {
            this.count += 1;
        }
    }

    public class MouseOverIcon implements MouseListener
    {   
        StateLabel self;
        public MouseOverIcon (StateLabel self) {
            this.self = self; 
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            sched.schedule(
                () -> { 
                    IconBlock ele = (IconBlock) e.getSource();
                    ele.circleIcon.requestFocus();
                    ele.circleIcon.mouseEventsValid = true;
                    ele.circleIcon.paintComponent(ele.circleIcon.getGraphics());
                    
                } , 1, TimeUnit.MILLISECONDS);
            }
            

        @Override
        public void mousePressed(MouseEvent e) {
            /* no op */
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            /* no op */

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            /* no op */
        }

        @Override
        public void mouseExited(MouseEvent e) {
            /* no op */
        }

    }
    

    public StateLabel () {
        String fileNameImage;
        Dimension d = new Dimension(1000,160);  // Monitor Size needed here
        
        this.setMaximumSize(d);
        this.setPreferredSize(d);
        this.setMinimumSize(d);

        this.usageMapIcon = new HashMap<>(Map.of(
            IconBlock.HEART, false,
            "up", false,
            "down", false
        ));

 
        sched.schedule(
            () -> {
            }    
        , 0, TimeUnit.MILLISECONDS);
        
        // state image + button select 
        this.filePathJson = Paths.get(FileHelper.rootPath, IconBlock.ASSETS, "Search",  "states.json").toAbsolutePath().normalize().toString();
        this.stateArrayList = new ArrayList<>();
        this.working = Paths.get(FileHelper.rootPath, IconBlock.ASSETS,  IconBlock.STATELIST, "icons").toAbsolutePath().normalize().toString();
        this.setBorder(null);
   
        this.setLayout(new GridBagLayout());
        this.setupGrid();
        this.setVisible(true);

        // icons 
        this.icons[0] =  (new IconBlock(this, IconBlock.HEART));
        this.icons[1] = new IconBlock(this, "up");
        this.icons[2] = new IconBlock(this, "down");

        //  label image 
        try {

            this.configJson = new JSONObject(  new String(Files.readAllBytes(Paths.get(this.filePathJson)))  );
            this.configJson.keys().forEachRemaining(stateArrayList::add); // add keys to array list 
            Collections.sort(stateArrayList );
            fileNameImage = Paths.get(FileHelper.rootPath , IconBlock.ASSETS,  IconBlock.STATELIST, "imgs", stateArrayList.get(StateLabel.cnt) + ".png" ).toAbsolutePath().normalize().toString();
            this.currImg = ImageIO.read(new File(fileNameImage)) ;      
            StateLabel.incrementCnt(StateLabel.cnt );

        } catch (JSONException | IOException e) {
            
            e.printStackTrace();

        }

        this.loadImage(this.currImg);        
        
        // loads metric module slot 
        this.metrics = new StateListMetricsModuleWrapper(this);
        this.loadMetricGrid(this.metrics );



    }

    private void loadImage(Image img) {

        JLabel labelImage = new JLabel(name) {
            
            @Override 
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0,0, this.getWidth(), this.getHeight(), this);
                this.revalidate();
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();

        gbc .anchor = GridBagConstraints.FIRST_LINE_START;
        gbc .fill = GridBagConstraints.BOTH;
        gbc .weightx = 1;
        gbc .weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 27;
        gbc.gridheight = 45;

        this.add(labelImage, gbc);
        this.setComponentZOrder( labelImage , 2);
    }

    private void setupGrid() {

        for (int x = 0; x < StateLabel.N; x++) {

            for (int y = 0; y < StateLabel.N ; y++) {

                GridBagConstraints gbc = new GridBagConstraints();
                gbc .anchor = GridBagConstraints.FIRST_LINE_START;
                gbc .weightx = 1;
                gbc .weighty = 1;

                gbc .fill = GridBagConstraints.BOTH;
                gbc.gridx = x;
                gbc.gridy = y;
                
                JPanel cell = new JPanel();
                // cell.setBorder(BorderFactory.createLineBorder(Color.PINK));
                this.add(cell, gbc);
            }

        }

        this.validate();
        
    }


    private void loadMetricGrid (StateListMetricsModuleWrapper jpanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc .anchor = GridBagConstraints.FIRST_LINE_START;
        gbc .fill = GridBagConstraints.BOTH;
        gbc.gridy = 3;
        gbc.gridx = 29; 
        gbc.gridwidth = 25;
        gbc.gridheight = 48; 
        this.add(jpanel , gbc);
        this.setComponentZOrder( jpanel , 3);

    }
    
}
