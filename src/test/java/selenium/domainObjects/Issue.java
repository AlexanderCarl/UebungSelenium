package selenium.domainObjects;

import java.util.List;

// Example for a DomainObject of a IssueEntry-Element
public record Issue(String header, List<String> tags, String shortInfo, int numberOfComments) {}
