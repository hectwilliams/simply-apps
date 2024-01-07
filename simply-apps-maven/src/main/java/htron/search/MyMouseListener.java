package jx_review.java_fun_child.weather_app.src.Search;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jx_review.java_fun_child.weather_app.src.WeatherInfo.Picture;
import jx_review.java_fun_child.weather_app.src.WeatherInfo.WeatherInfo;

public class MyMouseListener implements MouseListener {
    WeatherInfo weatherInfo;
    DebounceFirst<Object> debounce = null;

    public MyMouseListener(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
        this.debounce = new DebounceFirst<Object>(1400, (Object obj1, Object obj2) -> {
            WeatherInfo winfo = (WeatherInfo) obj1;
            Picture pic = (Picture) obj2;
            this.weatherInfo.weatherApi.getStateDayMeasurement(pic.getToolTipText() /*state*/, winfo);
            this.debounce.scheduledFuture  = null; 
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.debounce.call(this.weatherInfo, (Picture)e.getSource());
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}