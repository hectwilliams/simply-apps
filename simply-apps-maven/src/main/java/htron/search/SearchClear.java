package htron.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import htron.FileHelper;

public class SearchClear extends JPanel{
    JButton button; 
    String path; 
    Image img;
    int pixelsPerCellX = 5; 
    int pixelsPerCellY = 8;
    SearchTextArea textbox;
    SearchResults searchResults;
    
    public SearchClear (SearchTextArea textbox, SearchResults searchResults) {
        super();

        Dimension d = new Dimension(this.pixelsPerCellX, this.pixelsPerCellY);
        this.setMinimumSize(d);
        this.setMaximumSize(d);
        this.setPreferredSize(d);

        this.path = Paths.get(FileHelper.rootPath, "assets", "Search", "click2.jpeg").toAbsolutePath().normalize().toString();
        this.textbox = textbox; 
        this.searchResults = searchResults ;
        
        try {
            img = ImageIO.read(new File(  path)  ).getScaledInstance(7, 6, Image.SCALE_SMOOTH)    ;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBackground(Color.white);
        label.setMinimumSize(new Dimension(this.pixelsPerCellX, this.pixelsPerCellY));

        // label.setBorder(
        //     new CompoundBorder(
        //         BorderFactory.createEmptyBorder(2, 7, 0, 0),
        //         BorderFactory.createLineBorder(Color.BLACK)
        //     )
        // );
        
        this.setBackground((Color.WHITE));
        this.button = new JButton();

        // this.button.setBorder(new EmptyBorder(-1, 0, 0, 0));
        // this.setBackground(Color.white);
        this.button.setIcon(new ImageIcon(img));    
        this.button.setMinimumSize(new Dimension(this.pixelsPerCellX / 2, this.pixelsPerCellY / 2));
        this.button.addActionListener( new ActionListenerV2(textbox, searchResults));
        this.add(this.button);
        // this.revalidate();

    }


}
