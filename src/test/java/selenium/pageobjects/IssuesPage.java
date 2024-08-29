package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.pageobjects.common.BasePage;
import selenium.utils.Utils;
import selenium.utils.filters.IssuesLabelFilter;
import selenium.utils.filters.IssuesSortByFilter;

import java.time.Duration;
import java.util.List;

public class IssuesPage extends BasePage {
    
    public IssuesPage(WebDriver driver) {
        super(driver);
    }
    
    public void setLabelFilter(IssuesLabelFilter filter) {
        WebElement labelFilter = driver.findElement(By.id("label-select-menu"));
        labelFilter.click();
        labelFilter.findElement(By.id("label-filter-field")).sendKeys("java");
        driver.findElement(By.xpath("//div[@data-filterable-for='label-filter-field']/a[not(contains(@style, 'visibility: hidden'))]//div[contains(text(), '" + filter.getLabel() + "')]")).click();
    }
    
    public void setSortByFilter(IssuesSortByFilter filter) {
        // I tried different approaches with WebDriverWait but non did work - feedback would be very nice!
        // new WebDriverWait(driver, Duration.ofSeconds(5))
        //          .until(ExpectedConditions.elementToBeClickable(By.xpath("//details[@id='sort-select-menu']/summary")));

        // Open SortMenu
        Utils.workaround(500);
        WebElement sortByFilter = driver.findElement(By.xpath("//details[@id='sort-select-menu']/summary"));
        sortByFilter.click();
        
        // Set SortMenu
        Utils.workaround(500);
        driver.findElement(By.xpath("//details[@id='sort-select-menu']/summary/../details-menu//div[contains(@class, 'SelectMenu-list')]/a[contains(@class, 'SelectMenu-item')]/span[contains(text(), '" + filter.getLabel() + "')]")).click();
        Utils.workaround(500);
    }
    
    public List<WebElement> getAllShownIssues() {
        return driver.findElements(By.xpath("//div[@aria-label='Issues']//div[contains(@class, 'Box-row')]"));
    }

    @Override
    protected void waitUntilVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("js-issues-search")));
    }
}
