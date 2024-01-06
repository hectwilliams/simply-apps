package htron.weatherinfo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import htron.FileHelper;

public class WeatherMeasureIcon extends JLabel{

    Font myFont;
    JLabel picIcon =  null; 
    private String symbol = "\u0000";
    private static final  String PERCENT_SYMBOL =  " \u0025";
    private String value;
    private static final String ASSETS = "assets";
    private static final String WEATHER = "weather";

    public static final  Dimension DIMENSION = new Dimension(100, 12); 
    private ImageIcon iconImage;
    
    private JButton tempFarenheitButton;
    private JButton tempCelciusButton;
    private final Dimension tempButtonDimension = new Dimension(10, 10);
    private final Font tempButtonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 8); 

    private String id;

    private String tempFarenString; 

    private JButton prevButton;
    private Color darkGrey  = new Color(64  , 64, 64, 128 );
    
    private Picture pic; 

    private void setPic (Picture pic) {
        this.pic = pic;
        this.pic.setPreferredSize(new Dimension(5, 5));
    }

    public final Picture getPic() {
        return this.pic; 
    }

    public String getSymbol() {
        return this.symbol;
    }
    private void setValue(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }

    private void setPrevButton (JButton button) {
        this.prevButton = button;
        this.prevButton.setForeground(this.darkGrey);
    }

    private void setTempFarenString (String s) {
        this.tempFarenString = s;
    }

    private String getTempFarenString () {
        return this.tempFarenString;
    }

    private void setIconImage(ImageIcon imgIcon) {
        this.iconImage = imgIcon;
    }

     public final ImageIcon getIconImage() {
        return this.iconImage;
    }
    
    private void setTempFarenheitButton(JButton button) {
        this.tempFarenheitButton = button;
        this.tempFarenheitButton.setMinimumSize(tempButtonDimension);
        this.tempFarenheitButton.setFont(tempButtonFont);
    }

    public final JButton getTempFarenheitButton() {
        return this.tempFarenheitButton;
    }
    
    private void setTempCelciusButton(JButton button) {
        this.tempCelciusButton = button;
        this.tempCelciusButton.setMinimumSize(tempButtonDimension);
        this.tempCelciusButton.setFont(tempButtonFont);
    }

    public final JButton getTempCelciusButton() {
        return this.tempCelciusButton;
    }


    public WeatherMeasureIcon (String id) {
        int w = 20;
        int h = 15;

        this.myFont = new Font(Font.SANS_SERIF, Font.BOLD, 7);
        this.setFont(this.myFont);
        this.setPreferredSize(DIMENSION);
        this.setAlignmentX((SwingConstants.CENTER));
        this.id = id;
        this.setTempFarenString("0");
        this.pic = null;


        switch (this.id ) {

            case "temp" : 
                this.tempIconInit();    
                break;
            
            case "humid" : 
            case "percip":
            case "cloud":
                this.humidIconInit();
                break;

            case "wind":
                this.windIconInit();
                break;
            
            case "pressure":
                this.pressureIconInit();
                break;
 
            case "sunrise":
            case "sunset":
                this.sunIconInit();
                break;
            
            default:
                break;
        }
             try {
                this.setIconImage(
                    new ImageIcon(ImageIO.read(new File(    Paths.get(FileHelper.getWorkingDirectoryPath(), "../",  ASSETS, WEATHER, this.id + ".png" ).toAbsolutePath().normalize().toString() )  ).getScaledInstance(w, h, Image.SCALE_SMOOTH) ) 
                );

                this.setPic(new Picture( (    Paths.get(FileHelper.getWorkingDirectoryPath(), "../",  ASSETS, WEATHER, this.id + ".png" ).toAbsolutePath().normalize().toString() ) ) );
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private  void tempIconInit() {
        this.symbol = "\u00B0F";

        this.setTempFarenheitButton(new JButton("F" ));
        this.setTempCelciusButton(new JButton("C" ));
         this.setPrevButton(this.getTempFarenheitButton());
  
        
        // this.tempFarenString = String.valueOf((int) (1.8 * ( Double.valueOf( weatherInfoClient.map.get(id))  -273)) + 32); // map.get(return Kevlin value)
        this.updateLabelName( String.valueOf(this.getTempFarenString()) );
        this.setToolTipText(this.id);

        JButton [] arr = {this.getTempFarenheitButton(), this.tempCelciusButton};
        
        for (JButton button :  arr ) {
            
            button.addActionListener( e -> {
                
                JButton curr = ((JButton) e.getSource());
                
                if (this.prevButton != curr) {

                    this.prevButton.setForeground(Color.BLACK);
                    
                    if (  curr == this.getTempFarenheitButton())  {
                        this.updateLabelName(this.getTempFarenString());
                    } else if ( curr == this.tempCelciusButton ) {
                        this.updateLabelName (   String.valueOf( ((( Integer.valueOf(this.getTempFarenString()) - 32) * 5) / 9))            ) ; 
                    }
                    curr.setForeground(this.darkGrey);
                    this.prevButton = curr;
                }
                
            });
        }
    }

    private void humidIconInit() {
        this.symbol = WeatherMeasureIcon.PERCENT_SYMBOL;
        this.updateLabelName(null);
    }


    private void windIconInit() {
        this.symbol = " m/s";
        this.updateLabelName(null );
    }

    private void pressureIconInit() {
            this.symbol = " hPa";
        this.updateLabelName(null );
    }

    private void sunIconInit() {
        String str;
        int w = 30;
        int h = 30;

        try {
            this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            str = Paths.get(FileHelper.getWorkingDirectoryPath(), "../",  ASSETS, WEATHER, this.id + ".png" ).toAbsolutePath().normalize().toString();
            this.setIconImage(new ImageIcon(ImageIO.read(new File(  str )  ).getScaledInstance(w, h, Image.SCALE_SMOOTH) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }   

    }


    public void updateLabelName(String valueIn) {

        if (valueIn == null)  {
            valueIn = "0";
        }

        this.setValue(valueIn);
        this.setText(this.getValue() + this.symbol );
    }

    public void updateTempFaren(String value) {
        this.setTempFarenString(value);
    }

    public String calculatedTime(long t ) {
        return  String.format("%1$TI:%1$TM:%1$TS %1$Tp", t);

    // 'R'	Time formatted for the 24-hour clock as "%tH:%tM"
    // 'T'	Time formatted for the 24-hour clock as "%tH:%tM:%tS".
    // 'r'	Time formatted for the 12-hour clock as "%tI:%tM:%tS %Tp". The location of the morning or afternoon marker ('%Tp') may be locale-dependent.
    // 'D'	Date formatted as "%tm/%td/%ty".
    // 'F'	ISO 8601 complete date formatted as "%tY-%tm-%td".
    // 'c'	Date and time formatted as "%ta %tb %td %tT %tZ %tY", e.g. "Sun Jul 20 16:17:00 EDT 1969".

    }
}
