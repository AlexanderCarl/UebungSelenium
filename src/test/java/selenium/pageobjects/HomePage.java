package selenium.pageobjects;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.pageobjects.common.BasePage;

import java.time.Duration;

public class HomePage extends BasePage {
    
    @Getter
    private final ProjectBoardPage boardPage;
    
    @Getter
    private final HeaderPage headerPage;
    
    public HomePage(WebDriver driver) {
        super(driver);
        this.boardPage = new ProjectBoardPage(driver);
        this.headerPage = new HeaderPage(driver);
    }

    @Override
    protected void waitUntilVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@itemprop='author']/a[contains(text(), 'SeleniumHQ')]")));
    }
}
