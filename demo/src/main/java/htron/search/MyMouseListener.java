package htron.search;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import htron.weatherinfo.*;

public class MyMouseListener implements MouseListener {
    public static final WeatherInfoClient weatherInfoClient = new WeatherInfoClient(); // could be static - then need to make
    WeatherInfo weatherInfo;
                                                                                // thread safe
    DebounceFirst<Object> debounce = null;

    public MyMouseListener(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
        this.debounce = new DebounceFirst<>(1400, (Object obj1, Object obj2) -> {
            // WeatherInfo is the 3x3 grid with temperaturem humidty,etc blocks
            WeatherInfo weatherInfoViewer = (WeatherInfo) obj1;
            Picture pic = (Picture) obj2;
            // weather info viewer may be updated via method from weatherinfo
            weatherInfoClient.getStateDayMeasurement(pic.getToolTipText(), weatherInfoViewer);
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.debounce.call(this.weatherInfo, (Picture) e.getSource());
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}