package htron;

import java.nio.file.Paths;

public class FileHelper {
     public static final String ROOTPATH = Paths.get (FileHelper.class.getResource("/").getFile()).toAbsolutePath().normalize().toString();
}
