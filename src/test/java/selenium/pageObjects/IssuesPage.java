package selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium.domainObjects.Issue;
import selenium.pageObjects.common.BasePage;
import selenium.utils.Utils;
import selenium.utils.filters.IssuesLabelFilter;
import selenium.utils.filters.IssuesSortByFilter;

import java.time.Duration;
import java.util.List;

public class IssuesPage extends BasePage {
    
    private final static String issueSelector = "//div[@aria-label='Issues']//div[starts-with(@id, 'issue_')]";
    
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
        driver.findElement(By.xpath("//details[@id='sort-select-menu']/summary")).click();
        
        // Set SortMenu
        driver.findElement(By.xpath("//details[@id='sort-select-menu']/summary/../details-menu//div[contains(@class, 'SelectMenu-list')]/a[contains(@class, 'SelectMenu-item')]/span[contains(text(), '" + filter.getLabel() + "')]")).click();
        Utils.workaround(500);
    }
    
    // Eine gute Idee aber die Testdauer hat sich hiermit fast verdoppelt, da viel mehr Daten geladen werden als gebraucht werden. Es wird nur das 0-Elemente und die comments benötigt.
    public List<Issue> getAllShownIssues() {
        List<WebElement> elements = driver.findElements(By.xpath(issueSelector));
        
        return elements.stream().map(this::getDataFromIssuesElement).toList();
    }
    
    public Issue getIssue(int index) {
        List<WebElement> elements = driver.findElements(By.xpath(issueSelector));
        
        return this.getDataFromIssuesElement(elements.get(index));
    }
    
    public List<Integer> getAllShownIssuesComments() {
        List<WebElement> elements = driver.findElements(By.xpath(issueSelector));
        
        return elements.stream().map(ele -> Integer.parseInt(ele.findElement(By.xpath(".//a/span[contains(@class, 'text-small')]")).getText())).toList();
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
