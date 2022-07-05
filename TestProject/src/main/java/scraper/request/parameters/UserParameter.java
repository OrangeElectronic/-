package scraper.request.parameters;

import lombok.Value;

@Value
public class UserParameter implements RequestParameter {
    public long userId;
}
