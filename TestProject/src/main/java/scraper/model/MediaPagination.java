package scraper.model;

import lombok.Data;

@Data
public class MediaPagination extends PageObject<Media> {
    public Integer count;
}
