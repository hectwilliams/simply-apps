package htron.statelist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


public class StateListMetricsModule extends JScrollPane{

      private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);;
    JPanel metricsPanel;
    JPanel metricsPanelGrid;
    JScrollPane testimonialList;
    JPanel testimonialListClient;

    Testimonial testimonial;
    Graphy graphy;
    StateLabel stateLabel; 

    public StateListMetricsModule (StateLabel stateLabel) {
        metricsPanel = new JPanel(new GridBagLayout());
        // metricsPanel.setBorder(BorderFactory.createLineBorder(Color.PINK));
        this.setViewportView(metricsPanel);
        metricsPanelGrid = new JPanel(new GridBagLayout());
        this.metricsPanelGrid.setToolTipText("Metrics Canvas");
        this.metricsPanelGrid.setPreferredSize(new Dimension(20, 25));
        this.setBorder(  BorderFactory.createCompoundBorder ((BorderFactory.createLineBorder( new Color(255, 255, 255, 149) , 4)) ,BorderFactory.createMatteBorder(0,0,4,0,new Color(255, 255, 255, 149) )) ) ;
        this.stateLabel = stateLabel;   
                
        this.loadButtons();
   
        this.loadActiveGrid();

        sched.schedule(  () -> {
            this.loadActiveMetricComp();
        }, 0, TimeUnit.MILLISECONDS);
        
    }

    private void loadButtons( ) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        this.metricsPanel.add(new StateListMetricsButton("Like", this), c); 

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        this.metricsPanel.add(new StateListMetricsButton("Testimonial", this), c); 

        for (Component button_: this.metricsPanel.getComponents()) {
            ((StateListMetricsButton) button_).setPreferredSize(new Dimension(5, 13));
        }

        // default testimonial 
        ((StateListMetricsButton)this.metricsPanel.getComponent(1)).setBackground( StateListMetricsButton.getActiveColor());
    }

    private void loadActiveGrid() {
        GridBagConstraints c = new GridBagConstraints();
        GridBagConstraints cc = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 3;
        c.insets = new Insets(14, 0, 0, 0);
        
        // this.metricsPanelGrid.setBorder(BorderFactory.createLineBorder(Color.RED));
        this.metricsPanel.add( this.metricsPanelGrid, c);

        // setup grid
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                cc.anchor = GridBagConstraints.FIRST_LINE_START;
                cc.fill = GridBagConstraints.BOTH;
                cc.weightx = 1;
                cc.weighty = 1;
                cc.gridx = i;
                cc.gridy = j;
                JPanel p = new JPanel();
                p.setBorder(  (BorderFactory.createLineBorder( new Color(255, 255, 255, 0) , 1)) );
                this.metricsPanelGrid.add(p, cc);
            }
        }
        this.metricsPanelGrid.validate();
    }

    public void loadActiveMetricComp() {

        // load testomonial 
        this.testimonialList = new JScrollPane();
        this.testimonialList.getVerticalScrollBar().setUnitIncrement(20);
        this.testimonialList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.testimonialList.setPreferredSize(new Dimension(5, 5));
        this.testimonialList.setWheelScrollingEnabled(true);
        this.testimonialList.setBorder(null);
        this.testimonialListClient = new JPanel();
        this.testimonialListClient.setLayout(new BoxLayout(this.testimonialListClient, BoxLayout.PAGE_AXIS));

        this.testimonialList.setViewportView(testimonialListClient);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 19;
        c.gridwidth = 20;    
        
        testimonialListClient.add( new Testimonial(true) );
        testimonialListClient.add( new Testimonial(true) );
        testimonialListClient.add( new Testimonial(false) );    

        this.metricsPanelGrid.add( this.testimonialList, c);
        this.metricsPanelGrid.setComponentZOrder(this.testimonialList, 3);
        this.metricsPanelGrid.validate();

        // load graphy 

        this.graphy = new Graphy(this.stateLabel );
        this.graphy.setVisible(false);
        this.metricsPanelGrid.add( this.graphy, c);
        this.metricsPanelGrid.setComponentZOrder(this.graphy, 3);
    }

}
