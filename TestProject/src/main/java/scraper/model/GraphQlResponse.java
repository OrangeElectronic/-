package scraper.model;

import lombok.Data;

@Data
public class GraphQlResponse<T> {
    public T payload;
}
