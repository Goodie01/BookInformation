package org.goodiemania.api.models.requests;

public class SearchRequest {
    private SearchType type;
    private String searchTerm;

    public SearchRequest() {
    }

    public SearchType getType() {
        return type;
    }

    public void setType(final SearchType type) {
        this.type = type;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(final String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
