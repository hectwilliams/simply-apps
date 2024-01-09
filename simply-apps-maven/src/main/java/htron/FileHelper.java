package htron;

import java.io.File;
import java.nio.file.Paths;

public class FileHelper {
     public static final String rootPath = Paths.get (FileHelper.class.getResource("/").getFile()).toAbsolutePath().normalize().toString() ;

}
