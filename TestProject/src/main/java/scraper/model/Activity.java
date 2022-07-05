package scraper.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Activity {
    @Id
    public String id;
    public Double timestamp;
    public Integer type;
    public ActivityType activityType;
    @ManyToOne
    @JoinColumn(name="account_id")
    public Account user;
    @ManyToOne
    @JoinColumn(name="media_id")
    public Media media;
    public String text;
}
