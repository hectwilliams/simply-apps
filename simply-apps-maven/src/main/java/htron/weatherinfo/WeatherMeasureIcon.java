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

    Font font;
    JLabel picIcon =  null; 
    public String symbol = "\u0000";
    public String value;

    public static final  Dimension DIMENSION = new Dimension(100, 12); 
    public ImageIcon iconImage;
    public JButton tempFarenheitButton;
    public JButton tempCelciusButton;
    private String id, tmp;
    public String temp_faren; 
    private JButton prevButton;
    private Color darkGrey  = new Color(64  , 64, 64, 128 );
    private WeatherInfoClient weatherInfoClient;
    private Image img;
    public Picture pic; 

    public WeatherMeasureIcon (String id, WeatherInfoClient weatherInfoClient) {
        int w = 20;
        int h = 15;
        String picFileName= ""; 

        this.font = new Font(Font.SANS_SERIF, Font.BOLD, 7);
        this.setFont(this.font);
        this.setPreferredSize(DIMENSION);
        this.setAlignmentX((SwingConstants.CENTER));
        this.id = id;
        this.temp_faren = "0";
        this.pic = null;
        this.weatherInfoClient = weatherInfoClient;
        this.img = null; 
        switch (this.id ) {

            case "temp" : 
                this.tempIconInit();    
                picFileName = "temp";
                break;
            
            case "humid" : 
                this.humidIconInit();
                picFileName = "temp";
                break;

            case "percip":
                this.percipIconInit();
                picFileName = "temp";
                break;

            case "wind":
                this.windIconInit();
                picFileName = "temp";
                break;
            
            case "cloud":
                this.cloudIconInit();
                picFileName = "temp";
                break;
            
            case "pressure":
                this.pressureIconInit();
                picFileName = "temp";
                break;
 
            case "sunrise":
            case "sunset":
                this.sunIconInit();
                picFileName = "temp";
                break;

        }
             try {
                this.iconImage = new ImageIcon(ImageIO.read(new File(    Paths.get(FileHelper.getWorkingDirectoryPath(), "../",  "assets", "Weather", this.id + ".png" ).toAbsolutePath().normalize().toString() )  ).getScaledInstance(w, h, Image.SCALE_SMOOTH) ) ;
                this.pic =  new Picture( (    Paths.get(FileHelper.getWorkingDirectoryPath(), "../",  "assets", "Weather", this.id + ".png" ).toAbsolutePath().normalize().toString() )  );
                this.pic.setPreferredSize(new Dimension(5, 5));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private  void tempIconInit() {
        this.symbol = "\u00B0F";
        this.tempFarenheitButton = new JButton("F" );
        
        this.tempFarenheitButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        
        this.tempCelciusButton = new JButton("C" );
        this.tempCelciusButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        
        this.tempCelciusButton.setMinimumSize(new Dimension(10, 10));
        this.tempFarenheitButton.setMinimumSize(new Dimension(10, 10));
        
        this.prevButton = this.tempFarenheitButton;
        

        // this.temp_faren = String.valueOf((int) (1.8 * ( Double.valueOf( weatherInfoClient.map.get(id))  -273)) + 32); // map.get(return Kevlin value)
        this.updateLabelName( String.valueOf(this.temp_faren), "\u00B0" + "F" );
        this.setToolTipText(this.id);
        this.prevButton.setForeground(this.darkGrey);

        {

            JButton [] arr = {this.tempFarenheitButton, this.tempCelciusButton};
            
            for (JButton button :  arr ) {
                
                button.addActionListener((e) -> {
                    
                    JButton curr = ((JButton) e.getSource());
                    
                    if (this.prevButton != curr) {

                        this.prevButton.setForeground(Color.BLACK);
                        
                        if (  curr == tempFarenheitButton)  {
                            this.updateLabelName(this.temp_faren,  "\u00B0" + "F");
                        } else if ( curr == this.tempCelciusButton ) {
                            this.updateLabelName (   String.valueOf((int)((( Integer.valueOf(this.temp_faren) - 32) * 5) / 9))            ,  "\u00B0" + "C"  ) ; 
                        }
                        curr.setForeground(this.darkGrey);
                        this.prevButton = curr;
                    }
                    
                });
            }
        } 
    }

    private void humidIconInit() {
        this.symbol = " \u0025";
        this.updateLabelName(null, this.symbol);
    }


    private void percipIconInit() {
        this.symbol = " \u0025";
        this.updateLabelName(null , this.symbol);
    }

    private void windIconInit() {
        this.symbol = " m/s";
        this.updateLabelName(null , this.symbol);
    }

    private void cloudIconInit() {
            this.symbol = " \u0025";
        this.updateLabelName(null , this.symbol);
    }

    private void pressureIconInit() {
            this.symbol = " hPa";
        this.updateLabelName(null , this.symbol);
    }

    private void sunIconInit() {
        String str;
        int w = 30;
        int h = 30;

        try {
            this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            str = Paths.get(FileHelper.getWorkingDirectoryPath(), "../",  "assets", "Weather", this.id + ".png" ).toAbsolutePath().normalize().toString();
            this.iconImage = new ImageIcon(ImageIO.read(new File(  str )  ).getScaledInstance(w, h, Image.SCALE_SMOOTH) ) ;
        } catch (IOException e) {
            e.printStackTrace();
        }   

    }


    public void updateLabelName(String value, String unit) {

        if (value == null)  {
            value = "0";
        }
        this.value = value;
        this.setText(value + this.symbol );
    }

    public void updateTempFaren(String value) {
        this.temp_faren = value;
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
