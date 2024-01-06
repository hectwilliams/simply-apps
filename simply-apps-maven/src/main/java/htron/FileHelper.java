package htron;

import java.io.File;
import java.nio.file.Paths;

public class FileHelper {
    String pkgPath ; 
    public FileHelper () {
        pkgPath = this.getClass().getPackage().getName().replace(".", "/");
    }
    
    public final static String getWorkingDirectoryPath ()  {
        String workingDir; 

        FileHelper helper = new FileHelper(); 
        File javaSystemDir = new File("./"); 
        workingDir = Paths.get(javaSystemDir.getAbsolutePath().toString(), helper.pkgPath).toAbsolutePath().normalize().toString();
        
        return workingDir;
    }
}
