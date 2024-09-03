package selenium.pageObjects.common;

import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    
    protected WebDriver driver;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUntilVisible();
    }
    
    protected abstract void waitUntilVisible();
}
