package selenium.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private static Properties prop;
    
    private Utils() {}

    // There is only one prop-file at the moment, therefore no hierarchical loading is required this far.
    public static Properties getProp() {
        if (prop == null) {
            Properties local = new Properties();
            // try (InputStream is = local.getClass().getResourceAsStream("application.properties")) does not work together with intelliJ debugging
            try (FileReader reader = new FileReader("src/test/resources/application.properties")) {
                local.load(reader);
            } catch (Exception e) {
                logger.error("Prop-File could not be loaded!");
            }
            
            prop = local;
        }
        
        return prop;
    }
    
    public static Browser getConfiguredBrowser() {
        String name = getProp().getProperty("browser");
        for (Browser browser : Browser.values()) {
            if (browser.getName().equals(name)) {
                return browser;
            }
        }
        logger.error("No corresponding browser was found for application.properties.browser=" + name + " - Switching to default browser chrome.");
        
        return Browser.CHROME;
    }
}


