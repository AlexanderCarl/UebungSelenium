package selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import selenium.pageobjects.HomePage;
import selenium.pageobjects.IssuesPage;
import selenium.tests.common.BaseTest;
import selenium.utils.Utils;
import selenium.utils.filters.IssuesLabelFilter;
import selenium.utils.filters.IssuesSortByFilter;

import java.util.List;

public class GithubTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    
    @Test
    public void displayGitHubSimple() {
        // Step 1
        HomePage homePage = this.defaultLogin();
        
        // Step 2
        logger.info("There are " + homePage.getBoardPage().getNumberOfCommits());
        logger.info("The current release version is " + homePage.getBoardPage().getCurrentReleaseVersion());
        
        // Step 3
        IssuesPage issuesPage = homePage.getHeaderPage().goToIssues();
        issuesPage.setLabelFilter(IssuesLabelFilter.JAVA);
        issuesPage.setSortByFilter(IssuesSortByFilter.MOST_COMMENTED);
        
        // Step 4
        List<WebElement> sortedIssues = issuesPage.getAllShownIssues();
        int previousValue = 0;
        for (int i = 0; i < sortedIssues.size(); i++) {
            String comments = sortedIssues.get(i).findElement(By.xpath("//a/span[contains(@class, 'text-small')]")).getText();
            int numberOfComments = Integer.parseInt(comments);
            
            if (i == 0) {
                previousValue = numberOfComments;
            }

            Assert.assertTrue(previousValue >= numberOfComments, "The Issues should be ordered from the most commented to the least commented ones.");
            previousValue = numberOfComments;
        }
        
        // Step 5
        if (sortedIssues.isEmpty()) {
            logger.info("There are no issues matching your filter. Are you sure this is correct?");
        } else {
            WebElement mci = sortedIssues.get(0);
            String title = mci.findElement(By.xpath("//a[starts-with(@id, 'issue_')]")).getText();
            List<WebElement> titleBoxes = mci.findElements(By.xpath("//a[starts-with(@id, 'label-')]"));
            String buildInfo = titleBoxes.get(0).getText();
            String enhancementInfo = titleBoxes.get(1).getText();
            String openedInfo = mci.findElement(By.xpath("//span[contains(@class, 'opened-by')]")).getText();
            
            String issueInfo = "Most commentend infos:" +
                    "\nTitle: " + title +
                    "\nbuild: " + buildInfo +
                    "\nenhancement " + enhancementInfo +
                    "\nopened" + openedInfo;
            logger.info(issueInfo);
        }
    }
}
