package selenium.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.Properties;

public class Utils {
    
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    
    private Utils() {}

    // There is only one prop-file at the moment, therefore no hierarchical loading is required this far.
    public static Properties getProp() {
        Properties prop = new Properties();
        try (FileReader reader = new FileReader("src/test/resources/application.properties")) {
            prop.load(reader);
        } catch (Exception e) {
            logger.error("Prop-File could not be loaded!");
        }
        
        return prop;
    }
    
    public static void workaround(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


