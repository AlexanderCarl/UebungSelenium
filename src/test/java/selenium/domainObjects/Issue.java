package selenium.domainObjects;

import java.util.List;

// A single issue could also be a PO itself. For the current use case the plain values are enough. 
public record Issue(String header, List<String> tags, String shortInfo, int numberOfComments) {}
