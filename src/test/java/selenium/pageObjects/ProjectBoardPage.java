package selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.pageObjects.common.BasePage;

import java.time.Duration;
import java.util.List;

public class ProjectBoardPage extends BasePage {
    
    public ProjectBoardPage(WebDriver driver) {
        super(driver);
    }
    
    public String getNumberOfCommits() {
        return driver.findElement(By.xpath("//div[@data-testid='latest-commit-details']/..//span[@data-component='text']/span")).getText();
    }
    
    public String getCurrentReleaseVersion() {
        return driver.findElement(By.xpath("//span[@title='Label: Latest']/../span[contains(@class, 'text-bold')]")).getText();
    }

    @Override
    protected void waitUntilVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> {
                    List<WebElement> childEle = driver.findElements(By.xpath("//div[@data-testid='latest-commit']/*"));
                    return childEle.size() == 3;
                });
    }
}
