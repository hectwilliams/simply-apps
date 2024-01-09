package htron.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import htron.FileHelper;

public class SearchIcon extends JLabel{
    
    ImageIcon icon = null;
    String workingdir;
    String imgPath;
    Image img; 
    int pixelsPerCellX = 10; 
    int pixelsPerCellY = 10;
    
    public SearchIcon() {
        
        this.imgPath = Paths.get(FileHelper.rootPath,   "assets", "Search", "search_icon.jpeg").toAbsolutePath().normalize().toString();
        
        Dimension d = new Dimension(this.pixelsPerCellX, this.pixelsPerCellY);
        this.setMinimumSize(d);
        this.setMaximumSize(d);
        this.setPreferredSize(d);

        // read image and load into Jlabel component 
        try {
            
            img = ImageIO.read(new File(  this.imgPath)  );
            img = img.getScaledInstance(10, 10, Image.SCALE_SMOOTH)    ;
            this.setIcon((new ImageIcon(img)));
            this.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.setBackground(Color.white);
            this.setAlignmentY(TOP_ALIGNMENT);

        } catch (IOException ioe) {
            
            ioe.printStackTrace();
            
        }

    }
}