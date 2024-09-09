package selenium.tests;

import com.google.common.collect.Comparators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import selenium.pageObjects.HomePage;
import selenium.pageObjects.IssuesEntryPage;
import selenium.pageObjects.IssuesPage;
import selenium.tests.common.BaseTest;
import selenium.utils.Utils;
import selenium.utils.filters.IssuesLabelFilter;
import selenium.utils.filters.IssuesSortByFilter;

import java.util.Comparator;
import java.util.List;

public class GithubTests extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    
    @Test
    public void displayHomepage() {
        
        // Step 1
        HomePage homePage = this.defaultLogin();
    
        // Step 2
        logger.info("There are " + homePage.getBoardPage().getNumberOfCommits() + " number of commits");
        logger.info("The current release version is " + homePage.getBoardPage().getCurrentReleaseVersion());
    }
    
    @Test
    public void filterIssues() {
        
        // Step 1
        HomePage homePage = this.defaultLogin();
    
        // Step 2
        IssuesPage issuesPage = homePage.getHeaderPage().goToIssues();
        issuesPage.setLabelFilter(IssuesLabelFilter.JAVA);
        issuesPage.setSortByFilter(IssuesSortByFilter.MOST_COMMENTED);
    
        // Step 3
        List<Integer> sortedIssuesComments = issuesPage.getAmountOfIssuesComments();
        boolean isDescending = Comparators.isInOrder(sortedIssuesComments, Comparator.<Integer>naturalOrder().reversed());
        Assert.assertTrue(isDescending, "The Issues should be ordered from the most commented to the least commented ones.");
    
        // Step 4
        IssuesEntryPage issue = issuesPage.getIssue(0);
        String issueInfo = "Most commented infos:" +
                "\nTitle: " + issue.getTitle() +
                "\ntags: " + String.join(" ", issue.getTags()) +
                "\nopened: " + issue.shortInfo();
        
        logger.info(issueInfo);
    }
}
