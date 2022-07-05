package scraper.request.parameters;

import lombok.Value;

@Value
public class TagName implements RequestParameter {
    public String tag;
}
