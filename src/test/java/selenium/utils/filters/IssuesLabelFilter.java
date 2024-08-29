package selenium.utils.filters;

import lombok.Getter;

public enum IssuesLabelFilter {
    
    JAVA("C-java");

    @Getter
    private final String label;

    IssuesLabelFilter(String label) {
        this.label = label;
    }
}
