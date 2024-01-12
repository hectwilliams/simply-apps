package htron;

import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;

public final class FileHelper {

     private final Map<String, String> resourceMap = new HashMap<>();
     public static final FileHelper filehelper = new FileHelper(); // singleton object

     private FileHelper() {

          String pathSuffix = "target";
          String pathPrefix = "file:";
          int i;
          int e;
          String childOfTargetDir = "";

          // Initialize HashMap
          this.resourceMap.put("/assets", "");
          this.resourceMap.put("/config", "");
          this.resourceMap.put("/log", "");

          // parse, modify, and set value enttries in map to valid path names
          for (Map.Entry<String, String> entry : this.resourceMap.entrySet()) {
               // add resource path to value column in tables
               this.resourceMap.put(entry.getKey(), this.getClass().getResource(entry.getKey()).getPath());

               // find suffix and prefix substrings of element in values column
               i = this.resourceMap.get(entry.getKey()).indexOf(pathPrefix);
               e = this.resourceMap.get(entry.getKey()).lastIndexOf(pathSuffix);

               if (i != -1 && e != -1) {
                    // maven mutated path, clean it , update table
                    this.resourceMap.put(
                              entry.getKey(),
                              this.resourceMap.get(entry.getKey()).substring(i + pathPrefix.length(),
                                        e + pathSuffix.length()));
                    childOfTargetDir = "classes";
               }
               this.resourceMap.put(entry.getKey(),
                         Paths.get(this.resourceMap.get(entry.getKey()), childOfTargetDir, entry.getKey()).toString());

          }
     }

     // getter (gets valid paths )
     public static final String getAssetsPath() {
          return filehelper.resourceMap.get("/assets");
     }

     public static final String getConfigPath() {
          return filehelper.resourceMap.get("/config");
     }

     public static final String getLogPath() {
          return filehelper.resourceMap.get("/log");
     }

}
