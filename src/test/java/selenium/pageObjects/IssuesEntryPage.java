package selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.pageObjects.common.BasePage;

import java.time.Duration;
import java.util.List;

public class IssuesEntryPage extends BasePage {
    
    private WebElement rootElement;
    public IssuesEntryPage(WebDriver driver, WebElement rootElement) {
        super(driver);
        this.rootElement = rootElement;
    }
    
    public String getTitle() {
        return rootElement.findElement(By.xpath(".//a[starts-with(@id, 'issue_')]")).getText();
    }
    
    public List<String> getTags() {
        return  rootElement.findElements(By.xpath(".//a[starts-with(@id, 'label-')]"))
                .stream().map(WebElement::getText).toList();
    }
    
    public String shortInfo() {
        return rootElement.findElement(By.xpath(".//span[contains(@class, 'opened-by')]")).getText();
    }
    
    @Override 
    protected void waitUntilVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("js-issues-search")));
    }
}
