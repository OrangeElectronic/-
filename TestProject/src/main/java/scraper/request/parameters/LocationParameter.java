package scraper.request.parameters;

import lombok.Value;

@Value
public class LocationParameter implements RequestParameter {
    public String locationName;
}
