package htron.search;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import htron.Windowise;
import htron.weatherinfo.*;

public class Search extends JPanel{ //could extend panel

    static final int N_SIZE = 100; 
    static final int offset = 10; 

    JPanel parentSubWindowPanel = null;
    GridBagConstraints gd = null;
    GridBagLayout layout;
    public SearchTextArea textbox;
    SearchIcon textboxIcon;
    SearchResults searchlist;
    WeatherInfo weatherinfo;
    SearchClear searchClearButton;
    Trie trie;
    public SearchScrollBlock searchScrollView;
    public Component[] componentActiveList;
    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    Boolean canvasReady = false;
    Boolean trieBlockDone = false;
    int count = 0;
    Windowise w; 
    public Boolean ready = false;
    // JPanel scrollPanel;

    public Search(Windowise w, WeatherInfo weatherinfo) {
        super(new GridBagLayout());
        
        this.ready = false; 

        this.setVisible(false);
        
        this.w = w;

        this.weatherinfo = weatherinfo ;

        this.setParentGrid(w); 
        
        this.trie = new Trie();
        
        this.textboxIcon = new SearchIcon();
        
        this.searchScrollView = new SearchScrollBlock();
        
        this.searchlist =  new SearchResults( this.trie, this.weatherinfo , this.searchScrollView);
        
        this.textbox = new SearchTextArea("Search", this.searchlist, this.weatherinfo ) ;
        
        this.searchClearButton = new SearchClear(this.textbox, this.searchlist);
        
        count = 0;

        sched.schedule( () -> {
            while (!this.searchScrollView.client.isVisible()){
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("ready to set search");
            this.trieBlockDone = true;
            // this.searchScrollView.repaint();
            // this.searchScrollView.revalidate();
            this.setSearchList();
            this.loadStatusCounter();
            
        } , 0, TimeUnit.MILLISECONDS) ;   

        
        sched.schedule( () -> {
            while (!this.canvasReady){
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.setSearchBarIcon(); 
            this.loadStatusCounter();
        } , 0, TimeUnit.MILLISECONDS) ;   

        sched.schedule( () -> {
            while (!this.canvasReady){
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.setSearchBar(); 
            this.loadStatusCounter();
        } , 0, TimeUnit.MILLISECONDS) ;   

        sched.schedule( () -> {
            while (!this.canvasReady){
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.setSearchClearButton(); 
            this.loadStatusCounter();
        } , 0, TimeUnit.MILLISECONDS) ;   

    }

    private void loadStatusCounter() {
        this.count += 1; 

        if (this.count == 4) {
            this.w.banner.enableButton(0);  // banner button for weather app
            this.ready = true; 
            this.w.idlePage.waitIcon.searchStatusReady = true;
        }
    }

    public void hideComponents() {
        for (Component c: this.componentActiveList) {
            c.setVisible(false);
        }
    }

    public void setParentGrid (Windowise w) {
        Dimension d = new Dimension(10, 10);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 15;
        gc.gridy = 15;
        gc.gridwidth = 55;
        gc.gridheight = 40;
        
        // sustains rigidity of window 
        {
            this.setMinimumSize(d);
            this.setMaximumSize(d);
            this.setPreferredSize(d);
        }
        
        w.add( this , gc);
        w.setComponentZOrder( this , 1);

        // load cells into parent object 
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc .anchor = GridBagConstraints.FIRST_LINE_START;
                gbc .fill = GridBagConstraints.BOTH;
                gbc .weightx = 1;
                gbc .weighty = 1;
                gbc.gridx = x;
                gbc.gridy = y;

                JPanel cell = new JPanel();
                this.add(cell, gbc);
            }
        }

        this.canvasReady = true;
    }
    
    public void setSearchList () {
        GridBagConstraints gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 5;
        gc.gridy = 7;
        gc.gridwidth = 40;
        gc.gridheight = 40;

        this.add( this.searchScrollView, gc);
        this.setComponentZOrder( this.searchScrollView  , 1);
    }

    public void setSearchBar () {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 18;
        gc.gridy = 5;
        gc.gridwidth = 9;
        gc.gridheight = 1;
        this.add( this.textbox , gc);
        this.setComponentZOrder( this.textbox , 1);
    }

    public void  setSearchBarIcon () {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 6 + 11  ;
        gc.gridy = 5;
        gc.gridwidth =  2;
        gc.gridheight = 1;
        this.add( this.textboxIcon , gc);
        this.setComponentZOrder( this.textboxIcon , 1);
    }


    public void  setSearchClearButton () {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 27;
        gc.gridy = 5;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        this.add( this.searchClearButton , gc);
        this.setComponentZOrder( this.searchClearButton , 1);
    }


}  
