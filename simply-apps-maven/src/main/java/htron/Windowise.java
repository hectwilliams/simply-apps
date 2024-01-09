package htron;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import htron.statelist.*;
import htron.weatherinfo.*;
import htron.search.*;
import htron.banner.Banner;
import htron.banner.BannerPage;
import htron.idlescreen.IdlePage;

public class Windowise extends JPanel {

    static final int N_SIZE = 150;
    GridBagLayout gridBagLayout = null;
    GridBagConstraints gdConstraints = null;
    public JPanel gridPanel = null;
    public JFrame frame;
    public Search search;
    public WeatherInfo weatherinfo;
    public JScrollPane scrollPane;
    public BannerPage bannerPage;
    public ArrayList<Component> activeComponents;
    public StateList stateList;
    public boolean ready = false;
    public Banner banner;
    public IdlePage idlePage;
    public long time_start;
    // Plot plot;
    // public stateList TempListPage;
    // public WeatherQueryState WeatherQuaeryPage;

    public final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);

    public Windowise(JFrame frame) {
        this.time_start = System.currentTimeMillis();

        this.frame = frame;

        this.ready = false;

        this.activeComponents = new ArrayList<>();

        this.setFrameScrollable();
        this.setWindowFrameGrid(); // sychronous

        idlePage = new IdlePage(this);

        this.stateList = new StateList(this);

        this.banner = new Banner(this, this.stateList); // sychronous + async

        // app1
        this.weatherinfo = new WeatherInfo(this); // sychronous + async
        this.search = new Search(this, this.weatherinfo); // sychronous + async

        // s

        sched.schedule(() -> {
            this.wakeSearchWindow();
            this.frame.pack();
        }, 0, TimeUnit.MILLISECONDS);

    }

    private void setFrameScrollable() {
        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.frame.getContentPane().add(this.scrollPane);

    }

    private void setWindowFrameGrid() {

        this.setLayout(new GridBagLayout());

        for (int x = 0; x < Windowise.N_SIZE; x++) {

            for (int y = 0; y < Windowise.N_SIZE / 2; y++) {

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                gbc.weightx = 1;
                gbc.weighty = 1;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.gridx = x;
                gbc.gridy = y;
                gbc.ipadx = 0;
                gbc.ipady = 0;

                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
                this.add(cell, gbc);
                this.setComponentZOrder(cell, 0);
            }
        }

        this.ready = true;
        this.validate();
    }

    private final  void wakeSearchWindow() {
        JLabel label;

        while (!this.search.searchScrollView.isVisible()) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.frame.toFront();
        this.frame.requestFocus();

        // arbitrarily event required to show viewport :( -- To Be Determined
        label = ((WeatherMeasure) this.weatherinfo.getComponent(0)).getLabelMeasurement();
        label.dispatchEvent(new MouseEvent(label, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(),
                MouseEvent.NOBUTTON, 0, 0, 1, false));
        label.dispatchEvent(new MouseEvent(label, MouseEvent.MOUSE_EXITED, System.currentTimeMillis() + 100,
                MouseEvent.NOBUTTON, 0, 0, 1, false));

        // sched.shutdown();

    }

}
