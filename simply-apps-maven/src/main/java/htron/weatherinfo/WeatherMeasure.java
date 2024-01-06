package htron.weatherinfo

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class WeatherMeasure extends JPanel {
    
    public class TimerTask_ implements Runnable {
        
        public Object obj;

        TimerTask_ ( Object obj ){ 
            this.obj = obj;
        }

        @Override
        public void run() {}
    }

    public class LabelMeasurementMouseListener implements MouseListener {
        public TimerTask_ task1, task2;
        public ScheduledFuture<?> future1, future2;

        public LabelMeasurementMouseListener () {
            super();
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}
      
        @Override
        public void mouseEntered(MouseEvent e) {

            this.task1 = new TimerTask_(this ) {
                @Override
                public void run() {
                    System.out.println("entered block");
                    ((JLabel ) e.getSource() ).setFont(modes[1] );
                    // ((JLabel ) e.getSource() ).setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
                }
            };
            this.future1 = sched.schedule(this.task1, 0, TimeUnit.MILLISECONDS);

         }

        @Override
        public void mouseExited(MouseEvent e) {
            this.task2 = new TimerTask_(this ) {
                @Override
                public void run() {
                    ((JLabel ) e.getSource() ).setFont(modes[0] );
                    // ((JLabel ) e.getSource() ).setBorder(null);;

                }
            };
            this.future2 = sched.schedule(this.task2, 0, TimeUnit.MILLISECONDS);
        };

    }


    public LabelMeasurementMouseListener labelMeasurementMouseListener;
    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    Font [] modes = {new Font(Font.SERIF, Font.PLAIN, 12  ),  (new Font(Font.SERIF , Font.BOLD, 12) ) };
    JPanel panel = new JPanel(new GridBagLayout()); 
    WeatherMeasureIcon weatherMeasureIcon;
    public JLabel labelMeasurement;


    public WeatherMeasure (WeatherMeasureIcon weatherMeasureIcon) {
        this.weatherMeasureIcon = weatherMeasureIcon;
        this.labelMeasurement = new JLabel();
        
        this.setGrid();
        this.setPicture(this.weatherMeasureIcon);           // sef grid object  
        this.setLabelMeasurement(this.weatherMeasureIcon); // set grid object
        
    }

    private void setGrid () {
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {

                GridBagConstraints gc = new GridBagConstraints();
                gc .anchor = GridBagConstraints.FIRST_LINE_START;
                gc .weightx = 1;
                gc .weighty = 1;
                gc .fill = GridBagConstraints.BOTH;
                gc.gridx = x;
                gc.gridy = y;
                gc.ipadx = 0;
                gc.ipady = 0;

                JPanel p = new JPanel();
                p.setBorder(BorderFactory.createLineBorder( new Color(255, 255, 255, 150 )  , 1));
                p.setMinimumSize(new Dimension(20, 20));
                panel.add(p, gc);
                
            }
        }
        
        this.add(panel);
    }

    public void updateValue () {
        this.labelMeasurement.setText(this.weatherMeasureIcon.value + " " + this.weatherMeasureIcon.symbol);
    }

    private void setPicture(WeatherMeasureIcon w) {
        GridBagConstraints gc = new GridBagConstraints();
        gc .anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 2;
        gc.gridy = 2;
        gc.ipadx = 0;
        gc.ipady = 0;
        gc.gridwidth = 6;
        gc.gridheight = 5;
    
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.panel.add(w.pic,gc);
        this.panel.setComponentZOrder(w.pic, 1);

    }

    private void setLabelMeasurement(WeatherMeasureIcon w) {
        GridBagConstraints gc = new GridBagConstraints();
        gc .anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.gridy = 7;
        gc.ipadx = 0;
        gc.ipady = 0;
        gc.gridwidth = 9;
        gc.gridheight = 5;
        
        this.labelMeasurement.setText(w.value + " " + w.symbol);
        this.labelMeasurement.setFont(( modes[0] ) );
        this.labelMeasurement.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.labelMeasurementMouseListener = new LabelMeasurementMouseListener();
        this.labelMeasurement.addMouseListener( this.labelMeasurementMouseListener ) ;

        this.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.panel.add(this.labelMeasurement,gc);
        this.panel.setComponentZOrder(this.labelMeasurement, 1);
        this.panel.validate();
    }



}
