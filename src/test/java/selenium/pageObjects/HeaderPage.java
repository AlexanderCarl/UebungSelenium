package selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.pageObjects.common.BasePage;

import java.time.Duration;
import java.util.List;

public class HeaderPage extends BasePage {
    
    public HeaderPage(WebDriver driver) {
        super(driver);
    }
    
    public IssuesPage goToIssues() {
        driver.findElement(By.id("issues-tab")).click();
        
        return new IssuesPage(driver);
    }

    @Override
    protected void waitUntilVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(driver -> {
                    List<WebElement> list = driver.findElements(By.xpath("//div[@id='repository-container-header']//nav/ul/li"));
                    return list.size() == 8;
                });
    }
}
