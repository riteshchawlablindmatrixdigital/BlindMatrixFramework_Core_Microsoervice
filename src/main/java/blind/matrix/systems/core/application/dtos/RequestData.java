package blind.matrix.systems.core.application.dtos;

import java.io.Serializable;

public class RequestData implements Serializable {

    private String query;
    private String numberOfResults;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(String numberOfResults) {
        this.numberOfResults = numberOfResults;
    }
}
