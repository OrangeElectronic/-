package scraper.request.parameters;

import lombok.Value;

@Value
public class MediaCode implements RequestParameter {
    String shortcode;
}
