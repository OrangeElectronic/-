package scraper.model;

import lombok.Data;

import java.util.List;

@Data
public class ActivityFeed {
    public String timestamp;
    public Integer count;
    public List<Activity> activities;
}
