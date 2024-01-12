package htron.statelist;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import htron.FileHelper;
import htron.Windowise;

public class StateList  extends JScrollPane {

// INNER CLASS 
    public class MouseListenerT implements MouseListener {
        Windowise w; 
        int count;

        public MouseListenerT ( Windowise w) { 
            this.w = w;
            this.count = 0;

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // StateList l = (StateList)e.getSource();
            // sched.schedule(() -> {
            //     // this.w.stateList.requestFocusInWindow() ;
            //     // this.handler(l );
            // }, 0, TimeUnit.MILLISECONDS);
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override 
        public void mouseExited(MouseEvent e) {}

        // private void handler(StateList obj) {
        //     this.count += 1;
        //     // if (this.count > 3) {

        //     //     try {

        //     //         sched.schedule(() -> {
        //     //             this.w.stateList.extendListHeight(w);
        //     //         }, 1000, TimeUnit.MILLISECONDS);

        //     //         sched.wait();

        //     //         System.out.println("list updated");

        //     //     } catch (InterruptedException e) {
        //     //         e.printStackTrace();
        //     //     }
        //     // }
        //     // this.w.stateList.panelView.add(new StateLabel());
        //     // this.w.stateList.panelView.validate();
        //     // this.w.stateList.panelView.repaint();

        //     // this.w.stateList.validate();
        //     // this.w.stateList.repaint();
            
        //     // this.w.frame.pack();
        // }

    }

// MAIN CLASS
    private boolean ready;
    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    public GridBagConstraints gc;
    JPanel panelView;
    String path50Images;

    private void setReadyStatus(boolean state) {
        this.ready = state;
    }
    public boolean getReadyStatus() {
        return this.ready;
    }
    public StateList (Windowise w) {
        
        super(); 
        
        this.panelView = new JPanel();
        this.panelView .setLayout(new BoxLayout(this.panelView, BoxLayout.PAGE_AXIS));
        this.getVerticalScrollBar().setUnitIncrement(10);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.setWheelScrollingEnabled(true);
        
        this.setViewportView(this.panelView);
        this.setVisible(false);
        this.setReadyStatus(false);
        this.placeMeasureGrid(w);
        this .addMouseListener(new MouseListenerT(w));
        sched.schedule( ()-> { this.listStates(w); }, 2, TimeUnit.MILLISECONDS);
    
        
        this.gc = new GridBagConstraints();
        Dimension d = new Dimension(50, 50);

        // force smallscrollpane size to show scroll bars 
        this .setMaximumSize(d); this .setMinimumSize(d); this .setPreferredSize(d);


        path50Images = Paths.get(FileHelper.getAssetsPath(), "Statelist", "imgs").toAbsolutePath().normalize().toString();
    }

    public void listStates (Windowise w) {
    
        // load 50 dummy state images (placeholder)

        sched.schedule( ()-> { 
            for (int i =0; i < new File(path50Images).listFiles().length; i++) {  
                        // System.out.println("state list");
                        // TODO: change to 50 states 
                        w.stateList.panelView.add(new StateLabel());
                        w.stateList.panelView.add(new JPanel());
                    }
                    this.setReadyStatus(true);
                    w.banner.enableButton(1);
                }, 10, TimeUnit.MILLISECONDS);
     
    }

    // state list jpanel dimensions 
    public void placeMeasureGrid(Windowise w) {

        this.gc = new GridBagConstraints();
        this.gc .anchor = GridBagConstraints.FIRST_LINE_START;
        this.gc.weightx = 1;
        this.gc.weighty = 1;
        this.gc.fill = GridBagConstraints.BOTH;
        this.gc.gridx = 1;
        this.gc.gridy = 11;
        this.gc.gridwidth = 100;
        this.gc.gridheight = 55;
        w.add( this  , this.gc);
        w.setComponentZOrder( this  , 2);
        this .validate();
    }

    public void extendListHeight(Windowise w) {
        this.gc.gridheight += 30;
        this.gc = new GridBagConstraints();
        w.revalidate(); 
        w.repaint();
        sched.notifyAll();
    }

}
