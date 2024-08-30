package selenium.utils;

import lombok.Getter;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    LOCAL("local");
    
    @Getter
    private final String name;
    
    Browser(String name) {
        this.name = name;
    }
}
