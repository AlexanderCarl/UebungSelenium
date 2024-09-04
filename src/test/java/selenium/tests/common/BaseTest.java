package selenium.tests.common;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import selenium.pageObjects.HomePage;
import selenium.utils.Browser;
import selenium.utils.Utils;

import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    
    private static final Browser browser = Utils.getConfiguredBrowser();
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static ThreadLocal<BrowserWebDriverContainer> tlContainer = new ThreadLocal<>();
    protected Properties prop;

    protected String baseUrl;
    
    @BeforeTest
    public void prepareInit() {
        prop = Utils.getProp();
        baseUrl = prop.getProperty("baseUrl");
    }
    
    @BeforeTest
    public void baseInit() {
        tlDriver.set(getDriver(Utils.getConfiguredBrowser()));
        tlDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        tlDriver.get().manage().window().maximize();
    }
    
    // Necessary as multiple tests are run in a row while the container & driver is alive for all tests of a test-path e.g. Test1 & Test2
    @AfterMethod
    public void clearBrowserData() {
        WebDriver driver = tlDriver.get();
        
        // Clear cookies
        driver.manage().deleteAllCookies();
        
        // Clear local storage and session storage
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
        
        // Navigate to a blank page to reset the browser context
        driver.get("about:blank");
    }
    
    @AfterTest
    public void shutDown() {
        WebDriver driver = tlDriver.get();
        driver.quit();
        tlDriver.remove(); // clean up to avoid memory leaks
        
        if (browser != Browser.LOCAL) {
            tlContainer.get().stop();
            tlContainer.remove();
        }
    }
    
    public HomePage defaultLogin() {
        WebDriver driver = tlDriver.get();
        driver.get(baseUrl);
        
        return new HomePage(driver);
    }
    
    private WebDriver getDriver(Browser browser) {
         switch (browser) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                BrowserWebDriverContainer<?> chromeContainer = new BrowserWebDriverContainer<>().withCapabilities(chromeOptions);
                chromeContainer.start();
                tlContainer.set(chromeContainer);
                return new RemoteWebDriver(chromeContainer.getSeleniumAddress(), chromeOptions);
                
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                BrowserWebDriverContainer<?> firefoxContainer = new BrowserWebDriverContainer<>().withCapabilities(firefoxOptions);
                firefoxContainer.start();
                return new RemoteWebDriver(firefoxContainer.getSeleniumAddress(), firefoxOptions);
                
            // Local browser is for dev use only and does not support parallel execution
            case LOCAL:
                ChromeOptions localChromeOptions = new ChromeOptions();
                Properties prop = Utils.getProp();
                System.setProperty("webdriver.chrome.driver", prop.getProperty("locationChromeDriver"));
                localChromeOptions.setBinary(prop.getProperty("locationChromeBrowser"));
                return new ChromeDriver(localChromeOptions);
                
            default:
                return getDriver(Browser.CHROME);
        }
    }
}
