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
import selenium.pageobjects.HomePage;
import selenium.utils.Browser;
import selenium.utils.Utils;

import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    
    private static final Browser browser = Utils.getConfiguredBrowser();
    private BrowserWebDriverContainer container;
    protected WebDriver driver;
    protected Properties prop;

    protected String baseUrl;
    
    @BeforeTest
    public void baseInit() {
        prop = Utils.getProp();
        baseUrl = prop.getProperty("baseUrl");
        
        setupDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().window().maximize();
    }
    
    @AfterTest
    public void shutDown() {
        driver.quit();
        if (browser != Browser.LOCAL) {
            container.stop();
        }
    }
    
    // Necessary if multiple tests are run in a row as we keep the container & driver alive for multiple tests
    @AfterMethod
    public void clearBrowserData() {
        // Clear cookies
        driver.manage().deleteAllCookies();
    
        // Clear local storage and session storage
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
        ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
    
        // Navigate to a blank page to reset the browser context
        driver.get("about:blank");
    }
    
    public HomePage defaultLogin() {
        driver.get(baseUrl);
        
        return new HomePage(driver);
    }
    
    private void setupDriver() {
        switch (Utils.getConfiguredBrowser()) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                container = new BrowserWebDriverContainer<>().withCapabilities(chromeOptions);
                container.start();
                driver = new RemoteWebDriver(container.getSeleniumAddress(), chromeOptions);
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                container = new BrowserWebDriverContainer<>().withCapabilities(firefoxOptions);
                container.start();
                driver = new RemoteWebDriver(container.getSeleniumAddress(), firefoxOptions);
            case LOCAL:
                ChromeOptions localChromeOptions = new ChromeOptions();
                Properties prop = Utils.getProp();
                System.setProperty("webdriver.chrome.driver", prop.getProperty("locationChromeDriver"));
                localChromeOptions.setBinary(prop.getProperty("locationChromeBrowser"));
                driver = new ChromeDriver(localChromeOptions);
        }
    }
}
