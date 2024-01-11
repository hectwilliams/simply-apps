package htron.statelist;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import htron.FileHelper;


public class Testimonial extends JPanel{

    public class PhotoModule extends JPanel {
        public Image scaledImage = null;
        public String path_img;
        public Dimension circleSize;
        BufferedImage roundedImageBuff;
        BufferedImage imgBuff ;
        private Image img; 

        public PhotoModule() {
            
            setVisible(true);
            
            String path_img = Paths.get(FileHelper.getAssetsPath(), "Statelist", "icons", "her.png").toAbsolutePath().normalize().toString();
            this.circleSize = new Dimension(40, 50);
            this.setPreferredSize(circleSize);
            this.setMaximumSize(circleSize);
            this.setMinimumSize(circleSize);
            
            BufferedImage testImageBuff = new BufferedImage(this.circleSize.width, this.circleSize.height, BufferedImage.TYPE_INT_ARGB);
            testImageBuff = getImage(path_img); // not scaled 
            this.img = testImageBuff; 
            
        }
        
        protected void paintBorder(Graphics g) {
            // this.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(0, 0, 0, 0), BorderFactory.createLineBorder(Color.GREEN, 10))   );
            // g.setColor(Color.WHITE);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawRoundRect(4, 2, 39, 39, 23, 23);
            
       }
       
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 5, 2, this.circleSize.width, this.circleSize.height - 10, this);
         }

         private BufferedImage getImage(String filename) {
            try {
                return ImageIO.read(new File(filename  )  );
            } catch (IOException e) {
                e.printStackTrace();
            } 
            return null; 
        }
        
    }

    public class UserModule extends JLabel {
        public UserModule (String user) {
            super(user);
            this.setText( "@" + user);
            this.setFont(  new Font("Dialog", Font.PLAIN, 10)  );
            this.setForeground(Color.BLACK);
        }
    }

    public class TextModule extends JTextArea {
        private static final  int ROW = 3;
        private static final   int COLUMN = 25;
        public JScrollPane scrollPane;
        public TextModule (String msg) { 
            super(msg,TextModule.ROW, TextModule.COLUMN);
            this.setLineWrap(true);
            this.scrollPane = new JScrollPane(this);
            this.setFont(  new Font("Dialog", Font.PLAIN, 9)  );
            this.setBorder(null);
            this.setBackground(null);
        }
    }

    PhotoModule photoModule;
    UserModule usernameModule;
    TextModule textModule;

    public Testimonial(boolean visible) {
        this.photoModule = new PhotoModule();
        this.usernameModule = new UserModule("placeholder"); // placeholder username
        String placeholderText = "The team is extremely knowledgeable and helpful, it is a delight to work with them";
        this.textModule = new TextModule(placeholderText);
        this.setLayout (new GridBagLayout());
        this.setBorder(  BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(8,5,5,5),  BorderFactory.createMatteBorder(1,0,0,0,Color.GRAY))/*BorderFactory.createLineBorder(Color.MAGENTA, 1) */         );
        this.setPreferredSize(new Dimension(250, 100));
        this.setBackground(Color.BLACK);
        this.setupGrid();
        this.photoModuleAdd();
        this.usernameModuleAdd();
        this.textModuleAdd(); 
        this.setVisible(visible);
    }

    private void photoModuleAdd() {
        // this.photoModule.setBorder(null);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 4;
        c.gridwidth = 3;  

         
        this.add(this.photoModule, c);
        this.setComponentZOrder(this.photoModule, 5);
        this.revalidate();
    }

      private void usernameModuleAdd() {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 4;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 3;    
        this.add(this.usernameModule , c);  
        this.setComponentZOrder(this.usernameModule, 4);
        this.revalidate();
    }

    private void setupGrid () {
        // setup grid
        GridBagConstraints cc = new GridBagConstraints();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cc.anchor = GridBagConstraints.FIRST_LINE_START;
                cc.fill = GridBagConstraints.BOTH;
                cc.weightx = 1;
                cc.weighty = 1;
                cc.gridx = i;
                cc.gridy = j;
                JPanel p = new JPanel();
                p.setBorder(  (BorderFactory.createLineBorder( new Color(255, 255, 255, 0) , 1)) );
                p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                this.add(p, cc);
            }
        }
        this.validate();
    }

    private void textModuleAdd() {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 5;
        c.gridwidth = 10;    
        // this.textModule.setBackground(Color.GRAY);  
        // setBorder(  BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(5,5,0,5), BorderFactory.createLineBorder(Color.MAGENTA, 1)   )        );

        this.add(this.textModule.scrollPane , c);  
        this.setComponentZOrder(this.textModule.scrollPane, 5);
        this.revalidate();        
    }

}
