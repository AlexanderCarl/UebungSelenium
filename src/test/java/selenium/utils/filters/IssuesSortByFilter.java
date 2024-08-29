package selenium.utils.filters;

import lombok.Getter;

public enum IssuesSortByFilter {
    
    MOST_COMMENTED("Most commented");

    @Getter
    private final String label;
    
    IssuesSortByFilter(String label) {
        this.label = label;
    }
    
}
