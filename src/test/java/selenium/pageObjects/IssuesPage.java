package selenium.pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.domainObjects.Issue;
import selenium.pageObjects.common.BasePage;
import selenium.utils.filters.IssuesLabelFilter;
import selenium.utils.filters.IssuesSortByFilter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class IssuesPage extends BasePage {
    
    private static final String issueSelector = "//div[@aria-label='Issues']//div[starts-with(@id, 'issue_')]";
    
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
        
        // Open SortMenu
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until((WebDriver driver) -> {
                   try {
                       driver.findElement(By.xpath("//details[@id='sort-select-menu']")).click();
                       return true;
                   } catch (StaleElementReferenceException e) {
                       return false;
                   }
                });
        
        // Set SortMenu
        driver.findElement(By.xpath("//details[@id='sort-select-menu']/summary/../details-menu//div[contains(@class, 'SelectMenu-list')]/a[contains(@class, 'SelectMenu-item')]/span[contains(text(), '" + filter.getLabel() + "')]")).click();
    }
    
    public Issue getIssue(int index) {
        List<WebElement> elements = driver.findElements(By.xpath(issueSelector));
        
        return this.getDataFromIssuesElement(elements.get(index));
    }
    
    public List<Integer> getAllShownIssuesComments() {
        final List<Integer> numberOfComments = new ArrayList<>();
        
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until((WebDriver driver) -> {
                   try {
                       List<WebElement> elements = driver.findElements(By.xpath(issueSelector));
                       List<Integer> comments = elements.stream().map(ele -> Integer.parseInt(ele.findElement(By.xpath(".//a/span[contains(@class, 'text-small')]")).getText())).toList();
                       numberOfComments.addAll(comments);
                       
                       return true;
                   } catch (StaleElementReferenceException e) {
                       return false;
                   }
                });
        
        return numberOfComments;
    }

    @Override
    protected void waitUntilVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("js-issues-search")));
    }
    
    private Issue getDataFromIssuesElement(WebElement element) {
        String title = element.findElement(By.xpath(".//a[starts-with(@id, 'issue_')]")).getText();
        List<String> tags = element.findElements(By.xpath(".//a[starts-with(@id, 'label-')]"))
                .stream().map(WebElement::getText).toList();
        String shortInfo = element.findElement(By.xpath(".//span[contains(@class, 'opened-by')]")).getText();
        int numberOfComments = Integer.parseInt(element.findElement(By.xpath(".//a/span[contains(@class, 'text-small')]")).getText());
        
        return new Issue(title, tags, shortInfo, numberOfComments);
    }
}
