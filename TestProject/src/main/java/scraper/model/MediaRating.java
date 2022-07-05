package scraper.model;

import lombok.Data;

import java.util.List;

@Data
public class MediaRating {
    public PageObject<Media> media;
    public List<Media> topPosts;
}
