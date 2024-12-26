package Decorator;

import java.util.List;

public class BasicSearchResult implements SearchResult {
    private final List<String> results;

    public BasicSearchResult(List<String> results) {
        this.results = results;
    }

    @Override
    public List<String> getResults() {
        return results;
    }
}
