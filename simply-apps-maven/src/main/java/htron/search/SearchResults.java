package htron.search;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import htron.FileHelper;
import htron.weatherinfo.*;

public class SearchResults  extends JPanel{   // scrollable client 
    BoxLayout layout;
    JScrollPane scrollPane;
    JLabel label;
    JPanel scrollPaneClient, test;
    String pathToImg;
    Image img; 
    Trie trie;
    WeatherInfo weatherinfo;
    GridBagConstraints gd; 
    GridBagLayout gridBagLayout = null;
    SearchScrollBlock scrollBlockView;
    Picture dummyPic;
    String currString;


    public SearchResults(Trie trie, WeatherInfo weatherinfo, SearchScrollBlock scrollBlockView) {
        super();
        this.trie = trie;
        this.weatherinfo = weatherinfo;
        this.scrollBlockView = scrollBlockView;
        this.dummyPic = new Picture(null);
        this.currString = "";
    }

    public void searchtree (String s) {
        this.currString = s;
        
        ArrayList<String> collection =  this.trie.findAll(s);
        
        this.clearResults();
        
        for ( String key: collection) {
            
            this.pathToImg = Paths.get(FileHelper.rootPath,   "assets", "States", key + ".jpeg").toAbsolutePath().normalize().toString();

            try {
                img = ImageIO.read(new File(  this.pathToImg)  ).getScaledInstance(200, 200, Image.SCALE_SMOOTH) ;
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            Picture pic = new Picture(this.pathToImg);
            pic.addMouseListener(new MyMouseListener(this.weatherinfo));
            pic.setToolTipText(key);

            JLabel jlabel = new JLabel(key);
            jlabel.setFont(new Font("Verdana",1,20));
            
            pic.add(jlabel);
            jlabel.setAlignmentX(TOP_ALIGNMENT);
            jlabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jlabel.setFont(new Font( "Lucida Sans Unicode", Font.PLAIN, 8) );
            jlabel.setForeground(Color.black);

            this.scrollBlockView.addChildToView(pic);
            
            // pic.setPreferredSize(new Dimension(400, 300));
            // pic.setMinimumSize(new Dimension(400, 300));
            // pic.setMaximumSize(new Dimension(400 , 300));
            
        }   
        
        // add dummy components 
        if (collection.size() == 1) {
            this.scrollBlockView.addChildToView(this.dummyPic);
        }   

        this.scrollBlockView.client.revalidate();  // call after addition or removal of componenets s
        this.scrollBlockView.client.setVisible(true);
        this.scrollBlockView.revalidate();
            
    }

    public void clearResults () {
        this.scrollBlockView.client.removeAll();
        this.scrollBlockView.client.setVisible(false);
    }

}
