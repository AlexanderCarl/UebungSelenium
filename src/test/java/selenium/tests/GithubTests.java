package selenium.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import selenium.domainObjects.Issue;
import selenium.pageObjects.HomePage;
import selenium.pageObjects.IssuesPage;
import selenium.tests.common.BaseTest;
import selenium.utils.Utils;
import selenium.utils.filters.IssuesLabelFilter;
import selenium.utils.filters.IssuesSortByFilter;

import java.util.List;
import java.util.stream.IntStream;

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
        Utils.workaround(500);
        List<Integer> sortedIssuesComments = issuesPage.getAllShownIssuesComments();
        if (!sortedIssuesComments.isEmpty()) {
            boolean isDescending = IntStream.range(0, sortedIssuesComments.size() - 1)
                    .allMatch(i -> sortedIssuesComments.get(i) >= sortedIssuesComments.get(i + 1));
            Assert.assertTrue(isDescending, "The Issues should be ordered from the most commented to the least commented ones.");
        }
        
        // Step 5
        Issue issue = issuesPage.getIssue(0);
        String tags = String.join(" ", issue.tags());
        String issueInfo = "Most commentend infos:" +
                "\nTitle: " + issue.header() +
                "\ntags: " + tags +
                "\nopened: " + issue.shortInfo();
        
        logger.info(issueInfo);
    }
}
