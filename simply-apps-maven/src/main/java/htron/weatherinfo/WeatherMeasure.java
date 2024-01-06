package htron.weatherinfo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WeatherMeasure extends JPanel {

    public class TimerTaskT implements Runnable {

        TimerTaskT() {
        }

        @Override
        public void run() {
            /* scheduler class handles 'running' of task */
        }

    }

    public class LabelMeasurementMouseListener implements MouseListener {
        private final TimerTaskT[] tasksList = { null, null }; // object at fixed memory with dynamic entries
        private final ScheduledFuture<?>[] futureList = { null, null }; // object at fixed memory with dynamic entries

        public LabelMeasurementMouseListener() {
            super();
        }

        private final TimerTaskT getTask(int id) {
            return tasksList[id];
        }

        private final void setFutures(int id) {
            futureList[id] = sched.schedule(this.getTask(id), 0, TimeUnit.MILLISECONDS);
        }

        private final void setTask(JLabel label, int id) {

            tasksList[id] = new TimerTaskT() {
                @Override
                public void run() {
                    label.setFont(modes[~id]);
                }
            };

            setFutures(id);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            /* not used */
        }

        @Override
        public void mousePressed(MouseEvent e) {
            /* not used */
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            /* not used */
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            this.setTask(label, 0);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JLabel label = (JLabel) e.getSource();
            this.setTask(label, 1);
        }

    }

    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    Font[] modes = { new Font(Font.SERIF, Font.PLAIN, 12), (new Font(Font.SERIF, Font.BOLD, 12)) };
    JPanel panel = new JPanel(new GridBagLayout());
    WeatherMeasureIcon weatherMeasureIcon;
    private JLabel labelMeasurement;

    private final void setLabelMeasurementMouseListener(LabelMeasurementMouseListener newListener) {
        this.labelMeasurement.addMouseListener(newListener);
    }

    public final JLabel getLabelMeasurement() {
        return this.labelMeasurement;
    }

    private final void setLabelMeasurement(JLabel label) {
        this.labelMeasurement = label;
    }

    public WeatherMeasure(WeatherMeasureIcon weatherMeasureIcon) {
        this.weatherMeasureIcon = weatherMeasureIcon;

        this.setLabelMeasurement(new JLabel());

        this.setGrid();
        this.setPicture(this.weatherMeasureIcon); // sef grid object
        this.setLabelMeasurementGrid(this.weatherMeasureIcon); // set grid object

    }

    private void setGrid() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                GridBagConstraints gc = new GridBagConstraints();
                gc.anchor = GridBagConstraints.FIRST_LINE_START;
                gc.weightx = 1;
                gc.weighty = 1;
                gc.fill = GridBagConstraints.BOTH;
                gc.gridx = x;
                gc.gridy = y;
                gc.ipadx = 0;
                gc.ipady = 0;

                JPanel p = new JPanel();
                p.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 1));
                p.setMinimumSize(new Dimension(20, 20));
                panel.add(p, gc);

            }
        }

        this.add(panel);
    }

    public void updateValue() {
        this.labelMeasurement.setText(this.weatherMeasureIcon.getValue() + " " + this.weatherMeasureIcon.getSymbol());
    }

    private void setPicture(WeatherMeasureIcon w) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
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
        this.panel.add(w.getPic(), gc);
        this.panel.setComponentZOrder(w.getPic(), 1);

    }

    private void setLabelMeasurementGrid(WeatherMeasureIcon w) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.gridy = 7;
        gc.ipadx = 0;
        gc.ipady = 0;
        gc.gridwidth = 9;
        gc.gridheight = 5;

        this.labelMeasurement.setText(w.getValue() + " " + w.getSymbol());
        this.labelMeasurement.setFont((modes[0]));
        this.labelMeasurement.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLabelMeasurementMouseListener(new LabelMeasurementMouseListener());

        this.panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.panel.add(this.getLabelMeasurement(), gc);
        this.panel.setComponentZOrder(this.getLabelMeasurement(), 1);
        this.panel.validate();
    }

}
