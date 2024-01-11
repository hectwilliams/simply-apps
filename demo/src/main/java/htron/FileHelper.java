package htron;

import java.nio.file.Paths;

public final class FileHelper {

     public static final FileHelper filehelper = new FileHelper();

     private final String assetsPath;
     private final String configPath;
     private final String testPath;

     private FileHelper() {

          /*
           * config.json, in addition to being a config file, is used as a marker file to
           * set the root path
           */
          
          // this.assetsPath = Paths.get(this.getClass().getResource("/assets").getPath(), "../", "../", "assets").toAbsolutePath().normalize().toString();
          // this.configPath = Paths.get(this.getClass().getResource("/config").getPath(), "../", "../", "config").toAbsolutePath().normalize().toString();
          
          String pathSuffix = "target";

          String pathPrefix = "file:";
          
          String locStr = this.getClass().getResource("/assets").getPath();

          int i = locStr.indexOf(pathPrefix);  
          int e = locStr.lastIndexOf(pathSuffix); 

          if (i != -1 && e != -1) {
               locStr = locStr.substring(i + pathPrefix.length(), e  + pathSuffix.length()  );
          }

          this.assetsPath = Paths.get( locStr, "classes", "/assets").toString();
          this.configPath =  Paths.get( locStr, "classes","/config").toString();
          this.testPath = locStr; 
     }

     public static final String getAssetsPath() {
          return filehelper.assetsPath;
     }

     public static final String getConfigPath() {
          return filehelper.configPath;
     }

     public static final String getTestpath() {
          return filehelper.testPath;
     }

}
