package selenium.tests.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import selenium.pageobjects.HomePage;
import selenium.utils.Utils;

import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    
    protected WebDriver driver;
    protected Properties prop;

    protected String baseUrl;
    
    @BeforeTest
    public void baseInit() {
        prop = Utils.getProp();
        baseUrl = prop.getProperty("baseUrl");
        
        driver = getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().window().maximize();
    }
    
    @AfterTest
    public void shutDown() {
        driver.quit();
    }
    
    public HomePage defaultLogin() {
        driver.get(baseUrl);
        
        return new HomePage(driver);
    }
    
    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", prop.getProperty("locationChromeDriver"));
        ChromeOptions options = new ChromeOptions();
        options.setBinary(prop.getProperty("locationChromeBrowser"));

        return new ChromeDriver(options);
    }
}
