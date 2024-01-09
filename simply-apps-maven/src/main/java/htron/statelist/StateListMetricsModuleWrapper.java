package htron.statelist;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

// import htron.statelist.StateListMetricsModule;
// import htron.statelist.StateList;

public class StateListMetricsModuleWrapper extends JPanel {
    static final int N = 25;
    StateLabel stateLabel; 
    StateListMetricsModule stateListMetricsModule; 

    public StateListMetricsModuleWrapper (StateLabel stateLabel) {
        this.setLayout(new GridBagLayout());
        // this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        this.stateLabel = stateLabel;
        // Dimension d = new Dimension(30,30);  // Monitor Size needed here
        // this.setMaximumSize(d);
        // this.setPreferredSize(d);
        // this.setMinimumSize(d);
        this.stateListMetricsModule = new StateListMetricsModule(stateLabel);

        for (int r = 0; r < StateListMetricsModuleWrapper.N ; r++) {
            for (int c = 0; c < StateListMetricsModuleWrapper.N; c++) { 
                GridBagConstraints gbc = new GridBagConstraints();
                gbc .anchor = GridBagConstraints.FIRST_LINE_START;
                gbc .weightx = 1;
                gbc .weighty = 1;
                gbc .fill = GridBagConstraints.BOTH;
                gbc.gridx = c;
                gbc.gridy = r;
                JPanel cell = new JPanel();
                cell.setBorder( (BorderFactory.createLineBorder(new Color(255, 255, 255, 0 )) ));
                this.add(cell, gbc);
            }
        }

        this.loadMetricsModule();
    }

    private void loadMetricsModule () {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c .fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 25;
        c.gridheight = 50;
        this.add(this.stateListMetricsModule, c); 
        this.setComponentZOrder(this.stateListMetricsModule, 2);
    }

}
