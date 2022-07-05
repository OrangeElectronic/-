package scraper.model;

import lombok.Data;

@Data
public class ActionResponse<T> {
    public String status;
    public T payload;
}
