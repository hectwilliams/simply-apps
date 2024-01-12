package htron.loggy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import java.util.logging.LogRecord;

import htron.FileHelper;

public final class Loggy {

    public static final Loggy loggy = new Loggy();
    private final Logger logger;
    
    private Loggy () {
        this.logger = Logger.getLogger(this.getClass().getName());
        
        // filter 
        Filter filterHandler = new Filter() {
            @Override
            public boolean isLoggable(LogRecord log) {
                // DO NOT log CONFIG  LEVEL 
                return log.getLevel() != Level.CONFIG; 
            }
        };

        //  add Stream Handler to send logging to streams (i.e.) sys.stdout or any file like object 
        StreamHandler streamhandler = new StreamHandler(){
            @Override
            public synchronized  void publish (LogRecord recordData) {
                super.publish(recordData);
            }
            @Override 
            public synchronized void flush() {
                super.flush();
            }
            @Override 
            public synchronized void close() throws SecurityException {
                super.close();
            }
        };

        // format handled message 
        Formatter formatterHandler = new Formatter() {
            @Override
            public String format(LogRecord recordData) {
                return  recordData.getLongThreadID() + "\t" +recordData.getSourceClassName() + "\t" + recordData.getSourceMethodName() + "\t" + new Date(recordData.getMillis()) + "\t" + recordData.getMessage() + "\n";                 
            }
        };

        // console handler 
        ConsoleHandler consoleHandler = new ConsoleHandler();

        // read configuration file 
        try {
            // convert paths: string -> path -> inputstream
            LogManager.getLogManager().readConfiguration(  new FileInputStream(Paths.get( FileHelper.getLogPath() , "logging.properties").toFile())  );
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
  
        
        try {
            Handler filehandler = new FileHandler(  Paths.get( FileHelper.getLogPath(), "tmp", "logger.log").toString() , 2000, 5);

            // filter directives
            filehandler.setFilter(filterHandler);

            // file format directives
            filehandler.setFormatter(formatterHandler);

            // set level
            this.logger.setLevel(Level.FINE);

            // console handler
            this.logger.addHandler(consoleHandler);

            // stream handler 
            this.logger.addHandler(streamhandler);

            // file logger 
            this.logger.addHandler(filehandler);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

    }

    public static final Logger get () {
        return loggy.logger;
    }

    
}
